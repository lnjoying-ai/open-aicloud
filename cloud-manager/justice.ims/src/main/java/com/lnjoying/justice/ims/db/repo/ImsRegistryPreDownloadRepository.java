package com.lnjoying.justice.ims.db.repo;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lnjoying.justice.ims.db.mapper.TblImsRegistryPreDownloadMapper;
import com.lnjoying.justice.ims.db.model.TblImsRegistry;
import com.lnjoying.justice.ims.db.model.TblImsRegistryPreDownload;
import com.lnjoying.justice.ims.domain.dto.rsp.ImageDownloadInfoRsp;
import com.lnjoying.justice.ims.domain.model.ImageDownloadInfo;
import com.lnjoying.justice.ims.exception.ImsWebSystemException;
import com.lnjoying.justice.schema.common.ErrorLevel;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;

import static com.lnjoying.justice.schema.common.ErrorCode.FAILED_TO_UPDATE_PRE_DOWNLOAD;

/**
 * repository
 *
 * @author merak
 **/

@Repository
@Slf4j
public class ImsRegistryPreDownloadRepository
{

    @Autowired
    private TblImsRegistryPreDownloadMapper imsRegistryPreDownloadMapper;

    public void insert(TblImsRegistryPreDownload preDownload)
    {
        imsRegistryPreDownloadMapper.insertSelective(preDownload);
    }

    public void batchInsert(List<TblImsRegistryPreDownload> list)
    {
        imsRegistryPreDownloadMapper.batchInsert(list);
    }

    public int update(TblImsRegistryPreDownload preDownload)
    {
        Assert.hasText(preDownload.getId(), "The primary key cannot be empty when updating");
        int num = imsRegistryPreDownloadMapper.updateByPrimaryKeySelective(preDownload);
        if (num != 1)
        {
            log.error("failed to update pre download record: {}");
            throw new ImsWebSystemException(FAILED_TO_UPDATE_PRE_DOWNLOAD, ErrorLevel.INFO);
        }

        return num;
    }


    public ImageDownloadInfoRsp getPreDownloads(String registryId, String imageName, String nodeId, String queryBpId, String queryUserId, Integer pageNum, Integer pageSize)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<ImageDownloadInfo> tblImsRegistryPreDownloads = imsRegistryPreDownloadMapper.selectAll(registryId, imageName, nodeId, queryBpId, queryUserId);
        PageInfo<ImageDownloadInfo> pageInfo = new PageInfo<>(tblImsRegistryPreDownloads);
        return ImageDownloadInfoRsp.builder().totalNum(pageInfo.getTotal())
                .images(tblImsRegistryPreDownloads).build();
    }


    public int deleteByJobIdList(List<String> jobIdList)
    {
       return 0;
    }


    public int deleteByIdList(List<String> idList)
    {
        return imsRegistryPreDownloadMapper.deleteByIdList(idList);
    }
}
