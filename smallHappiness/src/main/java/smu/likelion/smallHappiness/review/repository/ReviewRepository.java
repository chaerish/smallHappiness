package smu.likelion.smallHappiness.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smu.likelion.smallHappiness.review.entity.Review;
import smu.likelion.smallHappiness.store.entity.Store;
import smu.likelion.smallHappiness.user.entity.Customer;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    //가게 리뷰 불러오기
    Page<Review> findAllByStoreId(Long storeId, Pageable pageable);
    List<Review> findReviewsByCustomerId(Long customerId);
    Optional<Review> findTopReviewsByCustomerAndStore(Customer customer, Store store);
    List <Review> findTop3ReviewsByStoreId(Long storeId);
}
