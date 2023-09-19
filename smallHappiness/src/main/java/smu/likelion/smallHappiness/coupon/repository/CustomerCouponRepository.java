package smu.likelion.smallHappiness.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smu.likelion.smallHappiness.coupon.entity.CustomerCoupon;

import java.util.List;

@Repository
public interface CustomerCouponRepository extends JpaRepository <CustomerCoupon,Long>{
    List<CustomerCoupon> findAllByCustomerId(Long customerId);
}
