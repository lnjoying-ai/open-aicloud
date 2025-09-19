package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpOsImages;
import com.lnjoying.justice.cmp.db.model.TblCmpOsImagesExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsImagesKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.OS_IMAGE)
public interface TblCmpOsImagesMapper {
    long countByExample(TblCmpOsImagesExample example);

    int deleteByExample(TblCmpOsImagesExample example);

    int deleteByPrimaryKey(TblCmpOsImagesKey key);

    int insert(TblCmpOsImages record);

    int insertSelective(TblCmpOsImages record);

    List<TblCmpOsImages> selectByExample(TblCmpOsImagesExample example);

    TblCmpOsImages selectByPrimaryKey(TblCmpOsImagesKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsImages record, @Param("example") TblCmpOsImagesExample example);

    int updateByExample(@Param("record") TblCmpOsImages record, @Param("example") TblCmpOsImagesExample example);

    int updateByPrimaryKeySelective(TblCmpOsImages record);

    int updateByPrimaryKey(TblCmpOsImages record);

    Set<String> getImageIds(@Param("cloudId")String cloudId);
}