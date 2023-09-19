package smu.likelion.smallHappiness.coupon.entity;

import jakarta.persistence.*;
import lombok.*;
import smu.likelion.smallHappiness.coupon.dto.CouponReturnDTO;
import smu.likelion.smallHappiness.store.entity.Store;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coupons")


public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "content")
    private String content;
    @Column(name = "couponNum")
    private String couponNum;

    @ManyToOne
    @JoinColumn(name="storeId")
    private Store store;

    public CouponReturnDTO toCouponReturnDTO(){
        return new CouponReturnDTO(
                id,
                content,
                couponNum,
                store.getId()
        );
    }

}