package org.cfuv.medcard.model.parameter;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ParameterState {

    @JsonProperty("elevated")
    ELEVATED,

    @JsonProperty("low")
    LOW
}
