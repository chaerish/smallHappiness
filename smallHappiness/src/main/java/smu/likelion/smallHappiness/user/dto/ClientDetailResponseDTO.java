package smu.likelion.smallHappiness.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import smu.likelion.smallHappiness.coupon.dto.CouponReturnDTO;
import smu.likelion.smallHappiness.menu.dto.MenuReturnDTO;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientDetailResponseDTO {
    private String userId;
    private String name;
    private String storeName;
    private String storeAddr;
    private String regNum;
    private List<MenuReturnDTO> menus;
    private List<CouponReturnDTO> coupons;
}
