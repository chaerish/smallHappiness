package smu.likelion.smallHappiness.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import smu.likelion.smallHappiness.coupon.entity.Coupon;
import smu.likelion.smallHappiness.store.entity.Store;

@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
public class CouponRequestDTO {
    private String content;
    private String couponNum;

    public Coupon toEntity(Store store){
       return Coupon.builder()
               .content(this.content)
               .couponNum(this.couponNum)
               .store(store)
               .build();
    }
}