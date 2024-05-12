package org.cfuv.medcard.api.service;

import org.cfuv.medcard.dto.ShareDTO;

public interface ShareService {
    String createToken(String userEmail, ShareDTO shareDTO);

    ShareDTO getFilterFromToken(String hash);

    String getEmailFromToken(String hash);
}
