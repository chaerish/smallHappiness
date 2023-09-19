package smu.likelion.smallHappiness.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smu.likelion.smallHappiness.common.dto.CommonListResponseDTO;
import smu.likelion.smallHappiness.coupon.dto.CouponRequestDTO;
import smu.likelion.smallHappiness.coupon.dto.CouponReturnDTO;
import smu.likelion.smallHappiness.coupon.service.CouponServiceImpl;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {
    @Autowired
    CouponServiceImpl couponService;

    // 가게 쿠폰 전체보기
    @GetMapping("/list")
    public ResponseEntity<?> getAllCoupon(HttpServletRequest request, @PageableDefault(size = 10) Pageable pageable){
        return ResponseEntity.ok(couponService.getAllCoupon(getAccessToken(request), pageable));
    }
    //쿠폰 등록하기
    @PostMapping
    public ResponseEntity<?> createCoupon(HttpServletRequest request, @RequestBody CouponRequestDTO couponRequestDTO){
        couponService.save(getAccessToken(request), couponRequestDTO);
        return ResponseEntity.ok().body(null);
    }
    //쿠폰 수정하기
    @PutMapping("/{couponId}")
    public ResponseEntity<?> updateCoupon(@PathVariable("couponId") Long couponId,
                                          @RequestBody CouponRequestDTO couponRequestDTO){
        couponService.update(couponId,couponRequestDTO);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{couponId}")
    public ResponseEntity<?> deleteCoupon(@PathVariable("couponId") Long id){
        couponService.delete(id);
        return ResponseEntity.ok(null);
    }

    //쿠폰 소비자에게 등록
    @PostMapping("register/{customerId}/{couponId}")
    public ResponseEntity<?> registerCoupon(@PathVariable("customerId") Long customerId, @PathVariable Long couponId){
        couponService.registerCouponToUser(customerId, couponId);
        return ResponseEntity.ok(null);
    }
    //소비자의 쿠폰 삭제
    @DeleteMapping("register/{customerCouponId}")
    public ResponseEntity<?> deleteCustomerCoupon(@PathVariable("customerCouponId") long couponId){
        couponService.deleteCoupon(couponId);
        return ResponseEntity.ok(null);
    }
    //소비자의 쿠폰 조회
    @GetMapping("/mine")
    public ResponseEntity<CommonListResponseDTO<CouponReturnDTO>> getCustomerCoupon(HttpServletRequest request){
        return ResponseEntity.ok(couponService.checkCustomerCoupon(getAccessToken(request)));
    }

    private String getAccessToken(HttpServletRequest request){
        return request.getHeader("Authorization").replace("Bearer", "").trim();
    }
}
