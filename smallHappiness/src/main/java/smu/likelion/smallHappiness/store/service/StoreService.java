package smu.likelion.smallHappiness.store.service;

import smu.likelion.smallHappiness.common.dto.PageResult;
import smu.likelion.smallHappiness.store.dto.StoreResponseDTO;
import smu.likelion.smallHappiness.store.dto.StoreSaveRequestDTO;
import org.springframework.data.domain.Pageable;

public interface StoreService {
    PageResult<StoreResponseDTO> getStoreList(Pageable pageable);
    StoreResponseDTO getStoreByStoreId(Long id);
    Long saveStore(StoreSaveRequestDTO storeSaveRequestDTO, String accessToken);
    Long updateStore(StoreSaveRequestDTO storeSaveRequestDTO, String accessToken);
    StoreResponseDTO getMyStore(String accessToken);
}
