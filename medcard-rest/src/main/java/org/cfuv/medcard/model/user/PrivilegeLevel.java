package org.cfuv.medcard.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PrivilegeLevel {
    @JsonProperty("basic")
    BASIC,

    @JsonProperty("premium")
    PREMIUM
}
