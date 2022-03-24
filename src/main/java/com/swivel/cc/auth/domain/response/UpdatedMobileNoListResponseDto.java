package com.swivel.cc.auth.domain.response;

import com.swivel.cc.auth.domain.entity.UserMobileNo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

/**
 *  UpdatedMobileNoList DTO for Response
 */
@Getter
@Setter
public class UpdatedMobileNoListResponseDto extends PageResponseDto {

    private List<UpdatedMobileNoResponseDto> updatedMobileNumbers;

    public UpdatedMobileNoListResponseDto(List<UpdatedMobileNoResponseDto> updatedMobileNoResponseDto,
                                          Page<UserMobileNo> page) {
        super(page);
        updatedMobileNumbers = new ArrayList<>();
        updatedMobileNumbers.addAll(updatedMobileNoResponseDto);
    }
}
