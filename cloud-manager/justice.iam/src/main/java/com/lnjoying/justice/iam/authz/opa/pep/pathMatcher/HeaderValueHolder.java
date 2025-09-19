package com.lnjoying.justice.iam.authz.opa.pep.pathMatcher;

import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/22 21:09
 */

class HeaderValueHolder {

    private final List<Object> values = new LinkedList<>();


    void setValue(@Nullable Object value) {
        this.values.clear();
        if (value != null) {
            this.values.add(value);
        }
    }

    void addValue(Object value) {
        this.values.add(value);
    }

    void addValues(Collection<?> values) {
        this.values.addAll(values);
    }

    void addValueArray(Object values) {
        CollectionUtils.mergeArrayIntoCollection(values, this.values);
    }

    List<Object> getValues() {
        return Collections.unmodifiableList(this.values);
    }

    List<String> getStringValues() {
        List<String> stringList = new ArrayList<>(this.values.size());
        for (Object value : this.values) {
            stringList.add(value.toString());
        }
        return Collections.unmodifiableList(stringList);
    }

    @Nullable
    Object getValue() {
        return (!this.values.isEmpty() ? this.values.get(0) : null);
    }

    @Nullable
    String getStringValue() {
        return (!this.values.isEmpty() ? String.valueOf(this.values.get(0)) : null);
    }

    @Override
    public String toString() {
        return this.values.toString();
    }

}