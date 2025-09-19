package com.lnjoying.justice.schema.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface CfgStatus
{
	int SUCCESS = 0;
	int NO_PERMISSION = 1;
	int NO_SPACE = 2;
	int HASH_CHECK_FAILED = 3;

	int OTHER_ERROR = 100;

	int SYNCING = 1001;
	int SYNCING_RESTART = 1002;

	List<Integer> statusList = getStatusCodes();

	static List<Integer> getStatusCodes()
	{
		List<Integer> statusCodes = Arrays.stream(CfgStatus.class.getFields()).map(field ->
		{
			try
			{
				return (Integer)field.get(null);
			}
			catch (IllegalAccessException e)
			{
				return null;
			}

		}).filter(Objects::nonNull).collect(Collectors.toList());
		return Collections.unmodifiableList(statusCodes);
	}

	static boolean checkStatus(int status)
	{
		return statusList.contains(status);
	}
}
