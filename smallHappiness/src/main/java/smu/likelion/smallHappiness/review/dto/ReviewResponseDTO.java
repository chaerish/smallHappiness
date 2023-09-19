package smu.likelion.smallHappiness.review.dto;

import lombok.*;
import smu.likelion.smallHappiness.coupon.entity.Coupon;
import smu.likelion.smallHappiness.review.entity.Review;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponseDTO {
    private Long id;
    private String content;
    private int starRating;
    private Long storeId;
    private Long customerId;
    private String customerName;

    public ReviewResponseDTO(Review review) {
        id = review.getId();
        content = review.getContent();
        starRating = review.getStarRating();
        storeId = review.getStore().getId();
        customerId = review.getCustomer().getId();
        customerName=review.getCustomer().getUsername();
    }
}
