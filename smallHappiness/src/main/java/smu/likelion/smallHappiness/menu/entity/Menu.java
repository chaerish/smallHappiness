package smu.likelion.smallHappiness.menu.entity;

import jakarta.persistence.*;
import lombok.*;
import smu.likelion.smallHappiness.menu.dto.MenuReturnDTO;
import smu.likelion.smallHappiness.store.entity.Store;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "menus")

public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "menuName")
    private String menuName;

    @Column(name = "price")
    private int price;

    @Column(name = "menuImg")
    private String menuImg;

    @ManyToOne
    @JoinColumn(name="storeId")
    private Store store;

    public MenuReturnDTO toMenuReturnDTO(){
        return new MenuReturnDTO(
                this.menuName,
                this.price,
                this.menuImg,
                this.id,
                store.getId()
        );
    }

}
