package com.lnjoying.justice.cmp.service.nextstack.repo;

import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.common.constant.StoragePoolType;
import com.lnjoying.justice.cmp.db.model.TblCmpStoragePool;
import com.lnjoying.justice.cmp.db.model.TblCmpStoragePoolExample;
import com.lnjoying.justice.cmp.db.repo.RepoRepository;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.StoragePoolDetailInfoRsp;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.StoragePoolsRsp;
import com.lnjoying.justice.cmp.entity.search.nextstack.repo.StoragePoolSearchCritical;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;

@Service("poolServiceBiz")
public class StoragePoolServiceBiz
{
    @Autowired
    private RepoRepository repoRepository;

    @Autowired
    private CloudService cloudService;

    public StoragePoolsRsp getStoragePools(String cloudId, StoragePoolSearchCritical storagePoolSearchCritical, String userId) throws WebSystemException
    {
        TblCmpStoragePoolExample example = new TblCmpStoragePoolExample();
        TblCmpStoragePoolExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        if (!StringUtils.isBlank(storagePoolSearchCritical.getPoolName()))
        {
            criteria.andNameLike("%" + storagePoolSearchCritical.getPoolName() + "%");
        }

        if (null == storagePoolSearchCritical.getPoolType() || StoragePoolType.POOL_FS_TYPE == storagePoolSearchCritical.getPoolType())
        {
            criteria.andTypeEqualTo(StoragePoolType.POOL_FS_TYPE);
        }
        else
        {
            criteria.andTypeEqualTo(StoragePoolType.POOL_BLOCK_TYPE);
        }

        StoragePoolsRsp getStoragePoolsRsp = new StoragePoolsRsp();

        long totalNum = repoRepository.countStoragePoolsByExample(example);

        getStoragePoolsRsp.setTotalNum(totalNum);
        if (totalNum < 1) {
            return getStoragePoolsRsp;
        }

        int begin = ((storagePoolSearchCritical.getPageNum() - 1) * storagePoolSearchCritical.getPageSize());
        example.setOrderByClause("create_time desc, storage_pool_id desc");

        example.setStartRow(begin);
        example.setPageSize(storagePoolSearchCritical.getPageSize());

        List<TblCmpStoragePool> storagePools = repoRepository.getStoragePools(example);
        if (null == storagePools) {
            return getStoragePoolsRsp;
        }

        List<String> storagePoolIds = new ArrayList<>();

        List<StoragePoolDetailInfoRsp> storagePoolInfos = storagePools.stream().map(tblRsStoragePool -> {
            StoragePoolDetailInfoRsp storagePoolInfo = new StoragePoolDetailInfoRsp();
            storagePoolInfo.setStoragePoolDetailInfoRsp(tblRsStoragePool);
            storagePoolIds.add(tblRsStoragePool.getStoragePoolId());
            return storagePoolInfo;
        }).collect(Collectors.toList());

        getStoragePoolsRsp.setStoragePools(storagePoolInfos);

        storagePoolInfos.forEach(storagePool -> cloudService.syncSingleData(cloudId, storagePool.getPoolId(), SyncMsg.NS_STORAGE_POOL));

        return getStoragePoolsRsp;
    }

    public StoragePoolDetailInfoRsp getStoragePool(String cloudId, String storagePoolId) throws WebSystemException
    {
        TblCmpStoragePool storagePool = repoRepository.getStoragePoolById(cloudId, storagePoolId);
        if (null == storagePool || REMOVED == storagePool.getEeStatus())
        {
            throw new WebSystemException(ErrorCode.IMAGE_NOT_EXIST, ErrorLevel.INFO);
        }
        StoragePoolDetailInfoRsp storagePoolDetailInfoRsp = new StoragePoolDetailInfoRsp();
        storagePoolDetailInfoRsp.setStoragePoolDetailInfoRsp(storagePool);

        cloudService.syncSingleData(cloudId, storagePoolId, SyncMsg.NS_STORAGE_POOL);

        return storagePoolDetailInfoRsp;
    }
}
