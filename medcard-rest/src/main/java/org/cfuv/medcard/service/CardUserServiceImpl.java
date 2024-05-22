package org.cfuv.medcard.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.cfuv.medcard.api.repository.CardUserRepository;
import org.cfuv.medcard.api.service.CardUserService;
import org.cfuv.medcard.api.service.ImageService;
import org.cfuv.medcard.dto.CardUserProfileDTO;
import org.cfuv.medcard.dto.ImageFileDTO;
import org.cfuv.medcard.exception.ObjectNotFoundException;
import org.cfuv.medcard.mapper.CardUserMapper;
import org.cfuv.medcard.model.user.CardUser;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class CardUserServiceImpl implements CardUserService {

    private final CardUserRepository cardUserRepository;
    private final ImageService imageService;

    @Override
    public CardUser loadByEmail(String email) {
        return cardUserRepository.findByEmail(email).orElseThrow(ObjectNotFoundException.supply(CardUser.class, email));
    }

    @Override
    public void saveAvatar(String userEmail, ImageFileDTO imageFileDTO) {
        CardUser cardUser = loadByEmail(userEmail);
        String avatar = cardUser.getAvatar();
        if (avatar != null) {
            imageService.delete(avatar);
        }
        String fileName = imageService.upload(imageFileDTO);
        cardUser.setAvatar(fileName);
        cardUserRepository.save(cardUser);
    }

    @Override
    public byte[] getAvatar(String userEmail) {
        CardUser cardUser = loadByEmail(userEmail);
        return imageService.get(cardUser.getAvatar());
    }

    @Override
    public CardUser updateUserInfo(String userEmail, CardUserProfileDTO profileDTO) {
        CardUser cardUser = CardUserMapper.INSTANCE.partialUpdate(profileDTO, loadByEmail(userEmail));
        return cardUserRepository.save(cardUser);
    }

    @Override
    public void moveToPremium(String userEmail) {
        throw new NotImplementedException("Account privilege levels functionality is not implemented yet");
    }

}
