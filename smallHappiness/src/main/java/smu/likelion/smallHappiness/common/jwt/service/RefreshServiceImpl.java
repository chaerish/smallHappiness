package smu.likelion.smallHappiness.common.jwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smu.likelion.smallHappiness.common.error.BusinessException;
import smu.likelion.smallHappiness.common.error.ErrorCode;
import smu.likelion.smallHappiness.common.jwt.entity.RefreshToken;
import smu.likelion.smallHappiness.common.jwt.repository.RefreshTokenRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshServiceImpl implements RefreshService{
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public String getRefresh(String refreshToken){
        RefreshToken refresh = refreshTokenRepository.findByRefreshToken(refreshToken);

        if(!isValidate(refresh)){
            refreshTokenRepository.delete(refresh);
            return null;
        }
        return refresh.getUserId();
    }

    private boolean isValidate(RefreshToken refreshToken){
        LocalDateTime expiryDateTime = refreshToken.getCreatedAt().plus(2592000000L, ChronoUnit.MILLIS);
        if(LocalDateTime.now().isAfter(expiryDateTime)){
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public void deleteRefresh(String refreshToken){
        RefreshToken findRefresh = refreshTokenRepository.findByRefreshToken(refreshToken);
        if(findRefresh != null){
            refreshTokenRepository.delete(findRefresh);
        }else{
            throw new BusinessException(ErrorCode.LOGOUT_TOKEN);
        }
    }
}
