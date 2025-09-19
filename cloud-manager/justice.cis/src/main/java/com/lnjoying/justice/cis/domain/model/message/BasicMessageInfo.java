package com.lnjoying.justice.cis.domain.model.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasicMessageInfo {

    private String name;

    private String provider;

    private String version;

    private String affinity;

    private String shortDesc;

    private String industry;

    private String type;

    public BasicMessageInfo() {

    }

    /**
     * init this object by packageDto.
     *
     */
    public BasicMessageInfo(Object obj) {

    }
}
