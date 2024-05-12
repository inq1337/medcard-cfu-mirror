package org.cfuv.medcard.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletResponse;
import org.cfuv.medcard.exception.ApiError;
import org.cfuv.medcard.exception.ErrorCode;
import org.cfuv.medcard.util.HttpUtils;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtFilterExceptionProcessorImpl implements JwtFilterExceptionProcessor {

    @Override
    public void processException(HttpServletResponse response, Exception e) throws IOException {
        if (e instanceof AuthenticationException) {
            processAuthenticationException(response, e);
        } else if (e instanceof JwtException) {
            processJwtException(response, e);
        } else {
            HttpUtils.sendApiError(response, new ApiError(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage()), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void processAuthenticationException(HttpServletResponse response, Exception e) throws IOException {
        if (e instanceof LockedException) {
            HttpUtils.sendUnauthorizedApiError(response, new ApiError(ErrorCode.ACCOUNT_LOCKED, e.getMessage()));
        } else {
            HttpUtils.sendApiError(response, new ApiError(ErrorCode.AUTHENTICATION_ERROR, e.getMessage()), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void processJwtException(HttpServletResponse response, Exception e) throws IOException {
        if (e instanceof ExpiredJwtException) {
            HttpUtils.sendUnauthorizedApiError(response, new ApiError(ErrorCode.TOKEN_EXPIRED, e.getMessage()));
        } else {
            HttpUtils.sendUnauthorizedApiError(response, new ApiError(ErrorCode.INVALID_TOKEN, e.getMessage()));
        }
    }

}
