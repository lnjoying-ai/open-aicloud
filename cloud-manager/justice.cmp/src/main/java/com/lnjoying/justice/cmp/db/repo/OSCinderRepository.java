package com.lnjoying.justice.cmp.db.repo;

import com.lnjoying.justice.cmp.db.mapper.*;
import com.lnjoying.justice.cmp.db.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;

@Repository
@Transactional(rollbackFor = {Exception.class})
public class OSCinderRepository
{
	@Autowired
	private TblCmpOsVolumesMapper tblCmpOsVolumesMapper;

	@Autowired
	private TblCmpOsVolumeMetadataMapper tblCmpOsVolumeMetadataMapper;

	@Autowired
	private TblCmpOsSnapshotsMapper tblCmpOsSnapshotsMapper;

	@Autowired
	private TblCmpOsSnapshotMetadataMapper tblCmpOsSnapshotMetadataMapper;

	@Autowired
	private TblCmpOsVolumeTypesMapper tblCmpOsVolumeTypesMapper;

	@Autowired
	private TblCmpOsBackupsMapper tblCmpOsBackupsMapper;

	@Autowired
	private TblCmpOsVolumeAttachmentMapper tblCmpOsVolumeAttachmentMapper;

	public TblCmpOsVolumes getVolumeById(String cloudId, String volumeId)
	{
		return tblCmpOsVolumesMapper.selectByPrimaryKey(new TblCmpOsVolumesKey(cloudId, volumeId));
	}

	public int insertVolume(TblCmpOsVolumes tblCmpOsVolumes)
	{
		return tblCmpOsVolumesMapper.insert(tblCmpOsVolumes);
	}

	public List<TblCmpOsVolumes> getVolumes(TblCmpOsVolumesExample example)
	{
		return tblCmpOsVolumesMapper.selectByExample(example);
	}

	public long countVolumesByExample(TblCmpOsVolumesExample example)
	{
		return tblCmpOsVolumesMapper.countByExample(example);
	}

	public int updateVolume(TblCmpOsVolumes tblCmpOsVolumes)
	{
		return tblCmpOsVolumesMapper.updateByPrimaryKeySelective(tblCmpOsVolumes);
	}

	public int deleteVolume(String cloudId, String volumeId)
	{
		TblCmpOsVolumes tblCmpOsVolume = new TblCmpOsVolumes();
		tblCmpOsVolume.setCloudId(cloudId);
		tblCmpOsVolume.setId(volumeId);
		tblCmpOsVolume.setEeStatus(REMOVED);

		return updateVolume(tblCmpOsVolume);
	}

	public Set<String> getVolumeIds(String cloudId)
	{
		return tblCmpOsVolumesMapper.getVolumeIds(cloudId);
	}

	public TblCmpOsVolumeMetadata getVolumeMetadataById(String cloudId, Integer id)
	{
		return tblCmpOsVolumeMetadataMapper.selectByPrimaryKey(new TblCmpOsVolumeMetadataKey(cloudId, id));
	}

	public int insertVolumeMetadata(TblCmpOsVolumeMetadata tblCmpOsVolumeMetadata)
	{
		return tblCmpOsVolumeMetadataMapper.insert(tblCmpOsVolumeMetadata);
	}

	public List<TblCmpOsVolumeMetadata> getVolumeMetadatas(TblCmpOsVolumeMetadataExample example)
	{
		return tblCmpOsVolumeMetadataMapper.selectByExample(example);
	}

	public long countVolumeMetadatasByExample(TblCmpOsVolumeMetadataExample example)
	{
		return tblCmpOsVolumeMetadataMapper.countByExample(example);
	}

	public int updateVolumeMetadata(TblCmpOsVolumeMetadata tblCmpOsVolumeMetadata)
	{
		return tblCmpOsVolumeMetadataMapper.updateByPrimaryKeySelective(tblCmpOsVolumeMetadata);
	}

	public TblCmpOsSnapshots getSnapshotById(String cloudId, String snapshotid)
	{
		return tblCmpOsSnapshotsMapper.selectByPrimaryKey(new TblCmpOsSnapshotsKey(cloudId, snapshotid));
	}

	public int insertSnapshot(TblCmpOsSnapshots tblCmpOsSnapshots)
	{
		return tblCmpOsSnapshotsMapper.insert(tblCmpOsSnapshots);
	}

	public List<TblCmpOsSnapshots> getSnapshots(TblCmpOsSnapshotsExample example)
	{
		return tblCmpOsSnapshotsMapper.selectByExample(example);
	}

	public long countSnapshotsByExample(TblCmpOsSnapshotsExample example)
	{
		return tblCmpOsSnapshotsMapper.countByExample(example);
	}

	public int updateSnapshot(TblCmpOsSnapshots tblCmpOsSnapshots)
	{
		return tblCmpOsSnapshotsMapper.updateByPrimaryKeySelective(tblCmpOsSnapshots);
	}

	public int deleteSnapshot(String cloudId, String snapshotId)
	{
		TblCmpOsSnapshots tblCmpOsSnapshot = new TblCmpOsSnapshots();
		tblCmpOsSnapshot.setCloudId(cloudId);
		tblCmpOsSnapshot.setId(snapshotId);
		tblCmpOsSnapshot.setEeStatus(REMOVED);

		return updateSnapshot(tblCmpOsSnapshot);
	}

