package com.lnjoying.justice.aos.common;

import com.lnjoying.justice.schema.common.RedisCacheField;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/4/14 14:57
 */

public interface RedisCache
{
    //tty-stack:nodes:node-id                               tty stack info on node.
    String TTY_STACK_NODES = "tty-stack:nodes:%s";

    //tty-stack:stacks:stack-id                                node id.
    String TTY_STACK_STACKS = "tty-stack:stacks:%s";

    // tty-stack:all
    String TTY_STACK_ALL = "tty-stack:all";

    // tty-stack:frp-server-addr
    String TTY_STACK_FRP_SERVER_ADDR = "tty-stack:frp-server-addr";

    // tty-stack:proxy_server_url:
    String TTY_STACK_FRP_SERVER_URL = "tty-stack:proxy_server_url";

    // tty-stack:frp-addr
    String TTY_STACK_FRP_ADDR = "tty-stack:frp-addr";

    // tty-stack:to-be-deleted  node set
    String TTY_STACK_TO_BE_DELETED = "tty-stack:to-be-deleted";

    //AOS:KEEPALIVE:SPEC                       keepalive spec id set
    String AOS_KEEPALIVE_SPECIDS = RedisCacheField.AOS + ":" + RedisCacheField.KEEPALIVE + ":" + RedisCacheField.SPEC;
    //AOS:KEEPALIVE:{spec_id}                  keepalive stack id set
    String AOS_KEEPALIVE_SPEC_STACKIDS = RedisCacheField.AOS + ":" + RedisCacheField.KEEPALIVE + ":";

    //AOS:LIFE:EVENT                           aos life mng event set.
    String AOS_SPAWN_LIFE_EVENT_SET = RedisCacheField.AOS + ":" + RedisCacheField.LIFE + ":" + RedisCacheField.EVENT;
    //AOS:LIFE:EVENT:{session_id}              aos life mng event.
    String AOS_SPAWN_LIFE_EVENT = RedisCacheField.AOS + ":" + RedisCacheField.LIFE + ":" + RedisCacheField.EVENT + ":";

    String KUBE_CONFIG_PATH = RedisCacheField.AOS + ":" + "kube-configs:%s";

    String HELM_STACK_TO_BE_DELETED = "helm-stack:to-be-deleted";
}
