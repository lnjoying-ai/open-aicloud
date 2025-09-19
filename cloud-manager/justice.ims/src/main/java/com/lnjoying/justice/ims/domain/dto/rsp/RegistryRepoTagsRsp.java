package com.lnjoying.justice.ims.domain.dto.rsp;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.ims.domain.model.ImsRegistryProject;
import com.lnjoying.justice.ims.domain.model.ImsRegistryRepoTag;
import com.lnjoying.justice.ims.harbor.model.Tag;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * repo tags
 *
 * @author merak
 **/

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ApiModel(value = "RegistryRepoTagsRsp")
public class RegistryRepoTagsRsp
{
    private long totalNum;

    private List<ImsRegistryRepoTag> tags;
}
