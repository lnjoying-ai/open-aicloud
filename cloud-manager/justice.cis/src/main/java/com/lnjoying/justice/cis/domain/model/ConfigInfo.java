package com.lnjoying.justice.cis.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/10/27 19:03
 */

@Data
@Builder
@Slf4j
public class ConfigInfo implements Serializable
{
    private static final long serialVersionUID = -4407632954522883296L;

    public static final String CONFIG_PREFIX = "cfg://";


    private String userId;

    private String group;

    private String dataId;

    public static ConfigInfo parseCfg(String cfg)
    {
        //ownerId/group/dataId

        String[] split = StringUtils.split(cfg, "/", 3);
        log.info("raw data:{}, after parsing:{}", cfg, Arrays.toString(split));
        if (split.length == 3)
        {
            ConfigInfo configInfo = ConfigInfo.builder().userId(split[0]).group(split[1]).dataId(split[2]).build();
            return configInfo;
        }

        return null;
    }

}
