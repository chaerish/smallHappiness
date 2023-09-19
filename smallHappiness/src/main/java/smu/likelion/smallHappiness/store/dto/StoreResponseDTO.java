package smu.likelion.smallHappiness.store.dto;

import lombok.*;
import smu.likelion.smallHappiness.coupon.dto.CouponReturnDTO;
import smu.likelion.smallHappiness.menu.dto.MenuReturnDTO;
import smu.likelion.smallHappiness.review.dto.ReviewResponseDTO;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreResponseDTO {
    private Long id;
    private String storeName;
    private String storeAddr;
    private String regNum;
    private String storeTime;
    private String storeIntro;
    private String storeImg;
    private int certificationNum;
    private List<CouponReturnDTO> coupons;
    private List<MenuReturnDTO> menus;
    private List<ReviewResponseDTO> reviews;
}

