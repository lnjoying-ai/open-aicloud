package com.lnjoying.justice.iam.domain.dto.request.user;

import lombok.Data;

/**
 * the current user modifies his own information
 *
 * @author merak
 **/

@Data
public class CurrentUserUpdateReq
{
    private int gender = 0;

    private String address;
}
