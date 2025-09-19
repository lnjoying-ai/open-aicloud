package com.lnjoying.justice.iam.domain.dto.response.bp;

import com.lnjoying.justice.iam.db.model.TblBpInfo;
import com.lnjoying.justice.iam.domain.dto.request.bp.BpContactsInfo;
import com.micro.core.common.Utils;
import lombok.Data;

@Data
public class BpRsp
{
	String id;
	int	status;
	String name;
	String description;
	String website;
	String license_id;
	String master_user;
	String master_user_id;
	BpContactsInfo contact_info;
	String create_time;
	String update_time;
	int user_total;

	public void setBpInfo(TblBpInfo tblBpInfo)
	{
		this.setId(tblBpInfo.getBpId());
		this.setLicense_id(tblBpInfo.getLicenseId());
		this.setMaster_user(tblBpInfo.getMasterUser());
		this.setName(tblBpInfo.getBpName());
		this.setDescription(tblBpInfo.getDescription());
		this.setWebsite(tblBpInfo.getWebsite());
		this.setCreate_time(Utils.formatDate(tblBpInfo.getCreateTime()));
		this.setUpdate_time(Utils.formatDate(tblBpInfo.getUpdateTime()));
		if (tblBpInfo.getStatus() != null) this.setStatus(tblBpInfo.getStatus());
	}
}
