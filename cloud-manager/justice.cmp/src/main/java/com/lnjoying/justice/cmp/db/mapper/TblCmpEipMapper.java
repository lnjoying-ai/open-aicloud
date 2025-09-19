package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpEip;
import com.lnjoying.justice.cmp.db.model.TblCmpEipExample;
import com.lnjoying.justice.cmp.db.model.TblCmpEipKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.NS_EIP)
public interface TblCmpEipMapper {
    long countByExample(TblCmpEipExample example);

    int deleteByExample(TblCmpEipExample example);

    int deleteByPrimaryKey(TblCmpEipKey key);

    int insert(TblCmpEip record);

    int insertSelective(TblCmpEip record);

    List<TblCmpEip> selectByExample(TblCmpEipExample example);

    TblCmpEip selectByPrimaryKey(TblCmpEipKey key);

    int updateByExampleSelective(@Param("record") TblCmpEip record, @Param("example") TblCmpEipExample example);

    int updateByExample(@Param("record") TblCmpEip record, @Param("example") TblCmpEipExample example);

    int updateByPrimaryKeySelective(TblCmpEip record);

    int updateByPrimaryKey(TblCmpEip record);

    List<Integer> selectEipIdProtocolPort(@Param("cloudId") String cloudId,
                                          @Param("userId") String userId,
                                          @Param("eipId") String eipId,
                                          @Param("protocol") short protocol);

    Set<String> getEipIds(@Param("cloudId")String cloudId);
}