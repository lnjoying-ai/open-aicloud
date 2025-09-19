package com.lnjoying.justice.scheduler.common.constant;

public enum LabelSelectorOperator
{
    IN("In"),
    NOT_IN("NotIn"),
    EXISTS("Exists"),
    NOT_EXISTS("NotExists"),
    GT("Gt"),
    LT("Lt"),
    GTE("Gte"),
    LTE("Lte");

    private String value;

    LabelSelectorOperator(String value)
    {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static LabelSelectorOperator fromValue(String value)
    {
        for (LabelSelectorOperator s : LabelSelectorOperator.values())
        {
            if (s.getValue().equals(value))
            {
                return s;
            }
        }
        return null;
    }
}
