package smu.likelion.smallHappiness.user.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smu.likelion.smallHappiness.common.error.BusinessException;
import smu.likelion.smallHappiness.common.error.ErrorCode;
import smu.likelion.smallHappiness.common.jwt.dto.TokenInfo;
import smu.likelion.smallHappiness.common.jwt.entity.RefreshToken;
import smu.likelion.smallHappiness.common.jwt.repository.RefreshTokenRepository;
import smu.likelion.smallHappiness.common.jwt.service.RefreshServiceImpl;
import smu.likelion.smallHappiness.common.jwt.util.JwtTokenProvider;
import smu.likelion.smallHappiness.coupon.dto.CouponReturnDTO;
import smu.likelion.smallHappiness.coupon.entity.CustomerCoupon;
import smu.likelion.smallHappiness.coupon.repository.CouponRepository;
import smu.likelion.smallHappiness.coupon.repository.CustomerCouponRepository;
import smu.likelion.smallHappiness.menu.dto.MenuReturnDTO;
import smu.likelion.smallHappiness.menu.repository.MenuRepository;
import smu.likelion.smallHappiness.review.dto.ReviewResponseDTO;
import smu.likelion.smallHappiness.review.repository.ReviewRepository;
import smu.likelion.smallHappiness.store.entity.VisitedStore;
import smu.likelion.smallHappiness.store.repository.StoreRepository;
import smu.likelion.smallHappiness.store.repository.VisitedStoreRepository;
import smu.likelion.smallHappiness.user.dto.*;
import smu.likelion.smallHappiness.user.entity.Client;
import smu.likelion.smallHappiness.user.entity.Customer;
import smu.likelion.smallHappiness.user.repository.ClientRepository;
import smu.likelion.smallHappiness.user.repository.CustomerRepository;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final RefreshServiceImpl refreshService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CustomerRepository customerRepository;
    private final ClientRepository clientRepository;
    private final MenuRepository menuRepository;
    private final CouponRepository couponRepository;
    private final CustomerCouponRepository customerCouponRepository;
    private final StoreRepository storeRepository;
    private final VisitedStoreRepository visitedStoreRepository;
    private final ReviewRepository reviewRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    @Value("${jwt.token.key}")
    private String secretKey;

    @Override
    @Transactional
    public TokenInfo logIn(LogInRequestDTO logInRequestDTO){
        String roles;
        boolean customer = customerRepository.existsByUserId(logInRequestDTO.getUserId());
        boolean client = clientRepository.existsByUserId(logInRequestDTO.getUserId());
        if(!customer && !client){
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        if(client){
            roles = "client";
        } else {
            roles = "customer";
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(logInRequestDTO.getUserId(), logInRequestDTO.getUserPw());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication, roles);

        refreshTokenRepository.deleteByUserId(logInRequestDTO.getUserId());

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(logInRequestDTO.getUserId());
        refreshToken.setRefreshToken(tokenInfo.getRefreshToken());
        refreshTokenRepository.save(refreshToken);
        return tokenInfo;
    }

    @Transactional
    public TokenInfo refresh(String accessToken, String refreshToken){
        Key key = getKey(secretKey);
        String userId = refreshService.getRefresh(refreshToken);
        if(userId == null) throw new BusinessException(ErrorCode.INVALID_JWT);

        if(!customerRepository.existsByUserId(userId) && !clientRepository.existsByUserId(userId)){
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        String roles = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(accessToken)
                .getBody()
                .get("roles").toString();
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        TokenInfo newToken = jwtTokenProvider.generateToken(authentication, roles);
        storeRefresh(userId, newToken.getRefreshToken());
        return newToken;
    }

    @Transactional
    public void storeRefresh(String userId, String refreshToken){
        RefreshToken token = refreshTokenRepository.findByUserId(userId);
        token.setRefreshToken(refreshToken);
        refreshTokenRepository.save(token);
    }
    @Override
    @Transactional
    public void logout(String refreshToken){
        refreshService.deleteRefresh(refreshToken);
    }

    public String getUserRoles(String accessToken){
        Key key = getKey(secretKey);
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(accessToken)
                .getBody()
                .get("roles").toString();
    }

    @Override
    public ClientResponseDTO getInfoByClient(String accessToken){
        String userId = getUserId(accessToken);
        return clientRepository.findByUserId(userId).get().toClientResponseDTO();
    }

    @Override
    public CustomerResponseDTO getInfoByCustomer(String accessToken){
        String userId = getUserId(accessToken);
        return customerRepository.findByUserId(userId).get().toCustomerResponseDTO();
    }

    @Override
    public ClientDetailResponseDTO getDetailByClient(String accessToken){
        try {
            String userId = getUserId(accessToken);
            Long storeId = storeRepository.findByClientId(userId).get().getId();
            Client client = clientRepository.findByUserId(userId).get();
            List<MenuReturnDTO> menus = menuRepository.findAllByStoreId(storeId)
                    .stream().map(MenuReturnDTO::new).collect(Collectors.toList());
            List<CouponReturnDTO> coupons = couponRepository.findAllByStoreId(storeId)
                    .stream().map(CouponReturnDTO::new).collect(Collectors.toList());
            return client.toClientDetailResponseDTO(menus, coupons);
        } catch (Exception e){
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomerDetailResponseDTO getDetailByCustomer(String accessToken){
        try {
            String userId = getUserId(accessToken);
            Customer customer = customerRepository.findByUserId(userId).get();
            List<Long> visitedStores = new ArrayList<>();
            List<Long> couponIds = new ArrayList<>();
            List<VisitedStore> stores = visitedStoreRepository.findAllByCustomer(customer);
            for(int i=0; i<stores.size(); i++){
                visitedStores.add(stores.get(i).getStore().getId());
            }
            List<CustomerCoupon> customerCoupons = customerCouponRepository.findAllByCustomerId(customer.getId());
            for(int i=0; i<customerCoupons.size(); i++){
                couponIds.add(customerCoupons.get(i).getCouponId());
            }
            List<CouponReturnDTO> coupons = couponRepository.findAllByIdIn(couponIds)
                    .stream().map(CouponReturnDTO::new).collect(Collectors.toList());

            List<ReviewResponseDTO> reviews = reviewRepository.findReviewsByCustomerId(customer.getId())
                    .stream().map(ReviewResponseDTO::new).collect(Collectors.toList());
            return customer.toCustomerDetailResponseDTO(visitedStores, coupons, reviews);
        } catch (Exception e){
            e.printStackTrace();
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private String getUserId(String accessToken){
        Key key = getKey(secretKey);
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(accessToken)
                .getBody()
                .getSubject();
    }

    private Key getKey(String secretKey){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
