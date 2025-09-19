package com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn;

import com.lnjoying.justice.commonweb.handler.template.actiondescription.AbstractActionDescriptionTemplate;

import java.util.Optional;

/**
 * 功能描述
 *
 * @author:
 * @date:
 */
public class CmpOpenstackActionDescriptionTemplate extends AbstractActionDescriptionTemplate
{
    @Override
    public Optional<String> getDescriptionTemplate()
    {
        return Optional.empty();
    }

    public interface Descriptions
    {
        String DELETE_BACKUP = "删除备份 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String RESTORE_BACKUP = "恢复备份 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String CREATE_BACKUP = "创建备份 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String UPDATE_BACKUP = "编辑备份 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";

        String CREATE_VOLUME = "创建磁盘 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String UPDATE_VOLUME = "编辑磁盘 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_VOLUME = "删除磁盘 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String ACTION_VOLUME = "操作磁盘 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";

        String CREATE_SNAPSHOT = "创建快照 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String CREATE_SNAPSHOT_METADATA = "创建快照信息 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String UPDATE_SNAPSHOT_METADATA = "编辑快照信息 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String UPDATE_SNAPSHOT = "编辑快照 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_SNAPSHOT = "删除快照 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";

        String EXT_ACTION_INSTANCE = "操作云主机 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";

        String CREATE_FLAVOR = "添加规格 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String UPDATE_FLAVOR = "编辑规格 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_FLAVOR = "删除规格 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";

        String CREATE_KEY_PAIR = "创建密钥 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_KEY_PAIR = "删除密钥 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";

        String CREATE_INSTANCE = "创建云主机 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String UPDATE_INSTANCE = "编辑云主机 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_INSTANCE = "删除云主机 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String ACTION_INSTANCE = "操作云主机 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String REMOTE_CONSOLES = "远程操作云主机 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";

        String CREATE_IMAGE = "添加镜像 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_IMAGE = "删除镜像 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";

        String CREATE_FLOATINGIP = "申请浮动IP ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String UPDATE_FLOATINGIP = "编辑浮动IP ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_FLOATINGIP = "释放浮动IP ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String UPDATE_PORTFORWARDING = "编辑浮动IP端口 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_PORTFORWARDING = "删除浮动IP端口 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String CREATE_PORTFORWARDING = "添加浮动IP端口 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";

        String UPDATE_NETWORK = "编辑网络 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_NETWORK = "删除网络 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String CREATE_NETWORK = "添加网络 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";

        String UPDATE_PORT = "编辑端口 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_PORT = "删除端口 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String CREATE_PORT = "添加端口 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";

        String EXT_UPDATE_ROUTER = "操作路由器 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String CREATE_ROUTER = "添加路由器 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String UPDATE_ROUTER = "编辑路由器 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_ROUTER = "删除路由器 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String ADD_ROUTER_INTERFACE = "添加路由器接口 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String REMOVE_ROUTER_INTERFACE = "删除路由器接口 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";

        String CREATE_SECURITYGROUP = "添加安全组 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String UPDATE_SECURITYGROUP = "编辑安全组 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_SECURITYGROUP = "删除安全组 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";

        String CREATE_SECURITYGROUPRULE = "添加安全组规则 ${RESOURCE_INSTANCE_ID}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_SECURITYGROUPRULE = "删除安全组规则 ${RESOURCE_INSTANCE_ID}，资源池 ${RESOURCE_POOL_ID}";

        String CREATE_SUBNET = "创建子网 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String UPDATE_SUBNET = "编辑子网 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_SUBNET = "删除子网 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
    }
}
