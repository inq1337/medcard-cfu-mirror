package org.cfuv.medcard.security;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.cfuv.medcard.exception.ApiError;
import org.cfuv.medcard.exception.ErrorCode;
import org.cfuv.medcard.util.HttpUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@Slf4j
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException {
        HttpUtils.sendUnauthorizedApiError(httpServletResponse,
                new ApiError(ErrorCode.ACCESS_DENIED, "You are trying to access a secured REST resource without supplying any credentials. Please authenticate"));
    }

}
