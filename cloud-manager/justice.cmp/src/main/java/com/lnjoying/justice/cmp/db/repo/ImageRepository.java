package com.lnjoying.justice.cmp.db.repo;

import com.lnjoying.justice.cmp.db.mapper.TblCmpImageMapper;
import com.lnjoying.justice.cmp.db.mapper.TblCmpShareMapper;
import com.lnjoying.justice.cmp.db.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
@Transactional(rollbackFor = {Exception.class})
public class ImageRepository
{

	@Autowired
	private TblCmpImageMapper tblCmpImageMapper;

	@Autowired
	private TblCmpShareMapper tblCmpShareMapper;

	public List<TblCmpImage> getImages(TblCmpImageExample example){
		return tblCmpImageMapper.selectByExample(example);
	}

	public TblCmpImage getImageById(String cloudId, String imageId){
		return tblCmpImageMapper.selectByPrimaryKey(new TblCmpImageKey(cloudId, imageId));
	}

	public  long countImageByExample(TblCmpImageExample example){
		return tblCmpImageMapper.countByExample(example);
	}

	public int createImage(TblCmpImage image){
		return  tblCmpImageMapper.insertSelective(image);
	}

	public int updateImage(TblCmpImage image){
		return tblCmpImageMapper.updateByPrimaryKeySelective(image);
	}

	public Set<String> getImageIds(String cloudId)
	{
		return tblCmpImageMapper.getImageIds(cloudId);
	}

	public List<TblCmpShare> getShares(TblCmpShareExample example){
		return tblCmpShareMapper.selectByExample(example);
	}

	public TblCmpShare getShareById(String cloudId, String shareId){
		return tblCmpShareMapper.selectByPrimaryKey(new TblCmpShareKey(cloudId, shareId));
	}

	public  long countShareByExample(TblCmpShareExample example){
		return tblCmpShareMapper.countByExample(example);
	}

	public int createShare(TblCmpShare share){
		return  tblCmpShareMapper.insertSelective(share);
	}

	public int updateShare(TblCmpShare share){
		return tblCmpShareMapper.updateByPrimaryKeySelective(share);
	}

	public Set<String> getShareIds(String cloudId)
	{
		return tblCmpShareMapper.getShareIds(cloudId);
	}
}
