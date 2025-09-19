package com.lnjoying.justice.ecrm.db.mapper;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import com.lnjoying.justice.ecrm.db.model.TblLabelOptionInfo;
import com.lnjoying.justice.ecrm.db.model.TblLabelOptionInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

@MapperModel(value="label")
public interface TblLabelOptionInfoMapper {
    long countByExample(TblLabelOptionInfoExample example);

    int deleteByExample(TblLabelOptionInfoExample example);

    int insert(TblLabelOptionInfo record);

    int insertSelective(TblLabelOptionInfo record);

    List<TblLabelOptionInfo> selectByExample(TblLabelOptionInfoExample example);

    int updateByExampleSelective(@Param("record") TblLabelOptionInfo record, @Param("example") TblLabelOptionInfoExample example);

    int updateByExample(@Param("record") TblLabelOptionInfo record, @Param("example") TblLabelOptionInfoExample example);
}