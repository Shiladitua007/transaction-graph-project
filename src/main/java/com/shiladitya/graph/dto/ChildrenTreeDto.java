package com.shiladitya.graph.dto;

import com.shiladitya.graph.model.NodeTransaction;

import java.util.ArrayList;
import java.util.List;

public class ChildrenTreeDto {

    private String id;

    private String name;

    private List<ChildrenTreeDto> children = new ArrayList<>();

    private List <NodeTransaction> transactions;

    private Integer level;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ChildrenTreeDto> getChildren() {
        return children;
    }

    public void setChildren(List<ChildrenTreeDto> children) {
        this.children = children;
    }

    public List<NodeTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<NodeTransaction> transactions) {
        this.transactions = transactions;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
