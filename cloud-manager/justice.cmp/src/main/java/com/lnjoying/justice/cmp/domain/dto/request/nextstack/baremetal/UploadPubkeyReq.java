package com.lnjoying.justice.cmp.domain.dto.request.nextstack.baremetal;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import java.util.regex.Pattern;

@EqualsAndHashCode(callSuper = true)
@Data
public class UploadPubkeyReq extends CommonReq
{
    @NotBlank(message = "pubKey is required")
    String pubKey;

    @AssertTrue(message = "pubKey is invalid")
    public boolean isValidPubKey()
    {
        String SSH_KEY_PATTERN = "^(ssh-rsa|ssh-dss|ecdsa-sha2-nistp256|ecdsa-sha2-nistp384|ecdsa-sha2-nistp521)\\s+([a-zA-Z0-9+/]+={0,3})\\s*(\\S+)?\\s*$";
        Pattern pattern = Pattern.compile(SSH_KEY_PATTERN);
        return pattern.matcher(pubKey).matches();
    }
}
