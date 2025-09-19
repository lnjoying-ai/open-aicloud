package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpImage;
import com.lnjoying.justice.cmp.db.model.TblCmpImageExample;
import com.lnjoying.justice.cmp.db.model.TblCmpImageKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.NS_IMAGE)
public interface TblCmpImageMapper {
    long countByExample(TblCmpImageExample example);

    int deleteByExample(TblCmpImageExample example);

    int deleteByPrimaryKey(TblCmpImageKey key);

    int insert(TblCmpImage record);

    int insertSelective(TblCmpImage record);

    List<TblCmpImage> selectByExample(TblCmpImageExample example);

    TblCmpImage selectByPrimaryKey(TblCmpImageKey key);

    int updateByExampleSelective(@Param("record") TblCmpImage record, @Param("example") TblCmpImageExample example);

    int updateByExample(@Param("record") TblCmpImage record, @Param("example") TblCmpImageExample example);

    int updateByPrimaryKeySelective(TblCmpImage record);

    int updateByPrimaryKey(TblCmpImage record);

    Set<String> getImageIds(@Param("cloudId")String cloudId);
}