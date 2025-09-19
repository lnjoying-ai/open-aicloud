package com.lnjoying.justice.omc.db.repo;

import com.lnjoying.justice.commonweb.util.SystemUtils;
import com.lnjoying.justice.omc.db.mapper.TblOmcPrometheusMapper;
import com.lnjoying.justice.omc.db.model.TblOmcPrometheus;
import com.micro.core.common.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/1 10:07
 */

@Repository
public class PrometheusInstanceRepository
{

    @Autowired
    private TblOmcPrometheusMapper prometheusMapper;

    public List<TblOmcPrometheus> selectByType(Integer type, String name, String siteId)
    {
       return prometheusMapper.selectAll(null, null, null, name, type, siteId);
    }

    public TblOmcPrometheus selectByPrimaryKey(String id)
    {
        return prometheusMapper.selectByPrimaryKey(id);
    }


    public int insertSelective(TblOmcPrometheus record)
    {
        return prometheusMapper.insertSelective(record);
    }

    public int deleteByPrimaryKey(String id)
    {
        return prometheusMapper.deleteByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(TblOmcPrometheus record)
    {
        return prometheusMapper.updateByPrimaryKeySelective(record);
    }
}
