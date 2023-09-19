package smu.likelion.smallHappiness.user.service;

import smu.likelion.smallHappiness.user.dto.ClientSignUpRequestDTO;

import java.io.IOException;

public interface ClientService {
    Long signUp(ClientSignUpRequestDTO clientSignUpRequestDTO) throws IOException;
    boolean getStatus(String regNum) throws IOException;
}
