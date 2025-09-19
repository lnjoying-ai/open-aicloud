package com.lnjoying.justice.ims.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * ims preDownload
 *
 * @author merak
 **/

@Data
@AllArgsConstructor
@Builder
public class ImsPreDownload
{
    private String nodeId;

    private String repos;

    private String registryUrl;

    private String registryUserName;

    /**
     * Use base 64 encoding
     */
    private String registryPassword;

    /**
     * Used to update the record status
     */
    private String id;
}
