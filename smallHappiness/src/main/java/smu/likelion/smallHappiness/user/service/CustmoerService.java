package smu.likelion.smallHappiness.user.service;

import smu.likelion.smallHappiness.user.dto.CustomerSignUpRequestDTO;

public interface CustmoerService {
    Long signUp(CustomerSignUpRequestDTO customerSignUpRequestDTO);
}
