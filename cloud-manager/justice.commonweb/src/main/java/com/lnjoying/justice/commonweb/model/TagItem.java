package com.lnjoying.justice.commonweb.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/9/26 14:23
 */

@Data
public class TagItem
{

    @NotBlank
    private String name;

    private String value;
}
