package smu.likelion.smallHappiness.common.jwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import smu.likelion.smallHappiness.common.error.BusinessException;
import smu.likelion.smallHappiness.common.error.ErrorCode;
import smu.likelion.smallHappiness.user.entity.Client;
import smu.likelion.smallHappiness.user.entity.Customer;
import smu.likelion.smallHappiness.user.repository.ClientRepository;
import smu.likelion.smallHappiness.user.repository.CustomerRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        if(customerRepository.existsByUserId(userId)){
            return customerRepository.findByUserId(userId)
                    .map(this::createCustomerDetails)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        }
        else if(clientRepository.existsByUserId(userId)){
            return clientRepository.findByUserId(userId)
                    .map(this::createClientDetails)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_DUPLICATED));
        }
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createCustomerDetails(Customer customer) {
        return User.builder()
                .username(customer.getUserId())
                .password(customer.getUserPw())
                .roles(customer.getRoles().toArray(new String[0]))
                .build();
    }

    private UserDetails createClientDetails(Client client) {
        return User.builder()
                .username(client.getUserId())
                .password(client.getUserPw())
                .roles(client.getRoles().toArray(new String[0]))
                .build();
    }
}
