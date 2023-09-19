package smu.likelion.smallHappiness.coupon.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import smu.likelion.smallHappiness.coupon.entity.Coupon;

@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
public class CouponReturnDTO {
    private Long couponId;
    private String content;
    private String couponNum;
    private Long storeId;

    public CouponReturnDTO(Coupon coupon) {
        couponId = coupon.getId();
        content = coupon.getContent();
        couponNum = coupon.getCouponNum();
        storeId = coupon.getStore().getId();
    }
}