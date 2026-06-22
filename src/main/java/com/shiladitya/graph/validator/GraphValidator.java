package com.shiladitya.graph.validator;

import com.shiladitya.graph.exception.InvalidDepthException;
import com.shiladitya.graph.exception.NodeNotFoundException;
import com.shiladitya.graph.model.GraphNode;
import org.springframework.stereotype.Component;

@Component
public class GraphValidator {

    public void validateMaxDepth(Integer maxDepth) {
        if (maxDepth < 0 || maxDepth > 5) {
            throw new InvalidDepthException(
                    "maxDepth must be between 0 and 5");
        }
    }

    public void validateNodeExists(GraphNode node, String nodeId) {
        if (node == null) {
            throw new NodeNotFoundException(
                    "Graph node "
                            + nodeId
                            + " does not exist");
        }
    }
}
