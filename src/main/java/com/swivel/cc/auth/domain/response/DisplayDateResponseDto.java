package com.swivel.cc.auth.domain.response;

import com.swivel.cc.auth.domain.entity.BankBusiness;
import com.swivel.cc.auth.domain.entity.Business;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DisplayDateResponseDto extends BusinessProfileViewsResponseDto {

    private String displayDate;

    public DisplayDateResponseDto(long viewCount, String displayDate, Business business) {
        super(viewCount, business);
        this.displayDate = displayDate;
    }

    public DisplayDateResponseDto(long viewCount, String displayDate, BankBusiness bankBusiness) {
        super(viewCount, new Business(bankBusiness));
        this.displayDate = displayDate;
    }
}
