package smu.likelion.smallHappiness.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smu.likelion.smallHappiness.common.jwt.dto.TokenInfo;
import smu.likelion.smallHappiness.user.dto.ClientSignUpRequestDTO;
import smu.likelion.smallHappiness.user.dto.CustomerSignUpRequestDTO;
import smu.likelion.smallHappiness.user.dto.LogInRequestDTO;
import smu.likelion.smallHappiness.user.service.ClientServiceImpl;
import smu.likelion.smallHappiness.user.service.CustomerServiceImpl;
import smu.likelion.smallHappiness.user.service.UsersServiceImpl;

import java.io.IOException;

@RestController
@RequestMapping("/api/user")
public class UsersController {
    @Autowired
    CustomerServiceImpl customerService;
    @Autowired
    ClientServiceImpl clientService;
    @Autowired
    UsersServiceImpl usersService;

    @PostMapping("/signup/customer")
    public ResponseEntity<Long> customerSignUp(@RequestBody CustomerSignUpRequestDTO customerSignUpRequestDTO){
        return ResponseEntity.ok(customerService.signUp(customerSignUpRequestDTO));
    }

    @PostMapping("/signup/client")
    public ResponseEntity<Long> clientSignUp(@RequestBody ClientSignUpRequestDTO clientSignUpRequestDTO) throws IOException {
        return ResponseEntity.ok(clientService.signUp(clientSignUpRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenInfo> login(@RequestBody LogInRequestDTO logInRequestDTO){
        return ResponseEntity.ok(usersService.logIn(logInRequestDTO));
    }

    @GetMapping("/refresh")
    public ResponseEntity<TokenInfo> refresh(HttpServletRequest request){
        String accessToken = getAccessToken(request);
        String refreshToken = getRefreshToken(request);
        return ResponseEntity.ok(usersService.refresh(accessToken, refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){
        String refreshToken = getRefreshToken(request);
        usersService.logout(refreshToken);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/info")
    public ResponseEntity<?> getInfo(HttpServletRequest request){
        String accessToken = getAccessToken(request);
        String userRoles = usersService.getUserRoles(accessToken);
        if(userRoles.equals("client")){
            return ResponseEntity.ok(usersService.getDetailByClient(accessToken));
        } else {
            return ResponseEntity.ok(usersService.getDetailByCustomer(accessToken));
        }
    }

    public String getAccessToken(HttpServletRequest request){
        return request.getHeader("Authorization").replace("Bearer", "").trim();
    }

    public String getRefreshToken(HttpServletRequest request){
        return request.getHeader("Refresh-Token");
    }
}
