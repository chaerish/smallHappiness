package smu.likelion.smallHappiness.review.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import smu.likelion.smallHappiness.common.entity.BaseEntity;
import smu.likelion.smallHappiness.review.dto.ReviewResponseDTO;
import smu.likelion.smallHappiness.store.entity.Store;
import smu.likelion.smallHappiness.user.entity.Customer;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name="review")
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "starRating")
    private Integer starRating;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "storeId")
    private Store store;

    @ManyToOne
    @JoinColumn(name = "customerId")
    private Customer customer;

    public ReviewResponseDTO toReviewResponseDTO(){
        return new ReviewResponseDTO(
            id,
            content,
            starRating,
            store.getId(),
            customer.getId(),
            customer.getUsername()
        );
    }
}
