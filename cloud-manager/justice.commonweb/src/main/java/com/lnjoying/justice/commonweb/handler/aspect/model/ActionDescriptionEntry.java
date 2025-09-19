package com.lnjoying.justice.commonweb.handler.aspect.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionDescriptionEntry
{
    private String language;
    private String createActionDescription;
    private String updateActionDescription;
    private String deleteActionDescription;
}