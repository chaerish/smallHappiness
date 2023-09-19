package smu.likelion.smallHappiness.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import smu.likelion.smallHappiness.common.dto.PageResult;
import smu.likelion.smallHappiness.common.error.BusinessException;
import smu.likelion.smallHappiness.common.error.ErrorCode;
import smu.likelion.smallHappiness.review.dto.ReviewResponseDTO;
import smu.likelion.smallHappiness.review.dto.ReviewSaveRequestDTO;
import smu.likelion.smallHappiness.review.entity.Review;
import smu.likelion.smallHappiness.review.repository.ReviewRepository;
import smu.likelion.smallHappiness.store.dto.VisitStoreDTO;
import smu.likelion.smallHappiness.store.entity.Store;
import smu.likelion.smallHappiness.store.entity.VisitedStore;
import smu.likelion.smallHappiness.store.repository.StoreRepository;
import smu.likelion.smallHappiness.store.repository.VisitedStoreRepository;
import smu.likelion.smallHappiness.user.entity.Customer;
import smu.likelion.smallHappiness.user.repository.CustomerRepository;
import smu.likelion.smallHappiness.user.service.UsersService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;
    private final CustomerRepository customerRepository;
    private final VisitedStoreRepository visitedStoreRepository;
    private final UsersService usersService;

    @Override
    public PageResult<ReviewResponseDTO> getReviewList(Long storeId, Pageable pageable) { //전체리뷰
        Page<Review> reviewPage = reviewRepository.findAllByStoreId(storeId, pageable);
        PageResult<ReviewResponseDTO> result = PageResult.ok(reviewPage.map(review -> review.toReviewResponseDTO()));
        return result;
    }

    @Override
    public void saveReview(Long storeId, ReviewSaveRequestDTO reviewSaveRequestDTO, String accessToken) {
        String customerId = usersService.getInfoByClient(accessToken).getUserId();

        Store store = storeRepository.findById(storeId).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.STORE_NOT_FOUND);
        });

        Customer customer = customerRepository.findByUserId(customerId).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        });


        if (visitedStoreRepository.findTopByCustomerAndStore(customer, store).isPresent()) {
            if(reviewRepository.findTopReviewsByCustomerAndStore(customer, store).isPresent()) {
                throw new BusinessException(ErrorCode.ALREADY_VISITED_STORE);
            }
            reviewRepository.save(Review.builder()
                    .starRating(reviewSaveRequestDTO.getStarRaiting())
                    .content(reviewSaveRequestDTO.getContent())
                    .customer(customer)
                    .store(store)
                    .build());
        }
        else {
            throw new BusinessException(ErrorCode.NOT_VISITED_STORE);
        }
    }

    @Override
    public int deleteReview(Long reviewId, String accessToken) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        String userId = usersService.getInfoByCustomer(accessToken).getUserId();
        Optional<Customer> customer = customerRepository.findByUserId(userId);
        if (review.get() == null) {
            throw new BusinessException(ErrorCode.REVIEW_NOT_FOUND);
        }
        if(customer.get() == null){
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        if(customer.get().getId() != review.get().getCustomer().getId()){
            throw new BusinessException(ErrorCode.INVALID_JWT);
        }
        else {
            reviewRepository.delete(review.get());
            return 200;
        }
    }

    public void addVisitedStore(Long storeId, VisitStoreDTO visitStoreDTO, String accessToken) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.STORE_NOT_FOUND);
        });

        if (store.getCertificationNum() != visitStoreDTO.getCertificationNum()) {
            throw new BusinessException(ErrorCode.NOT_MATCHED_CERTIFICATION_NUM);
        }

        String customerId = usersService.getInfoByClient(accessToken).getUserId();
        Customer customer = customerRepository.findByUserId(customerId).orElse(null);

        if (visitedStoreRepository.findTopByCustomerAndStore(customer, store).isEmpty()) {
            visitedStoreRepository.save(VisitedStore.builder()
                    .customer(customer)
                    .store(store)
                    .build());
        } else {
            throw new BusinessException(ErrorCode.ALREADY_VISITED_STORE);
        }
    }
}
