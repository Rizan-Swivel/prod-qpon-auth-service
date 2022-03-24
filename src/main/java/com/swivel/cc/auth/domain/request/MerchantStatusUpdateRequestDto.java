package com.swivel.cc.auth.domain.request;

import com.swivel.cc.auth.enums.ApprovalAction;
import lombok.Getter;
import lombok.Setter;

/**
 * Merchant status update requestDto
 */
@Getter
@Setter
public class MerchantStatusUpdateRequestDto extends RequestDto {

    private String merchantId;
    private ApprovalAction action;
    private String comment;

    /**
     * This method checks all required data are available.
     *
     * @return true/ false
     */
    @Override
    public boolean isRequiredAvailable() {
        return isNonEmpty(merchantId) && action != null &&
                (action.equals(ApprovalAction.UNBLOCK) || action.equals(ApprovalAction.APPROVE) ||
                        ((action.equals(ApprovalAction.BLOCK) || action.equals(ApprovalAction.REJECT))
                                && isNonEmpty(comment)));
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
