package org.cfuv.medcard.dto;

import java.time.LocalDate;
import java.util.List;

public record ShareDTO(List<Long> ids,
                       LocalDate analysisDatesSince) {
}
