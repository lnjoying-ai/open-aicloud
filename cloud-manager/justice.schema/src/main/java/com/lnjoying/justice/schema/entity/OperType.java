package com.lnjoying.justice.schema.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperType
{
    private String oper_type;
    private Map<String, String> desc;
}