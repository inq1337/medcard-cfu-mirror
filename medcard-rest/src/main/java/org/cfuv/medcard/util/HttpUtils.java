package org.cfuv.medcard.util;

import jakarta.servlet.http.HttpServletResponse;
import org.cfuv.medcard.exception.ApiError;

import java.io.IOException;

public class HttpUtils {

    public static void sendUnauthorizedApiError(HttpServletResponse response, ApiError error) throws IOException {
        sendApiError(response, error, HttpServletResponse.SC_UNAUTHORIZED);
    }

    public static void sendApiError(HttpServletResponse response, ApiError error, int statusCode) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        JsonUtils.OBJECT_MAPPER.writeValue(response.getWriter(), error);
    }

}
