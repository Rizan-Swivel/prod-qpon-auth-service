package com.swivel.cc.auth.controller;

import com.swivel.cc.auth.configuration.Translator;
import com.swivel.cc.auth.domain.response.ResponseDto;
import com.swivel.cc.auth.domain.response.SocialSignInResponseDto;
import com.swivel.cc.auth.enums.ErrorResponseStatusType;
import com.swivel.cc.auth.enums.SuccessResponseStatusType;
import com.swivel.cc.auth.wrapper.ErrorResponseWrapper;
import com.swivel.cc.auth.wrapper.ResponseWrapper;
import com.swivel.cc.auth.wrapper.SuccessLoginResponseWrapper;
import com.swivel.cc.auth.wrapper.SuccessResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.TimeZone;

/**
 * Controller
 */
public class Controller {

    protected static final String APPLICATION_JSON_UTF_8 = "application/json;charset=UTF-8";
    protected static final String APPLICATION_JSON = "application/json";
    protected static final String HEADER_USER_ID = "User-Id";
    protected static final String TIME_ZONE_HEADER = "Time-Zone";
    protected static final String ADMIN_ROLE = "ADMIN";
    protected static final String USER_ROLE = "USER";
    protected static final String MERCHANT_ROLE = "MERCHANT";
    protected static final String LOG_INVALID_TIMEZONE = "Invalid time zone for userId: {}, Time zone: {}";
    protected static final int PAGE_MAX_SIZE = 250;
    protected static final int DEFAULT_PAGE = 0;

    private final Translator translator;

    @Autowired
    public Controller(Translator translator) {
        this.translator = translator;
    }

    /**
     * This method creates the empty data response for bad request.
     *
     * @param errorResponseStatusType errorResponseStatusType
     * @return bad request error response
     */
    protected ResponseEntity<ResponseWrapper> getBadRequestError(ErrorResponseStatusType errorResponseStatusType) {
        ResponseWrapper responseWrapper =
                new ErrorResponseWrapper(errorResponseStatusType,
                        translator.toLocale(errorResponseStatusType.getCodeString(errorResponseStatusType.getCode())),
                        null);
        return new ResponseEntity<>(responseWrapper, HttpStatus.BAD_REQUEST);
    }

    /**
     * This method creates the empty data response for internal server error.
     *
     * @return internal server error response
     */
    protected ResponseEntity<ResponseWrapper> getInternalServerError() {
        ResponseWrapper responseWrapper =
                new ErrorResponseWrapper(ErrorResponseStatusType.INTERNAL_SERVER_ERROR,
                        translator.toLocale(ErrorResponseStatusType.INTERNAL_SERVER_ERROR
                                .getCodeString(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getCode())),
                        null);
        return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * This method creates empty data response for success response
     *
     * @return ok
     */
    protected ResponseEntity<ResponseWrapper> getSuccessResponse(SuccessResponseStatusType successResponseStatusType,
                                                                 ResponseDto responseDto) {
        ResponseWrapper responseWrapper = new SuccessResponseWrapper(successResponseStatusType,
                translator.toLocale(successResponseStatusType.getCode()),
                responseDto);
        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }

    /**
     * This method creates empty data response for success response
     *
     * @return ok
     */
    protected ResponseEntity<ResponseWrapper> getSuccessLoginResponse(SuccessResponseStatusType successResponseStatusType,
                                                                      SocialSignInResponseDto responseDto) {
        ResponseWrapper responseWrapper = new SuccessLoginResponseWrapper(successResponseStatusType,
                translator.toLocale(successResponseStatusType.getCode()),
                responseDto);
        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }

    protected ResponseEntity<ResponseWrapper> getUnauthorizedError(ErrorResponseStatusType errorResponseStatusType) {
        ResponseWrapper responseWrapper =
                new ErrorResponseWrapper(errorResponseStatusType,
                        translator.toLocale(errorResponseStatusType.getCodeString(errorResponseStatusType.getCode())),
                        null);
        return new ResponseEntity<>(responseWrapper, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Validate the time zone
     *
     * @param timeZone timeZone
     * @return true / false
     */
    protected boolean isValidTimeZone(String timeZone) {
        for (String tzId : TimeZone.getAvailableIDs()) {
            if (tzId.equals(timeZone)) {
                return true;
            }
        }
        return false;
    }
}
