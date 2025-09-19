package com.lnjoying.justice.cmp.service.rpc;

import com.lnjoying.justice.cmp.db.model.TblCmpImage;
import com.lnjoying.justice.cmp.db.repo.ImageRepository;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.RpcImage;
import com.lnjoying.justice.cmp.service.nextstack.repo.ImageServiceBiz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RpcImageServiceImpl
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageServiceBiz.class);

    @Autowired
    private ImageRepository imageRepository;

    public RpcImage getImage(String cloudId, String imageId)
    {
        TblCmpImage image = imageRepository.getImageById(cloudId, imageId);
        if (image == null) {
            LOGGER.info("get rsImage: null, imageId: {}",imageId);
            return null;
        }
        RpcImage rpcImage = new RpcImage();
        rpcImage.setImageId(image.getImageId());
        String imageName = image.getImageName();
        rpcImage.setName(imageName);
        if (imageName.contains("GPU"))
        {
            rpcImage.setName(imageName.split("-GPU-")[0]);
        }
        rpcImage.setFormat(image.getImageFormat() == null? null:image.getImageFormat().intValue());
        rpcImage.setImageOsType(image.getImageOsType() == null? null:image.getImageOsType().intValue());
        rpcImage.setImageOsVendor(image.getImageOsVendor() == null? null:image.getImageOsVendor().intValue());
        rpcImage.setImageOsVersion(image.getImageOsVersion());
        return rpcImage;
    }

    public List<RpcImage> getBatchImages(String cloudId, List<String> imageIdList)
    {
        return imageIdList.stream().map(imageId -> getImage(cloudId, imageId)).collect(Collectors.toList());
    }
}
