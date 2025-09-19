package com.lnjoying.justice.schema.entity.dev;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.schema.common.scheduler.LabelType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchedulingStrategy implements Serializable
{
    @SerializedName("label_selector_map")
    @JsonProperty("label_selector_map")
    private Map<String, List<LabelSelector>> labelSelectorMap;

    @SerializedName("replica_complete_strategy")
    @JsonProperty("replica_complete_strategy")
    private boolean replicaCompleteStrategy;

    @SerializedName("on_one_node")
    @JsonProperty("on_one_node")
    private Integer onOneNode;

    @SerializedName("target_nodes")
    @JsonProperty("target_nodes")
    private List<TargetNode> targetNodes;

    //Later version extend, default is null
    @SerializedName("strategy")
    @JsonProperty("strategy")
    private String strategy;

    public void addRegionPerferLabelSelector(String regionId)
    {
        if (getLabelSelectorMap() == null)
        {
            setLabelSelectorMap(new HashMap<>());
        }
        LabelSelector regionPerferSelector = new LabelSelector("Prefer", "In", "regionId", regionId);
        if (getLabelSelectorMap().get(LabelType.REGION.getValue()) == null)
        {
            getLabelSelectorMap().put(LabelType.REGION.getValue(), Arrays.asList(regionPerferSelector));
        }
        else
        {
            getLabelSelectorMap().get(LabelType.REGION.getValue()).add(regionPerferSelector);
        }
    }

    public void addSitePerferLabelSelector(String siteId)
    {
        if (getLabelSelectorMap() == null)
        {
            setLabelSelectorMap(new HashMap<>());
        }
        LabelSelector sitePerferSelector = new LabelSelector("Prefer", "In", "siteId", siteId);
        if (getLabelSelectorMap().get(LabelType.SITE.getValue()) == null)
        {
            getLabelSelectorMap().put(LabelType.SITE.getValue(), Arrays.asList(sitePerferSelector));
        }
        else
        {
            getLabelSelectorMap().get(LabelType.SITE.getValue()).add(sitePerferSelector);
        }
    }
}
