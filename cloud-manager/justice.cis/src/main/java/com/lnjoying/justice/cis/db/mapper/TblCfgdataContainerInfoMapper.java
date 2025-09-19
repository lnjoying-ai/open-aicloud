package com.lnjoying.justice.cis.db.mapper;
import java.util.Collection;
import com.lnjoying.justice.cis.db.model.TblContainerInstInfo;
import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.lnjoying.justice.cis.db.model.TblCfgdataContainerInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@MapperModel(value="container_cfg")
public interface TblCfgdataContainerInfoMapper {
    /**
     * delete by primary key
     * @param cfgVolumeId primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(String cfgVolumeId);

    /**
     * insert record to table
     * @param record the record
     * @return insert count
     */
    int insert(TblCfgdataContainerInfo record);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblCfgdataContainerInfo record);

    /**
     * select by primary key
     * @param cfgVolumeId primary key
     * @return object by primary key
     */
    TblCfgdataContainerInfo selectByPrimaryKey(String cfgVolumeId);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblCfgdataContainerInfo record);

    /**
     * update record
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblCfgdataContainerInfo record);


    List<TblContainerInstInfo> selectContainersByUserIdAndDatagroupAndDataId(@Param("userId")String userId, @Param("dataGroup")String dataGroup, @Param("dataId")String dataId,
                                                                             @Param("statusExcludeCollection") Collection<Integer> statusExcludeCollection);

    List<TblCfgdataContainerInfo> selectAll(@Param("userId")String userId,@Param("dataGroup")String dataGroup,@Param("dataId")String dataId,@Param("containerIdCollection")Collection<String> containerIdCollection);

    List<TblCfgdataContainerInfo> selectByContainerId(@Param("containerId")String containerId);

    int deleteByContainerId(@Param("containerId")String containerId);


}