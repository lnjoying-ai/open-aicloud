package com.lnjoying.justice.omc.grafana.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/12/12 20:02
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GrafanaDashboard
{
    @JsonProperty("id")
    private Long id;

    @JsonProperty("uid")
    private String uid;

    @JsonProperty("title")
    private String title;

    @JsonProperty("uri")
    private String uri;

    @JsonProperty("url")
    private String url;

    @JsonProperty("slug")
    private String slug;

    @JsonProperty("type")
    private String type;

    @JsonProperty("tags")
    private List<String> tags;

    @JsonProperty("isStarred")
    private boolean isStarred;

    @JsonProperty("sortMeta")
    private int sortMeta;

}
