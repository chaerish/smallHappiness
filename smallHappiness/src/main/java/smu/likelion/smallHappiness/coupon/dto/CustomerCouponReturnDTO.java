package smu.likelion.smallHappiness.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import smu.likelion.smallHappiness.coupon.entity.Coupon;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CustomerCouponReturnDTO {
    private CouponReturnDTO coupon;
    public CustomerCouponReturnDTO(Coupon coupon){

        this.coupon=new CouponReturnDTO(coupon);

    }

}
