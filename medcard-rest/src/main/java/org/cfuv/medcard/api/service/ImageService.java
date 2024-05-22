package org.cfuv.medcard.api.service;

import org.cfuv.medcard.dto.ImageFileDTO;

public interface ImageService {

    String upload(ImageFileDTO imageFileDTO);

    byte[] get(String fileName);

    void delete(String fileName);
}
