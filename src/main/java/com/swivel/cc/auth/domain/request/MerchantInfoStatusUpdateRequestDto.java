package com.swivel.cc.auth.domain.request;

import com.swivel.cc.auth.enums.ApprovalAction;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MerchantInfoStatusUpdateRequestDto extends RequestDto {

    private String id;
    private ApprovalAction action;
    private String comment;

    /**
     * This method checks all required data are available.
     *
     * @return true/ false
     */
    @Override
    public boolean isRequiredAvailable() {
        return isNonEmpty(id) && action != null && (action.equals(ApprovalAction.APPROVE) ||
                ((action.equals(ApprovalAction.REJECT)) && isNonEmpty(comment)));
    }

    /**
     * This method converts object to json string for logging purpose.
     * PII data should be obfuscated.
     *
     * @return json string
     */
    @Override
    public String toLogJson() {
        return toJson();
    }
}
