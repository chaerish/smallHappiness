package smu.likelion.smallHappiness.menu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smu.likelion.smallHappiness.common.dto.PageResult;
import smu.likelion.smallHappiness.common.error.BusinessException;
import smu.likelion.smallHappiness.common.error.ErrorCode;
import smu.likelion.smallHappiness.menu.dto.MenuRequestDTO;
import smu.likelion.smallHappiness.menu.dto.MenuReturnDTO;
import smu.likelion.smallHappiness.menu.entity.Menu;
import smu.likelion.smallHappiness.menu.repository.MenuRepository;
import smu.likelion.smallHappiness.store.repository.StoreRepository;
import smu.likelion.smallHappiness.store.entity.Store;
import smu.likelion.smallHappiness.user.service.UsersServiceImpl;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final UsersServiceImpl usersService;
    private final StoreRepository storeRepository;


    //가게 메뉴 전체 보기
    @Override
    public PageResult<MenuReturnDTO> getAllMenu(Long storeId, Pageable pageable) {
        Page<Menu> menus = menuRepository.findAllByStoreId(storeId, pageable);
        PageResult<MenuReturnDTO> result = PageResult.ok(menus.map(menu -> menu.toMenuReturnDTO()));
        return result;
    }

    //메뉴 상세보기
    @Override
    public MenuReturnDTO findById(Long id) {
        try {
            Optional<Menu> menuData = menuRepository.findById(id);
            if (menuData.isPresent()) {
                return menuData.get().toMenuReturnDTO();
            }
            else{
                throw new BusinessException(ErrorCode.MENU_NOT_FOUND);
            }
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.MENU_NOT_FOUND);
        }
    }

    //메뉴 생성
    @Override
    public void save(String accessToken, MenuRequestDTO menuRequestDTO) {
        String clientId = usersService.getInfoByClient(accessToken).getUserId();
        Store store = storeRepository.findByClientId(clientId).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.STORE_NOT_FOUND);
        });
        menuRepository.save(menuRequestDTO.toEntity(store));
    }


    //특정 메뉴 수정하기
    @Override
    public void update(Long id, MenuRequestDTO menuRequestDTO) {
        Optional<Menu> menuData = menuRepository.findById(id);
        if (menuData.isPresent()) {
            Menu _menu = menuData.get();
            _menu.setMenuName(menuRequestDTO.getMenuName());
            _menu.setPrice(menuRequestDTO.getPrice());
            _menu.setMenuImg(menuRequestDTO.getMenuImg());
        } else
            throw new BusinessException(ErrorCode.MENU_NOT_FOUND);
    }


    @Override
    public boolean delete(Long id) {
        Optional<Menu> menuData = menuRepository.findById(id);
        if (menuData.isPresent()) {
            menuRepository.delete(menuData.get());
            return true;
        } else {
            throw new BusinessException(ErrorCode.MENU_NOT_FOUND);
        }
    }
}