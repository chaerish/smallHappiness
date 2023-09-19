package smu.likelion.smallHappiness.coupon.service;

import org.springframework.data.domain.Pageable;
import smu.likelion.smallHappiness.common.dto.CommonListResponseDTO;
import smu.likelion.smallHappiness.common.dto.PageResult;
import smu.likelion.smallHappiness.coupon.dto.CouponRequestDTO;
import smu.likelion.smallHappiness.coupon.dto.CouponReturnDTO;

public interface CouponService {
    // 가게 -> 쿠폰 등록
    void save(String accessToken, CouponRequestDTO couponRequestDTO);
    //가게 -> 쿠폰
    PageResult<CouponReturnDTO> getAllCoupon(String accessToken, Pageable pageable);
    // 가게 -> 쿠폰 수정
    void update(Long couponId, CouponRequestDTO couponRequestDTO);
    //가게 -> 쿠폰 삭제
    void delete(Long couponId);
    //소비자에게 쿠폰 등록
    void registerCouponToUser(Long customerId,Long couponId);
    //소비자의 쿠폰 삭제
    void deleteCoupon(Long customerCouponId);
    // 소비자의 쿠폰 조회
    CommonListResponseDTO<CouponReturnDTO> checkCustomerCoupon(String customerAccessToken);

}
