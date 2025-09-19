package com.lnjoying.justice.schema.common;

public enum ErrorLevel
{
    INFO('I'), WARNING('W'), ERROR('E'), CRITICAL('C');

    /** Type indicator */
    private char type;

    private ErrorLevel(char type) {
        this.type = type;
    }

    /**
     * Get the area type.
     *
     * @return
     */
    public char getType() {
        return type;
    }

    /**
     * Get ErrorLevel from type indicator.
     *
     * @param type
     * @return
     */
    public static ErrorLevel fromType(char type) {
        for (ErrorLevel level : ErrorLevel.values()) {
            if (level.getType() == type) {
                return level;
            }
        }
        throw new RuntimeException("Unknown ErrorLevel type: " + type);
    }
}
