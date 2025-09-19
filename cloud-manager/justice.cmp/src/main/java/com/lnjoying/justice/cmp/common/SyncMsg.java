package com.lnjoying.justice.cmp.common;

public interface SyncMsg
{
    String NS_BAREMETAL_DEVICE = "NSBaremetalDevice";
    String NS_BAREMETAL_INSTANCE = "NSBaremetalInstance";
    String NS_PUBKEY = "NSPubkey";

    String NS_FLAVOR = "NSFlavor";
    String NS_IMAGE = "NSImage";
    String NS_STORAGE_POOL = "NSStoragePool";
    String NS_VOLUME = "NSVolume";
    String NS_VOLUME_SNAP = "NSVolumeSnap";

    String NS_HYPERVISOR_NODE = "NSHypervisorNode";
    String NS_PCI_DEVICE = "NSPciDevice";
    String NS_VM_INSTANCE = "NSVmInstance";
    String NS_VM_INSTANCES = "NSVmInstances";
    String NS_SNAP = "NSSnap";
    String NS_INSTANCE_GROUP = "NSInstanceGroup";
    String NS_RESOURCE_STATS = "NSResourceStats";
    String NS_NFS_SERVER = "NSNfsServer";

    String NS_VPC = "NSVpc";
    String NS_SUBNET = "NSSubnet";
    String NS_EIP = "NSEip";
    String NS_PORT_MAP = "NSPortMap";
    String NS_EIP_POOL = "NSEipPool";
    String NS_SECURITY_GROUP = "NSSecurityGroup";


    String OS_FLAVOR = "OSFlavor";
    String OS_INSTANCE = "OSInstance";
    String OS_KEYPAIR = "OSKeypair";
    String OS_VOLUME_ATTACHMENTS = "OSVolumeAttachments";

    String OS_NETWORK = "OSNetwork";
    String OS_PORT = "OSPort";
    String OS_SUBNET = "OSSubnet";
    String OS_ROUTER = "OSRouter";
    String OS_FLOATING_IP = "OSFloatingIp";
    String OS_SECURITY_GROUP = "OSSecurityGroup";
    String OS_SECURITY_GROUP_RULE = "OSSecurityGroupRule";

    String OS_VOLUME = "OSVolume";
    String OS_SNAPSHOT = "OSSnapshot";
    String OS_BACKUP = "OSBackup";

    String OS_IMAGE = "OSImage";


    String ES_FIREWALL = "ESFirewall";
    String ES_FIREWALL_POLICY = "ESFirewallPolicy";
    String ES_FIREWALL_RULE = "ESFirewallRule";
}
