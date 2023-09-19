package smu.likelion.smallHappiness.store.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import smu.likelion.smallHappiness.common.dto.PageResult;
import smu.likelion.smallHappiness.common.error.BusinessException;
import smu.likelion.smallHappiness.common.error.ErrorCode;
import smu.likelion.smallHappiness.coupon.dto.CouponReturnDTO;
import smu.likelion.smallHappiness.coupon.repository.CouponRepository;
import smu.likelion.smallHappiness.menu.dto.MenuReturnDTO;
import smu.likelion.smallHappiness.menu.repository.MenuRepository;
import smu.likelion.smallHappiness.review.dto.ReviewResponseDTO;
import smu.likelion.smallHappiness.review.repository.ReviewRepository;
import smu.likelion.smallHappiness.store.dto.StoreResponseDTO;
import smu.likelion.smallHappiness.store.dto.StoreSaveRequestDTO;
import smu.likelion.smallHappiness.store.entity.Store;
import smu.likelion.smallHappiness.store.repository.StoreRepository;
import org.springframework.data.domain.Pageable;
import smu.likelion.smallHappiness.user.dto.ClientResponseDTO;
import smu.likelion.smallHappiness.user.entity.Client;
import smu.likelion.smallHappiness.user.repository.ClientRepository;
import smu.likelion.smallHappiness.user.service.UsersServiceImpl;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;
    private final UsersServiceImpl usersService;
    private final ClientRepository clientRepository;
    private final CouponRepository couponRepository;
    private final MenuRepository menuRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public PageResult<StoreResponseDTO> getStoreList(Pageable pageable) {
        Page<Store> stores = storeRepository.findAll(pageable);
        PageResult<StoreResponseDTO> result = PageResult.ok(stores.map(store ->
                store.toStoreResponseDTO(
                        store,
                        clientRepository.findByUserId(store.getClientId()).get().getStoreName(),
                        clientRepository.findByUserId(store.getClientId()).get().getStoreAddr(),
                        clientRepository.findByUserId(store.getClientId()).get().getRegNum(),
                        storeRepository.findById(store.getId()).get().getStoreTime(),
                        couponRepository.findAllByStoreId(store.getId()).stream().map(CouponReturnDTO::new).collect(Collectors.toList()),
                        menuRepository.findAllByStoreId(store.getId()).stream().map(MenuReturnDTO::new).collect(Collectors.toList()),
                        reviewRepository.findTop3ReviewsByStoreId(store.getId()).stream().map(ReviewResponseDTO::new).collect(Collectors.toList())
                        )
                )
        );
        return result;
    }

    //유저가 가게 보기
    @Override
    public StoreResponseDTO getStoreByStoreId(Long id) {
        Optional<Store> storeById = storeRepository.findById(id);
        if(storeById.isPresent()){
            Optional<Client> clientByUserId = clientRepository.findByUserId(storeById.get().getClientId());
            if(clientByUserId.isPresent()){
                StoreResponseDTO storeResponseDTO = StoreResponseDTO.builder()
                        .id(storeById.get().getId())
                        .storeName(clientByUserId.get().getStoreName())
                        .storeAddr(clientByUserId.get().getStoreAddr())
                        .regNum(clientByUserId.get().getRegNum())
                        .storeIntro(storeById.get().getStoreIntro())
                        .storeImg(storeById.get().getStoreImg())
                        .certificationNum(storeById.get().getCertificationNum())
                        .storeTime(storeById.get().getStoreTime())
                        .coupons(couponRepository.findAllByStoreId(id).stream().map(CouponReturnDTO::new).collect(Collectors.toList()))
                        .menus(menuRepository.findAllByStoreId(id).stream().map(MenuReturnDTO::new).collect(Collectors.toList()))
                        .reviews(reviewRepository.findTop3ReviewsByStoreId(id).stream().map(ReviewResponseDTO::new).collect(Collectors.toList()))
                        .build();
                return storeResponseDTO;
            }
            else{
                throw new BusinessException(ErrorCode.USER_NOT_FOUND);
            }
        }
        else{
            throw new BusinessException(ErrorCode.STORE_NOT_FOUND);
        }
    }


    //내 가게 보기
    @Override
    public StoreResponseDTO getMyStore(String accessToken){
        ClientResponseDTO client = usersService.getInfoByClient(accessToken);
        Optional<Store> store = storeRepository.findByClientId(client.getUserId());
        if(store.isPresent()){
            StoreResponseDTO storeResponseDTO = StoreResponseDTO.builder()
                    .id(store.get().getId())
                    .storeName(client.getStoreName())
                    .storeAddr(client.getStoreAddr())
                    .regNum(client.getRegNum())
                    .storeIntro(store.get().getStoreIntro())
                    .storeImg(store.get().getStoreImg())
                    .certificationNum(store.get().getCertificationNum())
                    .storeTime(store.get().getStoreTime())
                    .coupons(couponRepository.findAllByStoreId(store.get().getId()).stream().map(CouponReturnDTO::new).collect(Collectors.toList()))
                    .menus(menuRepository.findAllByStoreId(store.get().getId()).stream().map(MenuReturnDTO::new).collect(Collectors.toList()))
                    .reviews(reviewRepository.findTop3ReviewsByStoreId(store.get().getId()).stream().map(ReviewResponseDTO::new).collect(Collectors.toList()))
                    .build();
            return storeResponseDTO;
        }
        else
            throw new BusinessException(ErrorCode.STORE_NOT_FOUND);
    }
    //스토어 저장
    @Override
    public Long saveStore(StoreSaveRequestDTO storeSaveRequestDTO, String accessToken) {
        Store store = new Store();
        store.setClientId(usersService.getInfoByClient(accessToken).getUserId());
        store.setStoreIntro(storeSaveRequestDTO.getStoreIntro());
        store.setStoreImg(storeSaveRequestDTO.getStoreImg());
        store.setCertificationNum(storeSaveRequestDTO.getCertificationNum());
        store.setStoreTime(storeSaveRequestDTO.getStoreTime());
        storeRepository.save(store);
        return store.getId();
    }

    @Override
    public Long updateStore(StoreSaveRequestDTO storeSaveRequestDTO, String accessToken) {
        String clientId = usersService.getInfoByClient(accessToken).getUserId();
        Optional<Store> store = storeRepository.findByClientId(clientId);
        if(store.isPresent()) {
            Store updateStore = store.get();
            updateStore.setStoreImg(storeSaveRequestDTO.getStoreImg());
            updateStore.setStoreIntro(storeSaveRequestDTO.getStoreIntro());
            updateStore.setCertificationNum(storeSaveRequestDTO.getCertificationNum());
            updateStore.setStoreTime(storeSaveRequestDTO.getStoreTime());
            storeRepository.save(updateStore);
            return updateStore.getId();
        }
        else{
            throw new BusinessException(ErrorCode.STORE_NOT_FOUND);
        }
    }
}
