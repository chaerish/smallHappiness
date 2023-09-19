package smu.likelion.smallHappiness.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import smu.likelion.smallHappiness.common.error.BusinessException;
import smu.likelion.smallHappiness.common.error.ErrorCode;
import smu.likelion.smallHappiness.user.dto.CustomerSignUpRequestDTO;
import smu.likelion.smallHappiness.user.entity.Customer;
import smu.likelion.smallHappiness.user.repository.ClientRepository;
import smu.likelion.smallHappiness.user.repository.CustomerRepository;

import java.util.Collections;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustmoerService {

    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;
    private final ClientRepository clientRepository;

    @Override
    @Transactional
    public Long signUp(CustomerSignUpRequestDTO customerSignUpRequestDTO){
        String userId = customerSignUpRequestDTO.getUserId();
        if(customerRepository.existsByUserId(userId) || clientRepository.existsByUserId(userId)){
            throw new BusinessException(ErrorCode.USER_DUPLICATED);
        }

        customerSignUpRequestDTO.setUserPw(passwordEncoder.encode(customerSignUpRequestDTO.getUserPw()));
        Customer customer = Customer.builder()
                .userId(customerSignUpRequestDTO.getUserId())
                .userPw(customerSignUpRequestDTO.getUserPw())
                .email(customerSignUpRequestDTO.getEmail())
                .image(customerSignUpRequestDTO.getImage())
                .roles(Collections.singletonList("USER"))
                .build();
        customerRepository.save(customer);

        return customer.getId();
    }
}
