package com.lnjoying.justice.schema.entity.dev;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabelSelector implements Serializable
{
    private String type;

    private String operator;

    private String key;

    private String value;
}
