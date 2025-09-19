package com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn;

import com.lnjoying.justice.commonweb.handler.template.actiondescription.AbstractActionDescriptionTemplate;

import java.util.Optional;

/**
 * 功能描述
 *
 * @author:
 * @date:
 */
public class CmpNextStackActionDescriptionTemplate extends AbstractActionDescriptionTemplate
{
    @Override
    public Optional<String> getDescriptionTemplate()
    {
        return Optional.empty();
    }

    public interface Descriptions
    {
        String ADD_BAREMETAL_DEVICE = "添加裸金属设备 ${name} 到资源池 ${RESOURCE_POOL_ID} ";
        String UPDATE_BAREMETAL_DEVICE = "编辑资源池 ${RESOURCE_POOL_ID} 中裸金属设备 ${name}";
        String DELETE_BAREMETAL_DEVICE = "从资源池 ${RESOURCE_POOL_ID} 中删除裸金属设备 ${RESOURCE_INSTANCE_NAME}";

        String ADD_BAREMETAL_INSTANCE = "创建裸金属实例 ${name}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_BAREMETAL_INSTANCE = "删除裸金属实例 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String POWEROFF_BAREMETAL_INSTANCE = "关闭裸金属实例 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String POWERON_BAREMETAL_INSTANCE = "开启裸金属实例 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String REBOOT_BAREMETAL_INSTANCE = "重启裸金属实例 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String RESET_BAREMETAL_INSTANCE = "重设裸金属实例 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String UPDATE_BAREMETAL_INSTANCE = "编辑裸金属实例 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";

        String ADD_PUBKEY = "添加密钥 ${name}，资源池 ${RESOURCE_POOL_ID}";
        String UPLOAD_PUBKEY = "上传密钥 ${name}，资源池 ${RESOURCE_POOL_ID}";
        String UPDATE_PUBKEY = "更新密钥 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_PUBKEY = "删除密钥 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";

        String ADD_EIP = "在EIP POOL ${eipPoolId} 中申请EIP ${startIpAddress} ～ ${endIpAddress}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_EIP = "删除EIP ${RESOURCE_INSTANCE_NAME}(${RESOURCE_INSTANCE_ID})，资源池 ${RESOURCE_POOL_ID}";
        String ALLOCATE_EIP = "分配EIP给用户，资源池 ${RESOURCE_POOL_ID}，VPC ${RESOURCE_INSTANCE_NAME}";
        String APPLY_EIP = "申请EIP，资源池 ${RESOURCE_POOL_ID}，EIP ${RESOURCE_INSTANCE_NAME}(${RESOURCE_INSTANCE_ID})";
        String RELEASE_EIP = "释放EIP，资源池 ${RESOURCE_POOL_ID}，EIP ${RESOURCE_INSTANCE_NAME}(${RESOURCE_INSTANCE_ID})";
        String ATTACH_EIP = "绑定EIP ${RESOURCE_INSTANCE_NAME}(${RESOURCE_INSTANCE_ID}) 到 ${RESOURCE_INSTANCE.sgId}，资源池 ${RESOURCE_POOL_ID}";
        String DETACH_EIP = "解绑EIP ${RESOURCE_INSTANCE_NAME}(${RESOURCE_INSTANCE_ID})，资源池 ${RESOURCE_POOL_ID}";
        String DETACH_EIP_PORT = "解绑EIP，资源池 ${RESOURCE_POOL_ID}，PORT ${RESOURCE_INSTANCE_NAME}(${RESOURCE_INSTANCE_ID})";

        String ADD_EIP_POOL = "创建EIP Pool ${name}，资源池 ${RESOURCE_POOL_ID}";
        String UPDATE_EIP_POOL = "编辑EIP Pool ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_EIP_POOL = "删除EIP Pool ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";

        String ADD_PORT_MAP = "创建NAT网关 ${mapName}，资源池 ${RESOURCE_POOL_ID}";
        String UPDATE_PORT_MAP_NAME = "编辑NAT网关名称 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String UPDATE_PORT_MAP = "编辑NAT网关 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_PORT_MAP = "删除NAT网关 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";

        String ADD_SECURITY_GROUP = "添加安全组 ${name}，资源池 ${RESOURCE_POOL_ID}";
        String UPDATE_SECURITY_GROUP = "编辑安全组 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_SECURITY_GROUP = "删除安全组 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String ADD_SECURITY_GROUP_RULE = "添加安全组规则，资源池 ${RESOURCE_POOL_ID}，安全组 ${RESOURCE_INSTANCE.sgId}";
        String UPDATE_SECURITY_GROUP_RULE = "编辑安全组规则 ${RESOURCE_INSTANCE_ID}，资源池 ${RESOURCE_POOL_ID}，安全组 ${RESOURCE_INSTANCE.sgId}";
        String DELETE_SECURITY_GROUP_RULE = "删除安全组规则 ${RESOURCE_INSTANCE_ID}，资源池 ${RESOURCE_POOL_ID}，安全组 ${RESOURCE_INSTANCE.sgId}";
        String BOUND_SECURITY_GROUP = "绑定安全组规则，资源池 ${RESOURCE_POOL_ID}，安全组 ${RESOURCE_INSTANCE_ID}，虚拟机 <#list vmInstances as vmInstance> ${vmInstance} </#list>";
        String UNBOUND_SECURITY_GROUP = "解绑安全组规则，资源池 ${RESOURCE_POOL_ID}，安全组 ${RESOURCE_INSTANCE_ID}，虚拟机 <#list vmInstances as vmInstance> ${vmInstance} </#list>";

