package com.lnjoying.justice.cluster.manager.domain.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Regulus
 * @Date: 11/25/21 10:45 AM
 * @Description:
 */
@Data
public class TipMessage implements Serializable
{
    private String level = TipLevel.INFO;
    private String message;
}
