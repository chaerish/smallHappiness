package smu.likelion.smallHappiness.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import smu.likelion.smallHappiness.store.dto.StoreResponseDTO;
import smu.likelion.smallHappiness.store.dto.StoreSaveRequestDTO;
import smu.likelion.smallHappiness.store.service.StoreService;
import org.springframework.data.domain.Pageable;

@Controller
@RequestMapping("/api/store")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    //유저가 가게 상세보기
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getStoreById(@PathVariable Long id) {
        return ResponseEntity.ok().body(storeService.getStoreByStoreId(id));
    }

    @GetMapping("/list")
    public ResponseEntity<?> getStoreList(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok().body(storeService.getStoreList(pageable));
    }

    @GetMapping
    public ResponseEntity<StoreResponseDTO> getMyStore(HttpServletRequest request){
        return ResponseEntity.ok(storeService.getMyStore(getAccessToken(request)));
    }

    @PutMapping
    public ResponseEntity<?> updateStore(@RequestBody StoreSaveRequestDTO storeSaveRequestDTO, HttpServletRequest request) {
        return ResponseEntity.ok(storeService.updateStore(storeSaveRequestDTO, getAccessToken(request)));
    }

    @PostMapping
    public ResponseEntity<?> saveStore(@RequestBody StoreSaveRequestDTO storeSaveRequestDTO, HttpServletRequest request) {
        return ResponseEntity.ok(storeService.saveStore(storeSaveRequestDTO, getAccessToken(request)));
    }

    private String getAccessToken(HttpServletRequest request){
        return request.getHeader("Authorization").replace("Bearer", "").trim();
    }
}
