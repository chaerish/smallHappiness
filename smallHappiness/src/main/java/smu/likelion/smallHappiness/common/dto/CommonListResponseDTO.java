package smu.likelion.smallHappiness.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CommonListResponseDTO<T>{
    private List<T> responseList;
}
