package com.lnjoying.justice.commonweb.model;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorLevel;

import java.util.Map;
import java.util.Set;

import static com.lnjoying.justice.schema.common.ErrorCode.INVALID_FIELD;

/**
 * @Description:
 * @Author: Merak Verify whether the field is allowed, mainly to prevent SQL injection.
 * @Date: 2024/8/23 15:05
 */

public class FieldValidator
{
    private final Map<String, Set<String>> allowedFields;

    public FieldValidator(Map<String, Set<String>> allowedFields) {
        this.allowedFields = allowedFields;
    }

    public boolean validateField(String field) {
        if (allowedFields.containsKey(field)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validateValue(String field, String value) {
        Set<String> allowedValues = allowedFields.get(field);
        if (allowedValues != null && allowedValues.contains(value)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validateSortField(String sort) {
        Sort.SortField sortField = Sort.SortField.parseSortField(sort, Sort.Direction.DESC);

        boolean field = validateField(sortField.getFieldName());
        boolean direction = validateValue(sortField.getFieldName(), sortField.getOrder().name());
        return field && direction;
    }

    public boolean validateSortFieldWithException(String sort) {
        boolean valid = validateSortField(sort);
        if (!valid) {
            throw new WebSystemException(INVALID_FIELD, ErrorLevel.ERROR);
        }

        return true;
    }
}
