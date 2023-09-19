package smu.likelion.smallHappiness.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smu.likelion.smallHappiness.store.entity.Store;
import smu.likelion.smallHappiness.store.entity.VisitedStore;
import smu.likelion.smallHappiness.user.entity.Customer;

import java.util.List;
import java.util.Optional;

@Repository
public interface VisitedStoreRepository extends JpaRepository<VisitedStore, Long> {
    // 내가 방문한 가게 추가
    VisitedStore save(VisitedStore visitedStore);

    // 내가 방문한 가게 삭제
    void delete(VisitedStore visitedStore);

    // 내가 방문한 가게 조회
    Optional<VisitedStore> findTopByCustomerAndStore(Customer customer, Store store);
    List<VisitedStore> findAllByCustomer(Customer customer);
}