package com.lnjoying.justice.ecrm.config.model;

import lombok.Data;

import java.util.List;

@Data
public class AdminDivisionBean extends AdminDivisionInfo
{
	List<AdminDivisionBean> division;
}
