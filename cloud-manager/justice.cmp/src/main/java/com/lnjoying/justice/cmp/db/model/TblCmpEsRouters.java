package com.lnjoying.justice.cmp.db.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class TblCmpEsRouters extends TblCmpOsRouters implements Serializable {

    private String firewallId;

    private static final long serialVersionUID = 1L;
}