package com.lnjoying.justice.cmp.common;


public final class BaremetalInstanceStatus
{
    public final static int BAREMETAL = 1;

    public final static int INSTANCE_INIT = 0;      //instance is initializing and waiting for deploying

    public final static int INSTANCE_GETTING_IMAGE_CONNECTION = 1;  //instance is getting image connection info

    public final static int INSTANCE_GET_IMAGE_CONNECTION_SUCCESS = 2;  //instance is getting image connection info

    public final static int INSTANCE_GET_IMAGE_CONNECTION_FAILED = 3;  //instance is getting image connection info

    public final static int INSTANCE_CREATING_SETTING_TO_DEPLOY_NETWORK = 4;         //instance is setting to depoly network

    public final static int INSTANCE_SET_TO_DEPLOY_NETWORK_SUCCESS = 5;         //instance is set to depoly network successfully

    public final static int INSTANCE_SET_TO_DEPLOY_NETWORK_FAILED = 6;          //instance is set to depoly network failed

    public final static int INSTANCE_DEPLOYING_BY_PXE_AGENT = 7;      //instance is creating by pxe agent

    public final static int INSTANCE_DEPLOYED_BY_PXE_AGENT_SUCCESS = 8;      //instance is created by pxe agent successfully

    public final static int INSTANCE_DEPLOYED_BY_PXE_AGENT_FAILED = 9;   //instance failed to be created by pxe agent

    public final static int INSTANCE_DELETING_FROM_DEPLOY_NETWORK = 10;    //deleting from deploy network

    public final static int INSTANCE_DELETED_FROM_DEPLOY_NETWORK_SUCCESS = 11;    //deleted from deploy network successfully

    public final static int INSTANCE_DELETED_FROM_DEPLOY_NETWORK_FAILED = 12;    //deleted from deploy network failed

    public final static int INSTANCE_SETTING_TO_TENANT_NETWORK = 13;     //instance is setting to tenant network

    public final static int INSTANCE_ARPING_OK = 13;

    public final static int INSTANCE_SET_TO_TENANT_NETWORK_SUCCESS = 14;      //instance is set to tenant network successfully

    public final static int INSTANCE_SET_TO_TENANT_NETWORK_FAILED = 15;          //instance is set to tenant network failed

    public final static int INSTANCE_INJECTING_ISO_TO_CLOUDINIT = 16;       //inject iso to cloud init

    public final static int INSTANCE_INJECT_ISO_TO_CLOUDINIT_SUCCESS = 17;       //inject iso to cloud init successfully

    public final static int INSTANCE_INJECT_ISO_TO_CLOUDINIT_FAILED = 18;       //inject iso to cloud init failed

    public final static int WAIT_INSTANCE_CLOUD_INIT_RESULT = 19;       //wait instance cloud initing

    public final static int INSTANCE_CLOUD_INIT_SUCCESS = 20;       //instance cloud init succefully

    public final static int INSTANCE_CLOUD_INIT_FAILED = 21;       //instance cloud init failed

    public final static int INSTANCE_RPOBE_INSTANCE_BY_ARP_PING = 22;       //probe instance by arp ping

    public final static int INSTANCE_ARP_PING_SUCCESS = 23;       //probe instance by arp ping successfully

    public final static int INSTANCE_ARP_PING_FAILED = 24;       //inject iso to cloud init failed finally

    public final static int INSTANCE_CREATE_FAILED = 100;      //instance is created failed

    public final static int INSTANCE_RUNNING = 101;       //instance is running

    public final static int INSTANCE_POWEROFFING = 102;       //instance is poweroffing

    public final static int INSTANCE_POWEROFF = 103;       //instance is poweroff

    public final static int INSTANCE_POWEROFF_FAILED = 104;       //instance is poweroff failed

    public final static int INSTANCE_POWERONING = 110;       //instance is poweroning

    public final static int INSTANCE_REBOOTING = 120;       //instance is rebooting

    public final static int INSTANCE_REBOOT_POWEROFFING = 121;       //instance is rebooting

    public final static int INSTANCE_REBOOT_POWERONING = 122;       //instance is rebooting

    public final static int INSTANCE_WAITING_REMOVED = 200;      //instance is waiting for delete, instance is removed when it is deleted successfully

    public final static int INSTANCE_REMOVED_FAILED = 201;      //instance is removed failed

    public final static int INSTANCE_REMOVE_DELETING_FROM_TENANT_NETWORK = 202;      //instance is deleting from tenant network

    public final static int INSTANCE_REMOVE_SETTING_TO_DEPLOY_NETWORK = 203;      //instance is setting to deploy network

    public final static int INSTANCE_DESTROYING_BY_PXE_AGENT = 204;      //instance is destroying by pxe agent



    private BaremetalInstanceStatus() {}

}
