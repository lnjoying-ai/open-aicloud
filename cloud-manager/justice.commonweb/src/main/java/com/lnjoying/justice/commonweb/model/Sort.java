package com.lnjoying.justice.commonweb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.util.Locale;
import java.util.Optional;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/1/8 11:41
 */

public class Sort
{
    public static enum Direction {
        ASC,
        DESC;

        private Direction() {
        }

        public boolean isAscending() {
            return this.equals(ASC);
        }

        public boolean isDescending() {
            return this.equals(DESC);
        }

        public static Direction fromString(String value)
        {
            try
            {
                return valueOf(value.toUpperCase(Locale.US));
            } catch (Exception var2)
            {
                return DESC;
            }
        }

        public static Optional<Direction> fromOptionalString(String value)
        {
            try
            {
                return Optional.of(fromString(value));
            } catch (IllegalArgumentException var2)
            {
                return Optional.empty();
            }
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SortField
    {
        private String fieldName;

        private Direction order;

        public static SortField parseSortField(String sort, Direction defaultDirection) {
           SortField sortField = new SortField();
            if (StringUtils.isNotBlank(sort)) {
                String[] parts = sort.split(":");
                if (parts.length == 2) {
                    sortField.setFieldName(parts[0]);
                    sortField.setOrder(Direction.fromString(parts[1]));
                }
                else
                {
                    sortField.setFieldName(parts[0]);
                    sortField.setOrder(defaultDirection);
                }
            }
            return sortField;
        }
    }

}
