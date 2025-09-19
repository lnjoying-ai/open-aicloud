package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpNodeImage;
import com.lnjoying.justice.cmp.db.model.TblCmpNodeImageExample;
import com.lnjoying.justice.cmp.db.model.TblCmpNodeImageKey;
import java.util.List;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.NS_NODE_IMAGE)
public interface TblCmpNodeImageMapper {
    long countByExample(TblCmpNodeImageExample example);

    int deleteByExample(TblCmpNodeImageExample example);

    int deleteByPrimaryKey(TblCmpNodeImageKey key);

    int insert(TblCmpNodeImage record);

    int insertSelective(TblCmpNodeImage record);

    List<TblCmpNodeImage> selectByExample(TblCmpNodeImageExample example);

    TblCmpNodeImage selectByPrimaryKey(TblCmpNodeImageKey key);

    int updateByExampleSelective(@Param("record") TblCmpNodeImage record, @Param("example") TblCmpNodeImageExample example);

    int updateByExample(@Param("record") TblCmpNodeImage record, @Param("example") TblCmpNodeImageExample example);

    int updateByPrimaryKeySelective(TblCmpNodeImage record);

    int updateByPrimaryKey(TblCmpNodeImage record);
}