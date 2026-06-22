package com.shiladitya.graph.dto;

import com.shiladitya.graph.model.GraphNode;
import com.shiladitya.graph.model.NodeTransaction;

import java.util.List;

public class GraphNodeResponse {

    private String id;
    private String parentId;
    private String name;
    private String accountNumber;

    private int level;

    private boolean root;

    private boolean leaf;

    private List<GraphNode> parentChain;

    private List<GraphNode> children;

    private List<ChildrenTreeDto> childrenTreeDtoList;

    private List<NodeTransaction> transactions;

    private List<NodeTransaction> nextLevelTransactions;

    private SubTreeAggregateInfoDto subTreeAggregateInfoDto;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public int getLevel() {
        return level;
    }

    public boolean isRoot() {
        return root;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public List<GraphNode> getParentChain() {
        return parentChain;
    }

    public List<GraphNode> getChildren() {
        return children;
    }

    public List<NodeTransaction> getTransactions() {
        return transactions;
    }

    public List<NodeTransaction> getNextLevelTransactions() {
        return nextLevelTransactions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public void setParentChain(List<GraphNode> parentChain) {
        this.parentChain = parentChain;
    }

    public void setChildren(List<GraphNode> children) {
        this.children = children;
    }

    public void setTransactions(List<NodeTransaction> transactions) {
        this.transactions = transactions;
    }

    public void setNextLevelTransactions(
            List<NodeTransaction> nextLevelTransactions) {

        this.nextLevelTransactions = nextLevelTransactions;
    }

    public List<ChildrenTreeDto> getChildrenTreeDtoList() {
        return childrenTreeDtoList;
    }

    public void setChildrenTreeDtoList(List<ChildrenTreeDto> childrenTreeDtoList) {
        this.childrenTreeDtoList = childrenTreeDtoList;
    }

    public SubTreeAggregateInfoDto getSubTreeAggregateInfoDto() {
        return subTreeAggregateInfoDto;
    }

    public void setSubTreeAggregateInfoDto(SubTreeAggregateInfoDto subTreeAggregateInfoDto) {
        this.subTreeAggregateInfoDto = subTreeAggregateInfoDto;
    }
}