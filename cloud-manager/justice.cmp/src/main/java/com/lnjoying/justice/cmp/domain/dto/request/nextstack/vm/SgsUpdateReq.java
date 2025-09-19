package com.lnjoying.justice.cmp.domain.dto.request.nextstack.vm;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class SgsUpdateReq
{
    @NotEmpty
    @Valid
    private List<@NotBlank String> sgIds;
}
