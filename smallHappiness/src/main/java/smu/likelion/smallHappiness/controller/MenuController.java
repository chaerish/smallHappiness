package smu.likelion.smallHappiness.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smu.likelion.smallHappiness.menu.dto.MenuRequestDTO;
import smu.likelion.smallHappiness.menu.dto.MenuReturnDTO;
import smu.likelion.smallHappiness.menu.service.MenuServiceImpl;
import smu.likelion.smallHappiness.user.service.UsersServiceImpl;


@RestController
@RequestMapping("/api/menu")
public class MenuController {
    @Autowired
    MenuServiceImpl menuService;

    //가게의 전체 메뉴 보기
    @GetMapping("/list/{storeId}")
    public ResponseEntity<?> getAllMenu(@PathVariable("storeId") Long id, @PageableDefault(size = 10) Pageable pageable){
        return ResponseEntity.ok(menuService.getAllMenu(id, pageable));
    }
    //가게 메뉴 추가하기
    @PostMapping
    public ResponseEntity<?> createMenu(HttpServletRequest request, @RequestBody MenuRequestDTO menuRequestDTO){
        menuService.save(getAccessToken(request),menuRequestDTO);
        return ResponseEntity.ok(null);
    }
    //가게 메뉴 상세보기
    @GetMapping("/detail/{menuId}")
    public ResponseEntity<MenuReturnDTO> getMenuById(@PathVariable("menuId") Long menuId){
        return ResponseEntity.ok(menuService.findById(menuId));
    }
    @PutMapping("/{menuId}")
    public ResponseEntity<?> updateMenu(@PathVariable("menuId") Long id,@RequestBody MenuRequestDTO menuRequestDTO){
        menuService.update(id, menuRequestDTO);
        return ResponseEntity.ok(null);
    }
    @DeleteMapping("/{menuId}")
    public ResponseEntity<?> deleteMenu(@PathVariable("menuId") Long id){
        if(menuService.delete(id)){
            return ResponseEntity.ok().body(null);
        }
        else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    private String getAccessToken(HttpServletRequest request){
        return request.getHeader("Authorization").replace("Bearer", "").trim();
    }
}