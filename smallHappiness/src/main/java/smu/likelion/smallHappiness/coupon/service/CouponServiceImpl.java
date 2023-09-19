package smu.likelion.smallHappiness.coupon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smu.likelion.smallHappiness.common.dto.CommonListResponseDTO;
import smu.likelion.smallHappiness.common.dto.PageResult;
import smu.likelion.smallHappiness.common.error.BusinessException;
import smu.likelion.smallHappiness.common.error.ErrorCode;
import smu.likelion.smallHappiness.coupon.dto.CouponRequestDTO;
import smu.likelion.smallHappiness.coupon.dto.CouponReturnDTO;
import smu.likelion.smallHappiness.coupon.entity.Coupon;
import smu.likelion.smallHappiness.coupon.entity.CustomerCoupon;
import smu.likelion.smallHappiness.coupon.repository.CouponRepository;
import smu.likelion.smallHappiness.coupon.repository.CustomerCouponRepository;
import smu.likelion.smallHappiness.store.repository.StoreRepository;
import smu.likelion.smallHappiness.store.entity.Store;
import smu.likelion.smallHappiness.user.dto.CustomerResponseDTO;
import smu.likelion.smallHappiness.user.repository.CustomerRepository;
import smu.likelion.smallHappiness.user.service.UsersServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final StoreRepository storeRepository;
    private final CustomerRepository customerRepository;
    private final CustomerCouponRepository customerCouponRepository;
    private final UsersServiceImpl usersService;

    //쿠폰 등록
    @Override
    public void save(String accessToken, CouponRequestDTO couponRequestDTO) {
        String clientId = usersService.getInfoByClient(accessToken).getUserId();
        Store store = storeRepository.findByClientId(clientId).orElseThrow(()->{
            throw new BusinessException(ErrorCode.STORE_NOT_FOUND);
        });
        couponRepository.save(couponRequestDTO.toEntity(store));
    }

    //전체 쿠폰 조회
    @Override
    public PageResult<CouponReturnDTO> getAllCoupon(String accessToken, Pageable pageable) {
        String clientId = usersService.getInfoByClient(accessToken).getUserId();
        Long storeId = storeRepository.findByClientId(clientId).get().getId();
        Page<Coupon> coupons = couponRepository.findAllByStoreId(storeId, pageable);
        PageResult result = PageResult.ok(coupons.map(coupon -> coupon.toCouponReturnDTO()));
        return result;
    }

    // 쿠폰 내용 수정하기
    @Override
    public void update(Long couponId, CouponRequestDTO couponRequestDTO) {
        Optional<Coupon> couponData = couponRepository.findById(couponId);
        if (couponData.isPresent()) {
            Coupon _coupon = couponData.get();
            _coupon.setContent(couponRequestDTO.getContent());
            _coupon.setCouponNum(couponRequestDTO.getCouponNum());
        }else
             throw new BusinessException(ErrorCode.COUPON_NOT_FOUND);
    }

    //쿠폰 삭제하기
    @Override
    public void delete(Long id) {
        Optional<Coupon> couponData = couponRepository.findById(id);
        if (couponData.isPresent()) {
            couponRepository.delete(couponData.get());
        } else {
            throw new BusinessException(ErrorCode.COUPON_NOT_FOUND);
        }
    }

    /* 쿠폰-소비자 */
    @Override
    public void registerCouponToUser(Long customerId, Long couponId){
        couponRepository.findById(couponId).orElseThrow(()->{
                    throw new BusinessException(ErrorCode.COUPON_NOT_FOUND);
                });

        // 손님 아이디를 기반으로 손님을 조회
        customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        CustomerCoupon customerCoupon = CustomerCoupon.builder()
                .couponId(couponId)
                .customerId(customerId)
                .build();
        // 쿠폰과 손님을 연결
       customerCouponRepository.save(customerCoupon);
    }

    @Override
    public void deleteCoupon(Long customerCouponId) {
        try {
            customerCouponRepository.delete(customerCouponRepository.findById(customerCouponId).get());
        } catch (Exception e){
            e.printStackTrace();
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    //사용자 쿠폰 조회
    @Override
    public CommonListResponseDTO<CouponReturnDTO> checkCustomerCoupon(String accessToken) {
        CustomerResponseDTO customer = usersService.getInfoByCustomer(accessToken); //customerID찾음
        Long customerId = customerRepository.findByUserId(customer.getUserId()).get().getId();
        List<CustomerCoupon> customerCoupons = customerCouponRepository.findAllByCustomerId(customerId);
        List<Long> ids = new ArrayList<>();
        for(int i=0; i<customerCoupons.size(); i++){
            ids.add(customerCoupons.get(i).getCouponId());
        }
        //이걸로 쿠폰 조회함
        List<Coupon> coupons = couponRepository.findAllByIdIn(ids);
        return new CommonListResponseDTO<>(coupons.stream().map(CouponReturnDTO::new).collect(Collectors.toList()));
    }
}
