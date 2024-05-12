package org.cfuv.medcard.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class ApiFieldError implements Serializable {
    private String objectName;
    private String field;
    private String message;
}
