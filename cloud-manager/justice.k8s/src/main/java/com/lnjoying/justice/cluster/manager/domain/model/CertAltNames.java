package com.lnjoying.justice.cluster.manager.domain.model;

import lombok.Data;

import java.util.List;

/**
 * @Author: Regulus
 * @Date: 11/17/21 7:07 PM
 * @Description:
 */
@Data
public class CertAltNames
{
    private List<String> dnsNames;
    private List<String> ips;
}
