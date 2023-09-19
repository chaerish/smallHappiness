package smu.likelion.smallHappiness.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import smu.likelion.smallHappiness.menu.entity.Menu;
import smu.likelion.smallHappiness.store.entity.Store;

@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
public class MenuRequestDTO {
    private String menuName;
    private int price;
    private String menuImg;

    public Menu toEntity(Store store){
        return Menu.builder()
                .menuName(this.menuName)
                .price(this.price)
                .menuImg(this.menuImg)
                .store(store)
                .build();
    }
}
