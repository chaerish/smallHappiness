package smu.likelion.smallHappiness.store.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreSaveRequestDTO {
    private String storeIntro;
    private String storeImg;
    private int certificationNum;
    private String storeTime;
}
