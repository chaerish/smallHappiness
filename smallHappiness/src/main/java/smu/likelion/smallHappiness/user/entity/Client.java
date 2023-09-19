package smu.likelion.smallHappiness.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import smu.likelion.smallHappiness.common.entity.BaseEntity;
import smu.likelion.smallHappiness.coupon.dto.CouponReturnDTO;
import smu.likelion.smallHappiness.menu.dto.MenuReturnDTO;
import smu.likelion.smallHappiness.user.dto.ClientDetailResponseDTO;
import smu.likelion.smallHappiness.user.dto.ClientResponseDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name="client")
public class Client extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "userId")
    private String userId;

    @Column(name = "userPw")
    private String userPw;

    @Column(name = "name")
    private String name;

    @Column(name = "storeName")
    private String storeName;

    @Column(name = "storeAddr")
    private String storeAddr;

    @Column(name = "regNum")
    private String regNum;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public String getPassword() {
        return userPw;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public ClientResponseDTO toClientResponseDTO(){
        return new ClientResponseDTO(
                userId,
                name,
                storeName,
                storeAddr,
                regNum
        );
    }

    public ClientDetailResponseDTO toClientDetailResponseDTO(List<MenuReturnDTO> menus, List<CouponReturnDTO> coupons){
        return new ClientDetailResponseDTO(
                userId,
                name,
                storeName,
                storeAddr,
                regNum,
                menus,
                coupons
        );
    }
}
