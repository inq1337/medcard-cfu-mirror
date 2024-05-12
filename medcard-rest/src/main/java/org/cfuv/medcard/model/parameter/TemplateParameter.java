package org.cfuv.medcard.model.parameter;

import jakarta.validation.constraints.NotNull;

public record TemplateParameter(@NotNull String name,
                                @NotNull String unit,
                                @NotNull Boolean hasState) {
}
