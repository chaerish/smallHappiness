package smu.likelion.smallHappiness.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientSignUpRequestDTO {
    private String userId;
    private String userPw;
    private String name;
    private String storeName;
    private String storeAddr;
    private String regNum;
}
