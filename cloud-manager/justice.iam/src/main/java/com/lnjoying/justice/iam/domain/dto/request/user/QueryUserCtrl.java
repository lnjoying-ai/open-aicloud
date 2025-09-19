package com.lnjoying.justice.iam.domain.dto.request.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@Data
public class QueryUserCtrl
{
    private static final String DEFAULT_SORTBY = "createTime";

    private static final String DEFAULT_SORTORDER = "ASC";

    @Min(value = -1)
    private int offset;

    @Min(value = 0)
    @Max(value = 100)
    private int limit;

    @ApiModelProperty(example = "userName")
    @Pattern(regexp = "(?i)userName|(?i)createTime")
    private String sortBy;

    @ApiModelProperty(example = "ASC")
    @Pattern(regexp = "(?i)ASC|(?i)DESC")
    private String sortOrder;

    /**
     * check basic data by trim.
     */
    public void stringTrim() {
        this.sortBy = StringUtils.trimWhitespace(this.sortBy);
        if (StringUtils.isEmpty(this.sortBy)) {
            this.sortBy = DEFAULT_SORTBY;
        }

        this.sortOrder = StringUtils.trimWhitespace(this.sortOrder);
        if (StringUtils.isEmpty(this.sortOrder)) {
            this.sortOrder = DEFAULT_SORTORDER;
        }
    }
}
