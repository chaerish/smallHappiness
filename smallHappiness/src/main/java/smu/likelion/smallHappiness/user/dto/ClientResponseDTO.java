package smu.likelion.smallHappiness.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponseDTO {
    private String userId;
    private String name;
    private String storeName;
    private String storeAddr;
    private String regNum;
}
