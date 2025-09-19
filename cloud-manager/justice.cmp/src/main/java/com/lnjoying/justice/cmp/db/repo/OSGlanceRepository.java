package com.lnjoying.justice.cmp.db.repo;

import com.lnjoying.justice.cmp.db.mapper.TblCmpOsImagesMapper;
import com.lnjoying.justice.cmp.db.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;

@Repository
@Transactional(rollbackFor = {Exception.class})
public class OSGlanceRepository
{
	@Autowired
	private TblCmpOsImagesMapper tblCmpOsImagesMapper;

	public TblCmpOsImages getImagesById(String cloudId, String imageId)
	{
		return tblCmpOsImagesMapper.selectByPrimaryKey(new TblCmpOsImagesKey(cloudId, imageId));
	}

	public int insertImages(TblCmpOsImages tblCmpOsImages)
	{
		return tblCmpOsImagesMapper.insert(tblCmpOsImages);
	}

	public List<TblCmpOsImages> getImagess(TblCmpOsImagesExample example)
	{
		return tblCmpOsImagesMapper.selectByExample(example);
	}

	public long countImagessByExample(TblCmpOsImagesExample example)
	{
		return tblCmpOsImagesMapper.countByExample(example);
	}

	public int updateImages(TblCmpOsImages tblCmpOsImages)
	{
		return tblCmpOsImagesMapper.updateByPrimaryKeySelective(tblCmpOsImages);
	}

	public Set<String> getImageIds(String cloudId)
	{
		return tblCmpOsImagesMapper.getImageIds(cloudId);
	}

	public int deleteImage(String cloudId, String imageId)
	{
		TblCmpOsImages tblCmpOsImages = new TblCmpOsImages();
		tblCmpOsImages.setCloudId(cloudId);
		tblCmpOsImages.setId(imageId);
		tblCmpOsImages.setEeStatus(REMOVED);
		return updateImages(tblCmpOsImages);
	}
}
