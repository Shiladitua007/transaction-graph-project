package com.shiladitya.graph.dto;

import java.util.Map;

public class SubTreeAggregateInfoDto {

   private Map<Integer, AggregateInfo> aggregateInfoMap;

    public Map<Integer, AggregateInfo> getAggregateInfoMap() {
        return aggregateInfoMap;
    }

    public void setAggregateInfoMap(Map<Integer, AggregateInfo> aggregateInfoMap) {
        this.aggregateInfoMap = aggregateInfoMap;
    }
}
