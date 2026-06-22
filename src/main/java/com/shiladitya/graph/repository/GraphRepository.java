package com.shiladitya.graph.repository;

import com.shiladitya.graph.model.GraphNode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GraphRepository {

    private final Map<String, GraphNode> nodeById =
            new ConcurrentHashMap<>();

    private final Map<String, List<GraphNode>>
            childrenByParentId =
            new ConcurrentHashMap<>();

    public Map<String, GraphNode> getNodeById() {
        return nodeById;
    }

    public Map<String, List<GraphNode>> getChildrenByParentId() {
        return childrenByParentId;
    }
}
