package smu.likelion.smallHappiness.user.service;

import smu.likelion.smallHappiness.common.jwt.dto.TokenInfo;
import smu.likelion.smallHappiness.user.dto.*;

public interface UsersService {
    TokenInfo logIn(LogInRequestDTO logInRequestDTO);
    void logout(String refreshToken);
    ClientResponseDTO getInfoByClient(String accessToken);
    CustomerResponseDTO getInfoByCustomer(String accessToken);
    ClientDetailResponseDTO getDetailByClient(String accessToken);
    CustomerDetailResponseDTO getDetailByCustomer(String accessToken);
}
