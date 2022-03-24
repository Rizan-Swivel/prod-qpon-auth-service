package com.swivel.cc.auth.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * customer request dto
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MobileNoRequestDto extends RequestDto {

    private static final String SEPARATOR = "-";
    private static final String EMPTY = "";
    private static final int FIRST_PART_INDEX = 0;
    private static final int SECOND_PART_INDEX = 1;
    private String countryCode;
    private String localNumber;

    /**
     * This method checks all required fields are available.
     *
     * @return true/ false
     */
    @Override
    public boolean isRequiredAvailable() {
        return isNonEmpty(countryCode) && isNonEmpty(localNumber);
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

    /**
     * This method converts mobileNoRequestDto to below format.
     * Eg: +94-713321911
     *
     * @return formatted mobile no
     */
    public String getNo() {
        return isNonEmpty(countryCode) && isNonEmpty(localNumber) ?
                countryCode.trim() + SEPARATOR + localNumber.trim()
                : null;
    }


    public MobileNoRequestDto(String mobileNo) {
        if (mobileNo != null && !mobileNo.trim().isEmpty() && mobileNo.contains(SEPARATOR)) {
            String[] mobileNoParts = mobileNo.split(SEPARATOR);
            this.countryCode = mobileNoParts[FIRST_PART_INDEX];
            this.localNumber = mobileNoParts[SECOND_PART_INDEX];
        }
    }
}
