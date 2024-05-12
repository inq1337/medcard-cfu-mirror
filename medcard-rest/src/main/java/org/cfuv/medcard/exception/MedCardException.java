package org.cfuv.medcard.exception;

import lombok.Getter;

@Getter
public class MedCardException extends RuntimeException {

    private final ErrorCode errorCode;

    public MedCardException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public MedCardException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public MedCardException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public MedCardException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    protected MedCardException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }

}
