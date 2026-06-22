package com.shiladitya.graph.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiladitya.graph.constants.GraphConstants;
import com.shiladitya.graph.model.GraphData;
import com.shiladitya.graph.model.GraphNode;
import com.shiladitya.graph.repository.GraphRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class GraphDataLoaderService {

    private final GraphRepository repository;

    public GraphDataLoaderService(GraphRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void load() throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        GraphData graphData =
                mapper.readValue(
                        new ClassPathResource(
                                "transactions-graph-nodes.json")
                                .getInputStream(),
                        GraphData.class);

        for (GraphNode node : graphData.getNodes()) {

            repository.getNodeById().put(node.getId(), node);

            String parentKey =
                    node.getParentId() == null
                            ? GraphConstants.ROOT
                            : node.getParentId();

            repository.getChildrenByParentId()
                    .computeIfAbsent(
                            parentKey,
                            k -> new ArrayList<>())
                    .add(node);
        }

    }
}
