package com.lnjoying.justice.aos.db.mapper;
import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.lnjoying.justice.aos.db.model.TblHelmStackInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@MapperModel(value="helm")
public interface TblHelmStackInfoMapper {
    /**
     * delete by primary key
     * @param stackId primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(String stackId);

    /**
     * insert record to table
     * @param record the record
     * @return insert count
     */
    int insert(TblHelmStackInfo record);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblHelmStackInfo record);

    /**
     * select by primary key
     * @param stackId primary key
     * @return object by primary key
     */
    TblHelmStackInfo selectByPrimaryKey(String stackId);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblHelmStackInfo record);

    /**
     * update record
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblHelmStackInfo record);

    List<TblHelmStackInfo> selectStackByChartName(@Param("chartName")String chartName);

    List<TblHelmStackInfo> selectAllStacks(@Param("releaseName")String releaseName,
                                           @Param("clusterIds") List<String> clusterIds,
                                           @Param("category")String category,
                                           @Param("bpId")String bpId,
                                           @Param("userId")String userId);

    List<TblHelmStackInfo> selectAllByReleaseName(@Param("releaseName")String releaseName);

    Integer countByOwnerBpIdAndOwnerId(@Param("ownerBpId")String ownerBpId,@Param("ownerId")String ownerId);


}