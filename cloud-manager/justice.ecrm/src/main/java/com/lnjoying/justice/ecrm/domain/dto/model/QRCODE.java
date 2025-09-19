package com.lnjoying.justice.ecrm.domain.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QRCODE
{
    @SerializedName("format")
    @JsonProperty("format")
    private String format;

    @SerializedName("coding")
    @JsonProperty("coding")
    private String coding;

    @SerializedName("content")
    @JsonProperty("content")
    private String content;
}
