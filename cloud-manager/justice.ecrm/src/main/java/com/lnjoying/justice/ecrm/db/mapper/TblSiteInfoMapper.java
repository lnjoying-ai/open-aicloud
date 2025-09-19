package com.lnjoying.justice.ecrm.db.mapper;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import com.lnjoying.justice.ecrm.db.model.TblSiteInfo;
import com.lnjoying.justice.ecrm.db.model.TblSiteInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

@MapperModel(value="site")
public interface TblSiteInfoMapper {
    long countByExample(TblSiteInfoExample example);

    int deleteByExample(TblSiteInfoExample example);

    int deleteByPrimaryKey(String siteId);

    int insert(TblSiteInfo record);

    int insertSelective(TblSiteInfo record);

    List<TblSiteInfo> selectByExample(TblSiteInfoExample example);

    TblSiteInfo selectByPrimaryKey(String siteId);

    int updateByExampleSelective(@Param("record") TblSiteInfo record, @Param("example") TblSiteInfoExample example);

    int updateByExample(@Param("record") TblSiteInfo record, @Param("example") TblSiteInfoExample example);

    int updateByPrimaryKeySelective(TblSiteInfo record);

    int updateByPrimaryKey(TblSiteInfo record);
}