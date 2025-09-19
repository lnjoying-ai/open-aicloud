package com.lnjoying.justice.cmp.db.model.search;

import com.lnjoying.justice.schema.entity.search.PageSearchCritical;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ImageSearchCritical extends PageSearchCritical {
     private String imageName;
     private Short imageOsType;
     private Short imageOsVendor;
     private Boolean isVm;
     private Boolean isPublic;
     private Boolean isOk;
     private Boolean isGpu;
}
