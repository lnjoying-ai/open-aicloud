package com.lnjoying.justice.ecrm.domain.dto.model;

import com.lnjoying.justice.ecrm.db.model.TblEdgeGwInfo;
import com.lnjoying.justice.ecrm.domain.MemInfo;
import com.micro.core.common.Utils;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GwNodeInfo
{
	String            node_id;
	String          node_name;
	HostNetInfo      host_net;
	DevInfo          dev_info;
//	Location location;
	int	        active_status;
	int     online_status = 0;
	Map<String,String> labels;
	List<NameIDInfo>  regions;
	String       core_version;
	String        create_time;
	String        update_time;

	public void setInfo(TblEdgeGwInfo tblEdgeGwInfo)
	{
		this.node_id     = tblEdgeGwInfo.getNodeId();
		this.node_name   = tblEdgeGwInfo.getNodeName();
		HostNetInfo hostNetInfo = new HostNetInfo();

		if (tblEdgeGwInfo.getHostInnerIp()    != null)    hostNetInfo.setInner_ip(tblEdgeGwInfo.getHostInnerIp());
		if (tblEdgeGwInfo.getHostInnerPort()  != null)  hostNetInfo.setInner_port(tblEdgeGwInfo.getHostInnerPort());
		if (tblEdgeGwInfo.getHostPublicIp()   != null)   hostNetInfo.setPublic_ip(tblEdgeGwInfo.getHostPublicIp());
		if (tblEdgeGwInfo.getHostPublicPort() != null) hostNetInfo.setPublic_port(tblEdgeGwInfo.getHostPublicPort());
		this.host_net = hostNetInfo;
		this.create_time = Utils.formatDate(tblEdgeGwInfo.getCreateTime());
		this.update_time = Utils.formatDate(tblEdgeGwInfo.getUpdateTime());
		if (tblEdgeGwInfo.getActiveStatus() != null) this.active_status = tblEdgeGwInfo.getActiveStatus();
		if (tblEdgeGwInfo.getOnlineStatus() != null) this.online_status = tblEdgeGwInfo.getOnlineStatus();

		DevInfo devInfo = new DevInfo();
		CpuInfo cpuInfo = new CpuInfo();
		MemInfo memInfo = new MemInfo();
		MachineInfo machineInfo = new MachineInfo();


		if (tblEdgeGwInfo.getCpuFrequency() != null)   cpuInfo.setCpuFrequency(tblEdgeGwInfo.getCpuFrequency());
		if (tblEdgeGwInfo.getCpuLogicNum()  != null)   cpuInfo.setCpuLogicNum(tblEdgeGwInfo.getCpuLogicNum());
		if (tblEdgeGwInfo.getCpuModel()     != null)   cpuInfo.setCpuModel(tblEdgeGwInfo.getCpuModel());
		if (tblEdgeGwInfo.getCpuPhysicalNum() != null) cpuInfo.setCpuPhysicalNum(tblEdgeGwInfo.getCpuPhysicalNum());

		if (tblEdgeGwInfo.getMemTotal() != null)    memInfo.setMemTotal(tblEdgeGwInfo.getMemTotal());
		if (tblEdgeGwInfo.getProduct() != null)     machineInfo.setProduct(tblEdgeGwInfo.getProduct());
		if (tblEdgeGwInfo.getSn() != null)          machineInfo.setSn(tblEdgeGwInfo.getSn());
		if (tblEdgeGwInfo.getUuid() != null)        machineInfo.setUuid(tblEdgeGwInfo.getUuid());
		if (tblEdgeGwInfo.getVender() != null)      machineInfo.setVender(tblEdgeGwInfo.getVender());
		if (tblEdgeGwInfo.getOs() != null)          machineInfo.setOs(tblEdgeGwInfo.getOs());
		if (tblEdgeGwInfo.getCoreVersion() != null) this.core_version = tblEdgeGwInfo.getCoreVersion();

		devInfo.setCpuInfo(cpuInfo);
		devInfo.setMemInfo(memInfo);
		devInfo.setMachineInfo(machineInfo);
		this.dev_info = devInfo;
	}
}
