package com.lnjoying.justice.commonweb.handler.aspect.model;

import com.lnjoying.justice.commonweb.handler.aspect.enums.SensitiveType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensitiveFieldDefinition
{
    private Field field;
    private List<String> fieldPathList;
    private Object fieldValue;
    private Object obj;
    private SensitiveType sensitiveType;
}