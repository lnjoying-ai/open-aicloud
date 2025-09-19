package com.lnjoying.justice.cmp.db.model.search;

import com.lnjoying.justice.schema.entity.search.PageSearchCritical;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ShareSearchCritical extends PageSearchCritical {
    String imageId;
    String userId;
}
