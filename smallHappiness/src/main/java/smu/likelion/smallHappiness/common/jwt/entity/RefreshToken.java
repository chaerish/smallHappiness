package smu.likelion.smallHappiness.common.jwt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import smu.likelion.smallHappiness.common.entity.BaseEntity;

@Entity
@Getter
@Setter
@Table(name = "refresh_token")
public class RefreshToken extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String refreshToken;

    private String userId;
}
