package smu.likelion.smallHappiness.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSignUpRequestDTO {
    private String userId;
    private String userPw;
    private String email;
    private String image ="";
}
