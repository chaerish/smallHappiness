package smu.likelion.smallHappiness.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomerResponseDTO {
    private String userId;
    private String email;
    private String image;
}
