package org.cfuv.medcard.util.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FilterOperator {
    EQUALS("="),
    NOT_EQUALS("!="),
    GT(">"),
    GT_OR_EQ(">="),
    LT("<"),
    LT_OR_EQ("<="),
    IN("in"),
    LIKE("contains"),
    START_WITH("startwith"),
    NOT_LIKE("notcontains");

    private final String text;

    public static FilterOperator fromString(String text) {
        for (FilterOperator b : FilterOperator.values()) {
            if (b.text.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
