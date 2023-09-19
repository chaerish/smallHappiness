package smu.likelion.smallHappiness.store.entity;

import jakarta.persistence.*;
import lombok.*;
import smu.likelion.smallHappiness.common.entity.BaseEntity;
import smu.likelion.smallHappiness.coupon.dto.CouponReturnDTO;
import smu.likelion.smallHappiness.coupon.entity.Coupon;
import smu.likelion.smallHappiness.menu.dto.MenuReturnDTO;
import smu.likelion.smallHappiness.review.dto.ReviewResponseDTO;
import smu.likelion.smallHappiness.store.dto.StoreResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="store")
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "clientId")
    private String clientId;

    @Column(name = "storeIntro")
    private String storeIntro;

    @Column(name = "storeImg")
    private String storeImg;

    @Column(name = "certificationNum")
    private int certificationNum;
    @Column(name = "storeTime")
    private String storeTime;



    public StoreResponseDTO toStoreResponseDTO(Store store, String storeName, String storeAddr, String regNum, String storeTime, List<CouponReturnDTO> coupons, List<MenuReturnDTO> menus, List<ReviewResponseDTO> reviews){
        return new StoreResponseDTO(
                store.getId(),
                storeName,
                storeAddr,
                regNum,
                storeTime,
                store.getStoreIntro(),
                store.getStoreImg(),
                store.getCertificationNum(),
                coupons,
                menus,
                reviews
        );
    }
}
