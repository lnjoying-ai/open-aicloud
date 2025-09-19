package com.lnjoying.justice.iam.config.wx;

import cn.binarywang.wx.miniapp.config.impl.WxMaRedisBetterConfigImpl;
import me.chanjar.weixin.common.redis.WxRedisOps;

/**
 * @Description: 基于lettuce的实现
 * @Author: Merak
 * @Date: 2022/10/20 19:13
 */

public class WxMaLettuceConfigImpl extends WxMaRedisBetterConfigImpl
{

    public WxMaLettuceConfigImpl(WxRedisOps redisOps, String keyPrefix)
    {
        super(redisOps, keyPrefix);
    }

}

