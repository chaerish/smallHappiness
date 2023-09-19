package smu.likelion.smallHappiness.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import smu.likelion.smallHappiness.coupon.dto.CouponReturnDTO;
import smu.likelion.smallHappiness.review.dto.ReviewResponseDTO;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CustomerDetailResponseDTO {
    private String userId;
    private String email;
    private String image;
    private List<Long> visitedStores;
    private List<CouponReturnDTO> coupons;
    private List<ReviewResponseDTO> reviews;
}
