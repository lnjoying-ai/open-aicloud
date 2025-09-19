package com.lnjoying.justice.omc.domain.dto.rsp;

import com.lnjoying.justice.schema.service.ums.UmsService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/4 15:23
 */

@Data
@Slf4j
public class BaseRsp
{
    private String userName;

    private String bpName;


    public static BaseRsp assembleUserInfo(String bpId, String userId, BaseRsp baseRsp, UmsService umsService)
    {
        if (Objects.isNull(baseRsp))
        {
            return baseRsp;
        }
        String bpName = "";
        if (StringUtils.isNotBlank(bpId))
        {

            {
                try
                {
                    bpName = umsService.getBpNameByBpId(bpId);
                }
                catch (Exception e)
                {
                    log.error("get bp name failed");
                }
            }
        }
        baseRsp.setBpName(bpName);

        String userName = "";
        if (StringUtils.isNotBlank(userId))
        {
            try
            {
                userName = umsService.getUserNameByUserId(userId);
            }
            catch (Exception e)
            {
                log.error("get user name failed");
            }
        }
        baseRsp.setUserName(userName);

        return baseRsp;
    }
}
