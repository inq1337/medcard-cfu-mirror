package org.cfuv.medcard.exception;

import java.util.function.Supplier;

public class ObjectNotFoundException extends MedCardException {

    public ObjectNotFoundException(String entityName, Object identity) {
        super(entityName + "[" + identity + "] not found", ErrorCode.NOT_FOUND);
    }

    public static Supplier<ObjectNotFoundException> supply(Class<?> clazz, Object identity) {
        return () -> build(clazz, identity);
    }

    public static ObjectNotFoundException build(Class<?> clazz, Object identity) {
        return new ObjectNotFoundException(clazz.getSimpleName(), identity);
    }
}
