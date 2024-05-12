package org.cfuv.medcard.exception;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class ApiError implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private ErrorCode code;
    private String description;
    private List<ApiFieldError> fieldErrors;

    public ApiError(ErrorCode code) {
        this(code, null);
    }

    public ApiError(ErrorCode code, String description) {
        this.code = code;
        this.description = description;
    }

    public void add(String objectName, String field, String message) {
        if (fieldErrors == null) {
            fieldErrors = new ArrayList<>();
        }
        fieldErrors.add(new ApiFieldError(objectName, field, message));
    }

}
