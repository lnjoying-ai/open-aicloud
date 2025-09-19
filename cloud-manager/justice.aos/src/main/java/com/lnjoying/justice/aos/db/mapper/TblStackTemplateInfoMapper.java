package com.lnjoying.justice.aos.db.mapper;

import com.lnjoying.justice.aos.db.model.TblStackTemplateInfo;
import com.lnjoying.justice.aos.db.model.TblStackTemplateInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblStackTemplateInfoMapper {
    long countByExample(TblStackTemplateInfoExample example);

    int deleteByExample(TblStackTemplateInfoExample example);

    int deleteByPrimaryKey(String templateId);

    int insert(TblStackTemplateInfo record);

    int insertSelective(TblStackTemplateInfo record);

    List<TblStackTemplateInfo> selectByExample(TblStackTemplateInfoExample example);

    TblStackTemplateInfo selectByPrimaryKey(String templateId);

    int updateByExampleSelective(@Param("record") TblStackTemplateInfo record, @Param("example") TblStackTemplateInfoExample example);

    int updateByExample(@Param("record") TblStackTemplateInfo record, @Param("example") TblStackTemplateInfoExample example);

    int updateByPrimaryKeySelective(TblStackTemplateInfo record);

    int updateByPrimaryKey(TblStackTemplateInfo record);

    List<TblStackTemplateInfo> selectByRootId(@Param("rootId")String rootId);

    TblStackTemplateInfo selectOneByTemplateName(@Param("name")String name);


}