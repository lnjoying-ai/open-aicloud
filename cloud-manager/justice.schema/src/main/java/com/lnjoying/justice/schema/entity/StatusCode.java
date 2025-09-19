package com.lnjoying.justice.schema.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "code")
public class StatusCode
{
    private int code;
    private Map<String, String> desc;
}
