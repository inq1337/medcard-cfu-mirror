package org.cfuv.medcard.exception;

import java.util.function.Supplier;

public class IncompatibleParametersException extends MedCardException {

    public IncompatibleParametersException(String typeName) {
        super("The passed " + typeName + " parameters are incompatible with template", ErrorCode.ILLEGAL_ARGUMENT);
    }

    public static Supplier<IncompatibleParametersException> supply(String typeName) {
        return () -> build(typeName);
    }

    public static IncompatibleParametersException build(String typeName) {
        return new IncompatibleParametersException(typeName);
    }
}
