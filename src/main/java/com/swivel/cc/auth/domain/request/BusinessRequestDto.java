package com.swivel.cc.auth.domain.request;

import com.swivel.cc.auth.domain.response.ResponseDto;
import com.swivel.cc.auth.util.Validator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Merchant business details request Dto
 */
@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BusinessRequestDto extends RequestDto {

    private static final String INVALID_IMAGE_URL = "Invalid image URL : {}";
    private String merchantId;
    private String businessName;
    private String ownerName;
    private MobileNoRequestDto mobileNo;
    private MobileNoRequestDto telephone;
    private String email;
    private String businessRegNo;
    private String address;
    private String imageUrl;
    private String webSite;
    private String facebook;
    private String instagram;

    @Override
    public boolean isRequiredAvailable() {
        return isNonEmpty(merchantId) && isNonEmpty(businessName) && isNonEmpty(ownerName) && isNonEmpty(imageUrl);
    }

    @Override
    public String toLogJson() {
        BasicBusinessLog basicBusinessLog = new BasicBusinessLog(this);
        return basicBusinessLog.toLogJson();
    }

    /**
     * Checks if valid mobile number.
     *
     * @param validator validator
     * @return true/false
     */
    public boolean isValidMobileNo(Validator validator) {
        return mobileNo != null && mobileNo.getNo() != null
                && validator.isValidMobileNoWithCountryCode(mobileNo.getNo());
    }

    /**
     * validate if telephone number present.
     *
     * @param validator validator
     * @return true/ false
     */
    public boolean validateTelephoneNumberIfPresent(Validator validator) {
        return telephone == null || telephone.getNo() == null
                || validator.isValidMobileNoWithCountryCode(telephone.getNo());
    }

    /**
     * Checks if valid email address.
     *
     * @param validator validator
     * @return true/false
     */
    public boolean isContainingValidEmail(Validator validator) {
        return !isNonEmpty(email) || validator.isValidEmail(email);
    }

    /**
     * Checks if valid urls.
     *
     * @param validator validator
     * @return true/false
     */
    public boolean isContainingValidUrls(Validator validator) {
        if (isNonEmpty(imageUrl) && !validator.isValidUrl(imageUrl)) {
            log.error(INVALID_IMAGE_URL, imageUrl);
        }
        return (!isNonEmpty(webSite) || validator.isValidUrl(webSite))
                && (!isNonEmpty(facebook) || validator.isValidUrl(facebook))
                && (!isNonEmpty(instagram) || validator.isValidUrl(instagram));
    }

    /**
     * This class is for logging purpose only.
     */
    @Getter
    private class BasicBusinessLog implements ResponseDto {
        private final String merchantId;
        private final String businessName;

        public BasicBusinessLog(BusinessRequestDto businessRequestDto) {
            this.merchantId = businessRequestDto.getMerchantId();
            this.businessName = businessRequestDto.businessName;
        }

        @Override
        public String toLogJson() {
            return toJson();
        }
    }
}
