package org.cfuv.medcard.dto;

import org.springframework.web.multipart.MultipartFile;

public record ImageFileDTO(MultipartFile image) {
}
