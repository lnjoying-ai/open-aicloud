package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpPort;
import com.lnjoying.justice.cmp.db.model.TblCmpPortExample;
import com.lnjoying.justice.cmp.db.model.TblCmpPortKey;
import java.util.List;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.NS_PORT)
public interface TblCmpPortMapper {
    long countByExample(TblCmpPortExample example);

    int deleteByExample(TblCmpPortExample example);

    int deleteByPrimaryKey(TblCmpPortKey key);

    int insert(TblCmpPort record);

    int insertSelective(TblCmpPort record);

    List<TblCmpPort> selectByExample(TblCmpPortExample example);

    TblCmpPort selectByPrimaryKey(TblCmpPortKey key);

    int updateByExampleSelective(@Param("record") TblCmpPort record, @Param("example") TblCmpPortExample example);

    int updateByExample(@Param("record") TblCmpPort record, @Param("example") TblCmpPortExample example);

    int updateByPrimaryKeySelective(TblCmpPort record);

    int updateByPrimaryKey(TblCmpPort record);
}