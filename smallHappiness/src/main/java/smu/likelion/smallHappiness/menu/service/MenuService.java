package smu.likelion.smallHappiness.menu.service;

import org.springframework.data.domain.Pageable;
import smu.likelion.smallHappiness.common.dto.PageResult;
import smu.likelion.smallHappiness.menu.dto.MenuRequestDTO;
import smu.likelion.smallHappiness.menu.dto.MenuReturnDTO;

public interface MenuService {
    //메뉴 등록하기
    void save(String accessToken,MenuRequestDTO menuRequestDTO);
    //가게 메뉴 상세보기
    MenuReturnDTO findById(Long id);
    //가게 메뉴 전체 보기
    PageResult<MenuReturnDTO> getAllMenu(Long storeId, Pageable pageable);
    void update(Long id, MenuRequestDTO menuRequestDTO);
    boolean delete(Long id);




}
