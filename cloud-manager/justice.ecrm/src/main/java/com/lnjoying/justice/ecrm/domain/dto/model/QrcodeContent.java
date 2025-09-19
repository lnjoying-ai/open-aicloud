package com.lnjoying.justice.ecrm.domain.dto.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QrcodeContent
{
    private String vendor;

    private String uuid;

    private String mac;
}
