package org.cfuv.medcard.service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.cfuv.medcard.api.service.ShareService;
import org.cfuv.medcard.dto.ShareDTO;
import org.cfuv.medcard.service.security.JwtService;
import org.cfuv.medcard.util.JsonUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ShareServiceImpl implements ShareService {

    public static final String IDS = "ids";
    public static final String ANALYSIS_DATES_SINCE = "analysisDatesSince";
    public static final String SPLITTER = "__!__splitter__!__";
    private final JwtService jwtService;

    @Override
    public String createToken(String userEmail, ShareDTO shareDTO) {
        userEmail += SPLITTER;
        String idsJson = JsonUtils.writeValue(shareDTO.ids());
        String analysisDatesSinceJson;
        if (Objects.nonNull(shareDTO.analysisDatesSince())) {
            analysisDatesSinceJson = JsonUtils.writeValue(shareDTO.analysisDatesSince().toString());
        } else {
            analysisDatesSinceJson = JsonUtils.writeValue(LocalDate.of(1970, 1, 2).toString());
        }
        return jwtService.generateSharedToken(userEmail, ImmutableMap.of(
                IDS, idsJson,
                ANALYSIS_DATES_SINCE, analysisDatesSinceJson)
        );
    }

    @Override
    public ShareDTO getFilterFromToken(String token) {
        Map<String, String> claimsValues = jwtService.getClaimsValues(token, ImmutableList.of(IDS, ANALYSIS_DATES_SINCE));
        List<Long> ids = JsonUtils.readCollectionFromString(claimsValues.get(IDS), Long.class);
        String analysisDatesSince = JsonUtils.readFromString(claimsValues.get(ANALYSIS_DATES_SINCE), String.class);
        return new ShareDTO(ids, LocalDate.parse(analysisDatesSince));
    }

    @Override
    public String getEmailFromToken(String token) {
        return jwtService.extractUsername(token).split(SPLITTER)[0];
    }

}
