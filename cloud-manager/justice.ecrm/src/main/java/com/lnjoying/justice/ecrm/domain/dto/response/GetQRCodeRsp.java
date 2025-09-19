package com.lnjoying.justice.ecrm.domain.dto.response;

import com.lnjoying.justice.ecrm.domain.dto.model.QRCODE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetQRCodeRsp
{
    QRCODE qrcode;
}
