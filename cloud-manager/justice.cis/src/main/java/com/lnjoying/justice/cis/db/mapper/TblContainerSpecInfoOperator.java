package com.lnjoying.justice.cis.db.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Mapper
public interface TblContainerSpecInfoOperator
{

    @Select("select spec_id from tbl_container_spec_info where status=#{status} limit 100")
    public List<String> getSpecIdsByStatus(@Param("status")Integer status);

}