        String ADD_SUBNET = "添加子网 ${name}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_SUBNET = "删除子网 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String UPDATE_SUBNET = "编辑子网 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";

        String ADD_VPC = "添加虚拟私有云 ${name}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_VPC = "删除虚拟私有云 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String UPDATE_VPC = "编辑虚拟私有云 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";

        String ADD_FLAVOR = "添加规格 ${name}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_FLAVOR = "删除规格 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String UPDATE_FLAVOR = "编辑规格 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";

        String ADD_IMAGE = "添加镜像 ${imageName}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_IMAGE = "删除镜像 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String UPDATE_IMAGE = "更新镜像 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String UPDATE_IMAGE_VISIBILITY = "更新镜像 ${RESOURCE_INSTANCE_NAME} 可见状态<#if visibility?? || visibility == true>为可见</#if><#if visibility?? || visibility == false>为不可见</#if>，资源池 ${RESOURCE_POOL_ID}";

        String UPDATE_STORAGE_POOL = "编辑存储池 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";

        String ADD_VOLUME = "创建云盘 ${name}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_VOLUME = "删除云盘 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String ATTACH_VOLUME = "挂载云盘 ${RESOURCE_INSTANCE_NAME} 到虚拟机 ${vmId}，资源池 ${RESOURCE_POOL_ID}";
        String DETACH_VOLUME = "卸载挂载 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String UPDATE_VOLUME = "编辑云盘 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String EXPORT_VOLUME = "导出云盘 ${RESOURCE_INSTANCE_NAME} 为镜像 ${imageName}，资源池 ${RESOURCE_POOL_ID}";

        String ADD_VOLUME_SNAP = "为云盘 ${volumeId} 创建快照 ${name}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_VOLUME_SNAP = "删除云盘快照 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String UPDATE_VOLUME_SNAP = "编辑云盘快照 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String SWITCH_VOLUME_SNAP = "回滚云盘快照 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";

        String ADD_HYPERVISOR_NODE = "添加计算节点 ${name} 到资源池 ${RESOURCE_POOL_ID} ";
        String DELETE_HYPERVISOR_NODE = "从资源池 ${RESOURCE_POOL_ID} 中删除计算节点 ${RESOURCE_INSTANCE_NAME}";
        String UPDATE_HYPERVISOR_NODE = "编辑资源池 ${RESOURCE_POOL_ID} 中计算节点 ${RESOURCE_INSTANCE_NAME}";

        String ADD_INSTANCE_GROUP = "添加虚拟机组 ${name}，资源池 ${RESOURCE_POOL_ID}";
        String UPDATE_INSTANCE_GROUP = "编辑虚拟机组 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_INSTANCE_FROM_GROUP = "从虚拟机组 ${RESOURCE_INSTANCE_NAME} 中删除虚拟机 ${vmInstanceId}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_INSTANCE_GROUP = "删除虚拟机组 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";

        String ATTACH_PCI_DEVICE = "挂载PCI设备 ${RESOURCE_INSTANCE_NAME}(${RESOURCE_INSTANCE_ID}) 到虚拟机 ${vmInstanceId}，资源池 ${RESOURCE_POOL_ID}";
        String DETACH_PCI_DEVICE = "卸载PCI设备 ${RESOURCE_INSTANCE_NAME}(${RESOURCE_INSTANCE_ID})，资源池 ${RESOURCE_POOL_ID}";

        String ADD_VM_INSTANCE = "创建虚拟机 ${name}，资源池 ${RESOURCE_POOL_ID}";
        String ADD_VM_INSTANCE_COUNT = "批量创建虚拟机 ${vmInstanceCreateReq.name}，数量 ${count}，资源池 ${RESOURCE_POOL_ID}";
        String RENEW_VM_INSTANCE = "重建虚拟机 ${name}，资源池 ${RESOURCE_POOL_ID}";
        String BOUND_SECURITY_GROUP_TO_VM_INSTANCE = "绑定安全组到虚拟机 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String UPDATE_BOUND_SECURITY_GROUP = "编辑虚拟机安全组 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String POWEROFF_VM_INSTANCE = "关闭虚拟机 ${RESOURCE_INSTANCE_NAME} <#if detachment?? || detachment == true>(节省模式)</#if>，资源池 ${RESOURCE_POOL_ID}";
        String POWERON_VM_INSTANCE = "打开虚拟机 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String REBOOT_VM_INSTANCE = "重启虚拟机 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String UPDATE_VM_INSTANCE = "编辑虚拟机 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_VM_INSTANCE = "删除虚拟机 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String MIGRATE_VM_INSTANCE = "迁移虚拟机 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String ATTACH_VOLUME_TO_VM_INSTANCE = "挂载云盘到虚拟机 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String RESET_VM_INSTANCE = "重置虚拟机 ${RESOURCE_INSTANCE_NAME} 密码、密钥或主机名，资源池 ${RESOURCE_POOL_ID}";

        String ADD_VM_SNAP = "为虚拟机 ${vmInstanceId} 创建快照 ${name}，资源池 ${RESOURCE_POOL_ID}";
        String SWITCH_VM_SNAP  = "回滚虚拟机快照 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String UPDATE_VM_SNAP = "编辑虚拟机快照 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_VM_SNAP = "删除虚拟机快照 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";

        String ADD_NFS = "创建NFS ${name}，资源池 ${RESOURCE_POOL_ID}";
        String UPDATE_NFS = "编辑NFS ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_NFS = "删除NFS ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
    }
}
