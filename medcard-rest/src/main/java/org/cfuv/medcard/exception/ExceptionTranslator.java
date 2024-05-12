package org.cfuv.medcard.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Slf4j
@ControllerAdvice
public class ExceptionTranslator {

    // Security exceptions

    @ExceptionHandler(LockedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ApiError processLockedException(LockedException ex) {
        return new ApiError(ErrorCode.ACCOUNT_LOCKED, ex.getMessage());
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError processInternalAuthenticationServiceException(InternalAuthenticationServiceException ex) {
        return new ApiError(ErrorCode.AUTHENTICATION_ERROR, ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError processBadCredentialsException(BadCredentialsException ex) {
        return new ApiError(ErrorCode.AUTHENTICATION_ERROR, ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ApiError processAuthenticationError(AuthenticationException ex) {
        return new ApiError(ErrorCode.ACCESS_DENIED, ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ApiError processAccessDeniedException(AccessDeniedException ex) {
        return new ApiError(ErrorCode.ACCESS_DENIED, ex.getMessage());
    }

    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError processJwtException(JwtException ex) {
        return new ApiError(ErrorCode.INVALID_TOKEN, ex.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ApiError processExpiredJwtException(ExpiredJwtException ex) {
        return new ApiError(ErrorCode.TOKEN_EXPIRED, ex.getMessage());
    }

    // MedCard exceptions

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError processObjectNotFoundException(ObjectNotFoundException ex) {
        return new ApiError(ErrorCode.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(IncompatibleParametersException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError IncompatibleParametersException(IncompatibleParametersException ex) {
        return new ApiError(ErrorCode.ILLEGAL_ARGUMENT, ex.getMessage());
    }

    // Java exceptions

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError processIllegalArgumentException(IllegalArgumentException ex) {
        log.error(ex.getMessage(), ex);
        return new ApiError(ErrorCode.VALIDATION_FAILED, ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError processIllegalStateException(IllegalStateException ex) {
        return new ApiError(ErrorCode.ILLEGAL_STATE, ex.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError processValidationError(ValidationException ex) {
        return new ApiError(ErrorCode.VALIDATION_FAILED, ex.getMessage());
    }


    // Spring exceptions

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError processMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return processFieldErrors(fieldErrors);
    }


    // Exception

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> processException(Exception ex, @AuthenticationPrincipal UserDetails user) {
        log.error(ex.getMessage(), ex);

        ResponseEntity.BodyBuilder builder;
        ApiError apiError;
        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
        if (responseStatus != null) {
            builder = ResponseEntity.status(responseStatus.value());
            apiError = new ApiError(ErrorCode.INTERNAL_SERVER_ERROR, responseStatus.reason());
        } else {
            builder = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
            apiError = new ApiError(ErrorCode.INTERNAL_SERVER_ERROR, "Internal server error");
        }


        return builder.body(apiError);
    }

    private ApiError processFieldErrors(List<FieldError> fieldErrors) {
        ApiError dto = new ApiError(ErrorCode.VALIDATION_FAILED);
        for (FieldError fieldError : fieldErrors) {
            dto.add(fieldError.getObjectName(), fieldError.getField(), fieldError.getCode());
        }
        return dto;
    }

}
