package smu.likelion.smallHappiness.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewSaveRequestDTO {
    private int starRaiting;
    private String content;
}
