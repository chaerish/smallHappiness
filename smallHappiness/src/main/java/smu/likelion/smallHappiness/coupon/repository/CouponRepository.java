package smu.likelion.smallHappiness.coupon.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import smu.likelion.smallHappiness.coupon.entity.Coupon;

import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon,Long> {
    Page<Coupon> findAllByStoreId(Long storeId, Pageable pageable);
    List<Coupon> findAllByStoreId(Long storeId);
    List<Coupon> findAllByIdIn(@Param("ids")List<Long> ids);
}