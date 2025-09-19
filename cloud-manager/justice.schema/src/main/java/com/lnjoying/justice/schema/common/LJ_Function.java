package com.lnjoying.justice.schema.common;

@FunctionalInterface
public interface LJ_Function<T>
{
    public void operator(T t);
}