package com.shiladitya.graph.controller;

import com.shiladitya.graph.dto.GraphNodeResponse;
import com.shiladitya.graph.service.GraphService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/graph")
public class GraphController {

    private final GraphService graphService;

    public GraphController(
            GraphService graphService) {

        this.graphService = graphService;
    }

    @GetMapping("/nodes/{id}")
    public ResponseEntity<GraphNodeResponse> getNode(
            @PathVariable String id,
            @RequestParam(required = false, defaultValue = "1") Integer maxDepth) {
        return ResponseEntity.status(HttpStatus.OK).body(graphService.getNode(id, maxDepth));
    }
}