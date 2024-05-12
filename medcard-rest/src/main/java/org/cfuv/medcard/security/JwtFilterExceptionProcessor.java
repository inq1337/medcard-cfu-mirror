package org.cfuv.medcard.security;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface JwtFilterExceptionProcessor {

    void processException(HttpServletResponse response, Exception e) throws IOException;

}
