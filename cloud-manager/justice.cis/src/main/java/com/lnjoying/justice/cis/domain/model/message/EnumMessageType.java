package com.lnjoying.justice.cis.domain.model.message;

public enum EnumMessageType {
    // push from here to other app store
    PUSH,

    // pull from other app store to here
    PULL,

    // receive message from other app store to here
    NOTICE,

    // download package by other
    BE_DOWNLOADED
}
