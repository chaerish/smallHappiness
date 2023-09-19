package smu.likelion.smallHappiness.review.service;

import org.springframework.data.domain.Pageable;
import smu.likelion.smallHappiness.common.dto.PageResult;
import smu.likelion.smallHappiness.review.dto.ReviewResponseDTO;
import smu.likelion.smallHappiness.review.dto.ReviewSaveRequestDTO;
import smu.likelion.smallHappiness.store.dto.VisitStoreDTO;

import java.util.List;

public interface ReviewService {
    PageResult<ReviewResponseDTO> getReviewList(Long storeId, Pageable pageable);
    void saveReview(Long storeId, ReviewSaveRequestDTO reviewSaveRequestDTO, String accessToken);
    int deleteReview(Long reviewId, String accessToken);
    void addVisitedStore(Long storeId, VisitStoreDTO visitStoreDTO, String accessToken);
}
