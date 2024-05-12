package org.cfuv.medcard.api.service;

import org.cfuv.medcard.dto.ImageFileDTO;
import org.cfuv.medcard.model.user.CardUser;

public interface CardUserService {

    CardUser loadByEmail(String email);

    void saveAvatar(String userEmail, ImageFileDTO imageFileDTO);

    byte[] getAvatar(String userEmail);
}
