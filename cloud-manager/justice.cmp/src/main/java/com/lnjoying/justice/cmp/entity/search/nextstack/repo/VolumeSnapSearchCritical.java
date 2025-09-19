package com.lnjoying.justice.cmp.entity.search.nextstack.repo;

import com.lnjoying.justice.schema.entity.search.PageSearchCritical;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class VolumeSnapSearchCritical extends PageSearchCritical
{
    private String volumeId;

    private String volumeSnapName;
}
