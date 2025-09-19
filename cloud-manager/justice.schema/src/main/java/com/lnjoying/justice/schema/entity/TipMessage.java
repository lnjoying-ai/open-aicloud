package com.lnjoying.justice.schema.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipMessage
{
    private String level;
    private String message;

    public enum TipMessageLevel {
        INFO("info"),
        WARNING("warning"),
        ERROR("error");

        private final String level;

        TipMessageLevel(String level){
            this.level = level;
        }

        public String getLevel() {
            return level;
        }
    }
}
