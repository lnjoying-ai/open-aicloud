package com.lnjoying.justice.ims.db.mapper;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import com.lnjoying.justice.ims.common.ImsResources;
import com.lnjoying.justice.ims.domain.model.ImageDownloadInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import com.lnjoying.justice.ims.db.model.TblImsRegistryPreDownload;

@MapperModel(ImsResources.THIRD_PARTY_REGISTRY_IMAGE_PRE_DOWNLOAD)
public interface TblImsRegistryPreDownloadMapper
{
    /**
     * delete by primary key
     *
     * @param id primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(String id);

    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(TblImsRegistryPreDownload record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblImsRegistryPreDownload record);

    /**
     * select by primary key
     *
     * @param id primary key
     * @return object by primary key
     */
    TblImsRegistryPreDownload selectByPrimaryKey(String id);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblImsRegistryPreDownload record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblImsRegistryPreDownload record);

    /**
     * batch insert
     *
     * @param list
     * @return
     */
    int batchInsert(@Param("list") List<TblImsRegistryPreDownload> list);


    List<ImageDownloadInfo> selectAll(@Param("registryId")String registryId, @Param("likeRepos")String likeRepos, @Param("nodeId")String nodeId, @Param("bpId")String bpId,
                                      @Param("userId")String userId);


    int deleteByIdList(@Param("idList")List<String> idList);


}