package com.lnjoying.justice.ecrm.domain.dto.model;

import com.lnjoying.justice.ecrm.db.model.TblEdgeComputeInfo;
import com.lnjoying.justice.ecrm.domain.MemInfo;
import com.micro.core.common.Utils;
import lombok.Data;

import java.util.Map;

@Data
public class EdgeNodeInfo
{
	String                 node_id;
	String		         node_name;
	String                 host_ip;
	String		         host_name;
	DevInfo dev_info;
	String		           site_id;
	String                 site_name;
	String               region_id;
	String               region_name;
	int 		     active_status = 0;
	int              online_status = 0;
	ResourceLimit resource_limit;
	Map<String, String>     labels;
	Map<String, String>     taints;
	String             create_time;
	String		       update_time;
	String user_id;
	String user_name;
	String bp_id;
	String bp_name;
	String core_version;
	int upgrade_status = 0;

	public void setInfo(TblEdgeComputeInfo tblEdgeComputeInfo)
	{
		this.node_id     = tblEdgeComputeInfo.getNodeId();
		this.site_id     = tblEdgeComputeInfo.getSiteId();
		this.node_name   = tblEdgeComputeInfo.getNodeName();
//		this.host_ip     = tblEdgeComputeInfo.getHostIp();
		this.host_name   = tblEdgeComputeInfo.getHostName();
		this.site_id     = tblEdgeComputeInfo.getSiteId();
		this.region_id   = tblEdgeComputeInfo.getRegionId();
		this.create_time = Utils.formatDate(tblEdgeComputeInfo.getCreateTime());
		this.update_time = Utils.formatDate(tblEdgeComputeInfo.getUpdateTime());
		if (tblEdgeComputeInfo.getActiveStatus() != null)this.active_status = tblEdgeComputeInfo.getActiveStatus();
		if (tblEdgeComputeInfo.getOnlineStatus() != null)this.online_status = tblEdgeComputeInfo.getOnlineStatus();

		DevInfo devInfo = new DevInfo();
		CpuInfo cpuInfo = new CpuInfo();
		MemInfo memInfo = new MemInfo();
		MachineInfo machineInfo = new MachineInfo();

		if (tblEdgeComputeInfo.getCpuFrequency() != null) cpuInfo.setCpuFrequency(tblEdgeComputeInfo.getCpuFrequency());
		if (tblEdgeComputeInfo.getCpuLogicNum() != null) cpuInfo.setCpuLogicNum(tblEdgeComputeInfo.getCpuLogicNum());
		if (tblEdgeComputeInfo.getCpuModel() != null) cpuInfo.setCpuModel(tblEdgeComputeInfo.getCpuModel());
		if (tblEdgeComputeInfo.getCpuPhysicalNum() != null) cpuInfo.setCpuPhysicalNum(tblEdgeComputeInfo.getCpuPhysicalNum());
		if (tblEdgeComputeInfo.getCpuNum() != null) cpuInfo.setCpuNum(tblEdgeComputeInfo.getCpuNum());

		if (tblEdgeComputeInfo.getMemTotal() != null) memInfo.setMemTotal(tblEdgeComputeInfo.getMemTotal());
		machineInfo.setProduct(tblEdgeComputeInfo.getProduct());
		machineInfo.setSn(tblEdgeComputeInfo.getSn());
		machineInfo.setUuid(tblEdgeComputeInfo.getUuid());
		machineInfo.setVender(tblEdgeComputeInfo.getVender());

		machineInfo.setOs(tblEdgeComputeInfo.getOs());

		if (tblEdgeComputeInfo.getCoreVersion() != null) this.core_version = tblEdgeComputeInfo.getCoreVersion();

		devInfo.setCpuInfo(cpuInfo);
		devInfo.setMemInfo(memInfo);
		devInfo.setMachineInfo(machineInfo);
		this.dev_info = devInfo;
	}
}
