package com.shiladitya.graph.dto;

public class AggregateInfo {

    private Integer level;

    private Integer nodeCount;

    private Integer txCount;

    private Double totalTxAmount;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getNodeCount() {
        return nodeCount;
    }

    public void setNodeCount(Integer nodeCount) {
        this.nodeCount = nodeCount;
    }

    public Integer getTxCount() {
        return txCount;
    }

    public void setTxCount(Integer txCount) {
        this.txCount = txCount;
    }

    public Double getTotalTxAmount() {
        return totalTxAmount;
    }

    public void setTotalTxAmount(Double totalTxAmount) {
        this.totalTxAmount = totalTxAmount;
    }
}
