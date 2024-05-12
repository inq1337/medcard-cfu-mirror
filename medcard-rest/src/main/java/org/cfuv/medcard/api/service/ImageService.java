package org.cfuv.medcard.api.service;

import lombok.SneakyThrows;
import org.cfuv.medcard.dto.ImageFileDTO;

public interface ImageService {

    String upload(ImageFileDTO imageFileDTO);

    @SneakyThrows
    byte[] get(String fileName);

    void delete(String fileName);
}
