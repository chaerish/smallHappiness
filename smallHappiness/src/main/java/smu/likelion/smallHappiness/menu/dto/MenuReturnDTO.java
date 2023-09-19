package smu.likelion.smallHappiness.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import smu.likelion.smallHappiness.menu.entity.Menu;

@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
public class MenuReturnDTO {

    private String menuName;
    private int price;
    private String menuImg;
    private Long menuId;
    private Long storeId;
    public MenuReturnDTO(Menu menu){
        menuName=menu.getMenuName();
        price=menu.getPrice();
        menuImg=menu.getMenuImg();
        menuId=menu.getId();
        storeId=menu.getStore().getId();
    }
}