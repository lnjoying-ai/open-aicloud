package com.lnjoying.justice.schema.entity.omc;

import com.lnjoying.justice.schema.common.UserMetaDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/8/23 16:32
 */

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BpLastLoginTimeDTO extends UserMetaDTO
{

    private Date lastLoginTime;
}
