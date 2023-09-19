package smu.likelion.smallHappiness.coupon.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customerCoupons")
public class CustomerCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "couponId")
    private Long couponId;

    @Column(name = "customerId")
    private Long customerId;
}
