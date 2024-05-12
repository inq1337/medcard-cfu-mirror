package org.cfuv.medcard.util.filter;

public enum LogicalOperator {
    AND, OR;

    public static LogicalOperator fromString(String text) {
        text = text.replaceAll(",", "");
        for (LogicalOperator b : LogicalOperator.values()) {
            if (b.toString().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
