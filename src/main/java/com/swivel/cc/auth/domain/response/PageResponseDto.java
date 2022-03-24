package com.swivel.cc.auth.domain.response;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * PageResponseDto List
 */
@Getter
public class PageResponseDto implements ResponseDto {

    private final long totalItems;
    private final long totalPages;
    private final int page;
    private final int size;

    public PageResponseDto(Page<?> page) {
        this.totalItems = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        Pageable pageable = page.getPageable();
        this.page = pageable.getPageNumber();
        this.size = pageable.getPageSize();
    }

    /**
     * This method converts this object to json string for logging purpose.
     * All fields are obfuscated.
     *
     * @return json string
     */
    @Override
    public String toLogJson() {
        return toJson();
    }

}
