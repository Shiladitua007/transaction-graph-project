package com.shiladitya.graph.service;

import com.shiladitya.graph.dto.AggregateInfo;
import com.shiladitya.graph.dto.ChildrenTreeDto;
import com.shiladitya.graph.dto.GraphNodeResponse;
import com.shiladitya.graph.dto.SubTreeAggregateInfoDto;
import com.shiladitya.graph.exception.CyclicException;
import com.shiladitya.graph.model.GraphNode;
import com.shiladitya.graph.model.NodeTransaction;
import com.shiladitya.graph.repository.GraphRepository;
import com.shiladitya.graph.validator.GraphValidator;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GraphService {

    private final GraphRepository repository;

    private final GraphValidator validator;

    public GraphService(GraphRepository repository, GraphValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public GraphNodeResponse getNode(String nodeId, Integer maxDepth) {

        validator.validateMaxDepth(maxDepth);

        GraphNode node = repository.getNodeById().get(nodeId);

        validator.validateNodeExists(node, nodeId);

        List<GraphNode> children =
                repository.getChildrenByParentId()
                        .getOrDefault(
                                nodeId,
                                Collections.emptyList());

        GraphNodeResponse response =
                new GraphNodeResponse();
        response.setId(node.getId());
        response.setParentId(node.getParentId());
        response.setName(node.getName());
        response.setAccountNumber(node.getAccountNumber());
        response.setLevel(calculateLevel(node));
        response.setRoot(isRoot(node));
        response.setLeaf(children.isEmpty());
        response.setParentChain(buildParentChain(node));
        response.setChildren(children);
        if(maxDepth>1) {
            ChildrenTreeDto childrenTreeDto =
                    buildChildrenTree(
                            node,
                            0,
                            maxDepth,
                            new HashSet<>());
            List<ChildrenTreeDto> flattenChildrenTreeDto =
                    flattenChildrenTree(childrenTreeDto, new ArrayList<>());
            response.setChildrenTreeDtoList(flattenChildrenTreeDto);

            response.setSubTreeAggregateInfoDto(
                    subTreeAggregateInfo(flattenChildrenTreeDto));
        }
        response.setTransactions(node.getTransactions());
        response.setNextLevelTransactions(getChildTransactions(children));

        return response;
    }

    private boolean isRoot(GraphNode node) {

        String parentId = node.getParentId();

        return parentId == null
                || !repository.getNodeById()
                .containsKey(parentId);
    }

    private int calculateLevel(GraphNode node) {
        int level = 0;

        String parentId = node.getParentId();

        Set<String> visited = new HashSet<>();

        while (parentId != null) {
            if (!visited.add(parentId)) {
                throw new CyclicException(
                        "Cycle detected in hierarchy at node: " + parentId);
            }

            GraphNode parent = repository.getNodeById().get(parentId);

            if (parent == null) {
                return 0;
            }

            level++;

            parentId = parent.getParentId();
        }
        return level;
    }

    private List<GraphNode> buildParentChain(GraphNode node) {

        List<GraphNode> chain = new ArrayList<>();

        String parentId = node.getParentId();

        Set<String> visited = new HashSet<>();

        while (parentId != null) {

            if (!visited.add(parentId)) {
                throw new CyclicException(
                        "Cycle detected at node: " + parentId);
            }

            GraphNode parent = repository.getNodeById().get(parentId);
            if (parent == null) {
                return Collections.emptyList();
            }

            chain.add(parent);

            parentId = parent.getParentId();
        }
        Collections.reverse(chain);
        return chain;
    }

    private List<NodeTransaction> getChildTransactions(List<GraphNode> children) {
        List<NodeTransaction> result = new ArrayList<>();

        for (GraphNode child : children) {

            if (child.getTransactions() != null) {
                result.addAll(child.getTransactions());
            }
        }
        return result;
    }

    private ChildrenTreeDto buildChildrenTree(
            GraphNode node,
            int currentDepth,
            int maxDepth,
            Set<String> visited) {

        if (currentDepth > maxDepth) {
            return null;
        }

        if (!visited.add(node.getId())) {
            return null;
        }

        ChildrenTreeDto tree = new ChildrenTreeDto();

        tree.setId(node.getId());
        tree.setName(node.getName());
        tree.setLevel(currentDepth);
        tree.setTransactions(node.getTransactions());

        if (currentDepth < maxDepth) {

            List<GraphNode> children =
                    repository.getChildrenByParentId()
                            .getOrDefault(
                                    node.getId(),
                                    Collections.emptyList());

            for (GraphNode child : children) {

                ChildrenTreeDto childTree =
                        buildChildrenTree(
                                child,
                                currentDepth + 1,
                                maxDepth,
                                visited);

                if (childTree != null) {
                    tree.getChildren().add(childTree);
                }
            }
        }

        visited.remove(node.getId());

        return tree;
    }

    private List<ChildrenTreeDto> flattenChildrenTree(
            ChildrenTreeDto node,
            List<ChildrenTreeDto> result) {

        for (ChildrenTreeDto child : node.getChildren()) {

            result.add(child);

            flattenChildrenTree(child, result);
        }
        return result;
    }

    private SubTreeAggregateInfoDto subTreeAggregateInfo(
            List<ChildrenTreeDto> childrenTreeDtoList) {

        SubTreeAggregateInfoDto subTreeAggregateInfoDto = new SubTreeAggregateInfoDto();
        Map<Integer, AggregateInfo> aggregateInfoMap = new HashMap<>();

        Set<Integer> levelSet = new HashSet<>();

        for (ChildrenTreeDto childrenTreeDto : childrenTreeDtoList) {
            levelSet.add(childrenTreeDto.getLevel());
        }

        for (Integer level : levelSet) {
            AggregateInfo aggregateInfo = new AggregateInfo();
            aggregateInfo.setLevel(level);
            aggregateInfo.setNodeCount(0);
            aggregateInfo.setTxCount(0);
            aggregateInfo.setTotalTxAmount(0.0);
            aggregateInfoMap.put(level, aggregateInfo);
        }

        for (Integer level : levelSet) {
            for (ChildrenTreeDto childrenTreeDto : childrenTreeDtoList) {
                if(Objects.equals(level, childrenTreeDto.getLevel())) {
                    aggregateInfoMap.get(level).setNodeCount(
                            aggregateInfoMap.get(level).getNodeCount()+1);
                    for(NodeTransaction nodeTransaction : childrenTreeDto.getTransactions()) {
                        aggregateInfoMap.get(level).setTxCount(
                                aggregateInfoMap.get(level).getTxCount()+1);
                        aggregateInfoMap.get(level).setTotalTxAmount(
                                aggregateInfoMap.get(level).getTotalTxAmount() + nodeTransaction.getAmount());
                    }
                }
            }
        }

        subTreeAggregateInfoDto.setAggregateInfoMap(aggregateInfoMap);

        return subTreeAggregateInfoDto;
    }
}