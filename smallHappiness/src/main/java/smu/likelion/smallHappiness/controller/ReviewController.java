package smu.likelion.smallHappiness.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import smu.likelion.smallHappiness.common.error.BusinessException;
import smu.likelion.smallHappiness.common.error.ErrorCode;
import smu.likelion.smallHappiness.review.dto.ReviewSaveRequestDTO;
import smu.likelion.smallHappiness.review.service.ReviewService;
import smu.likelion.smallHappiness.store.dto.VisitStoreDTO;

@Controller
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/list/{storeId}")
    public ResponseEntity<?> getReviewList(@PathVariable Long storeId, Pageable pageable) {
        return ResponseEntity.ok().body(reviewService.getReviewList(storeId, pageable));
    }

//    @GetMapping("/{customerId}")
//    public ResponseEntity<?> getCustomerReviewList(@PathVariable Long customerId){
//        return ResponseEntity.ok().body(reviewService.getCustomerReviewList(customerId));
//    }

    @PostMapping("/{storeId}")
    public ResponseEntity<?> saveReview(@PathVariable Long storeId, @RequestBody ReviewSaveRequestDTO reviewSaveRequestDTO, HttpServletRequest request) {
        reviewService.saveReview(storeId, reviewSaveRequestDTO, getAccessToken(request));
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Object> deleteReview(@PathVariable Long reviewId, HttpServletRequest request) {
        try{
            int flag = reviewService.deleteReview(reviewId, getAccessToken(request));
            if (flag == 200) {
                return ResponseEntity.ok().body(null);
            } else {
                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }catch(BusinessException e){
            ErrorCode errorCode = e.getErrorCode();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorCode);
        }
    }

    @PostMapping("/visitedStore/{storeId}")
    public ResponseEntity<Object> addVisitedStore(@PathVariable Long storeId, @RequestBody VisitStoreDTO visitStoreDTO, HttpServletRequest request) {
        reviewService.addVisitedStore(storeId, visitStoreDTO, getAccessToken(request));
        return ResponseEntity.ok().build();
    }

    private String getAccessToken(HttpServletRequest request) {
        return request.getHeader("Authorization").replace("Bearer", "").trim();
    }
}