	public Set<String> getSnapshotIds(String cloudId)
	{
		return tblCmpOsSnapshotsMapper.getSnapshotIds(cloudId);
	}

	public TblCmpOsSnapshotMetadata getSnapshotMetadataById(String cloudId, Integer id)
	{
		return tblCmpOsSnapshotMetadataMapper.selectByPrimaryKey(new TblCmpOsSnapshotMetadataKey(cloudId, id));
	}

	public int insertSnapshotMetadata(TblCmpOsSnapshotMetadata tblCmpOsSnapshotMetadata)
	{
		return tblCmpOsSnapshotMetadataMapper.insert(tblCmpOsSnapshotMetadata);
	}

	public List<TblCmpOsSnapshotMetadata> getSnapshotMetadatas(TblCmpOsSnapshotMetadataExample example)
	{
		return tblCmpOsSnapshotMetadataMapper.selectByExample(example);
	}

	public long countSnapshotMetadatasByExample(TblCmpOsSnapshotMetadataExample example)
	{
		return tblCmpOsSnapshotMetadataMapper.countByExample(example);
	}

	public int updateSnapshotMetadata(TblCmpOsSnapshotMetadata tblCmpOsSnapshotMetadata)
	{
		return tblCmpOsSnapshotMetadataMapper.updateByPrimaryKeySelective(tblCmpOsSnapshotMetadata);
	}

	public TblCmpOsVolumeTypes getVolumeTypeById(String cloudId, String volumeTypeId)
	{
		return tblCmpOsVolumeTypesMapper.selectByPrimaryKey(new TblCmpOsVolumeTypesKey(cloudId, volumeTypeId));
	}

	public int insertVolumeType(TblCmpOsVolumeTypes tblCmpOsVolumeTypes)
	{
		return tblCmpOsVolumeTypesMapper.insert(tblCmpOsVolumeTypes);
	}

	public List<TblCmpOsVolumeTypes> getVolumeTypes(TblCmpOsVolumeTypesExample example)
	{
		return tblCmpOsVolumeTypesMapper.selectByExample(example);
	}

	public long countVolumeTypesByExample(TblCmpOsVolumeTypesExample example)
	{
		return tblCmpOsVolumeTypesMapper.countByExample(example);
	}

	public int updateVolumeType(TblCmpOsVolumeTypes tblCmpOsVolumeTypes)
	{
		return tblCmpOsVolumeTypesMapper.updateByPrimaryKeySelective(tblCmpOsVolumeTypes);
	}

	public Set<String> getVolumeTypeIds(String cloudId)
	{
		return tblCmpOsVolumeTypesMapper.getVolumeTypeIds(cloudId);
	}

	public TblCmpOsBackups getBackupById(String cloudId, String backupId)
	{
		return tblCmpOsBackupsMapper.selectByPrimaryKey(new TblCmpOsBackupsKey(cloudId, backupId));
	}

	public int insertBackup(TblCmpOsBackups tblCmpOsBackups)
	{
		return tblCmpOsBackupsMapper.insert(tblCmpOsBackups);
	}

	public List<TblCmpOsBackups> getBackups(TblCmpOsBackupsExample example)
	{
		return tblCmpOsBackupsMapper.selectByExample(example);
	}

	public long countBackupsByExample(TblCmpOsBackupsExample example)
	{
		return tblCmpOsBackupsMapper.countByExample(example);
	}

	public int updateBackup(TblCmpOsBackups tblCmpOsBackups)
	{
		return tblCmpOsBackupsMapper.updateByPrimaryKeySelective(tblCmpOsBackups);
	}

	public int deleteBackup(String cloudId, String backupId)
	{
		TblCmpOsBackups tblCmpOsBackup = new TblCmpOsBackups();
		tblCmpOsBackup.setCloudId(cloudId);
		tblCmpOsBackup.setId(backupId);
		tblCmpOsBackup.setEeStatus(REMOVED);

		return updateBackup(tblCmpOsBackup);
	}

	public Set<String> getBackupIds(String cloudId)
	{
		return tblCmpOsBackupsMapper.getBackupIds(cloudId);
	}

	public int insertVolumeAttachment(TblCmpOsVolumeAttachment tblCmpOsVolumeAttachment)
	{
		return tblCmpOsVolumeAttachmentMapper.insert(tblCmpOsVolumeAttachment);
	}

	public List<TblCmpOsVolumeAttachment> getVolumeAttachments(TblCmpOsVolumeAttachmentExample example)
	{
		return tblCmpOsVolumeAttachmentMapper.selectByExample(example);
	}

	public long countVolumeAttachmentsByExample(TblCmpOsVolumeAttachmentExample example)
	{
		return tblCmpOsVolumeAttachmentMapper.countByExample(example);
	}

	public int updateVolumeAttachment(TblCmpOsVolumeAttachment tblCmpOsVolumeAttachment)
	{
		return tblCmpOsVolumeAttachmentMapper.updateByPrimaryKeySelective(tblCmpOsVolumeAttachment);
	}

	public Set<String> getVolumeAttachmentIds(String cloudId, String volumeId)
	{
		return tblCmpOsVolumeAttachmentMapper.getVolumeAttachmentIds(cloudId, volumeId);
	}

	public Set<String> getServerVolumeAttachmentIds(String cloudId, String serverId)
	{
		return tblCmpOsVolumeAttachmentMapper.getServerVolumeAttachmentIds(cloudId, serverId);
	}
}
