package smu.likelion.smallHappiness.common.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageResult<T>{
    int currentPage;
    int pageSize;
    int totalPage;
    Long totalElements;
    List<T> content;

    public PageResult(Page<T> data) {
        currentPage = data.getPageable().getPageNumber();
        pageSize = data.getPageable().getPageSize();
        totalPage = data.getTotalPages();
        totalElements = data.getTotalElements();
        content = data.getContent();
    }

    public static PageResult ok(Page data){
        return new PageResult(data);
    }
}
