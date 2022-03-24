package com.swivel.cc.auth.controller;

import com.swivel.cc.auth.configuration.Translator;
import com.swivel.cc.auth.domain.request.GroupedReportRequestDto;
import com.swivel.cc.auth.domain.request.ReportRequestDto;
import com.swivel.cc.auth.domain.response.AllGroupedProfileViewsResponseDto;
import com.swivel.cc.auth.domain.response.AllProfileViewsResponseDto;
import com.swivel.cc.auth.domain.response.BusinessProfileViewsResponseDto;
import com.swivel.cc.auth.domain.response.TopTenBusinessProfileViewsDto;
import com.swivel.cc.auth.enums.ErrorResponseStatusType;
import com.swivel.cc.auth.enums.GraphDateOption;
import com.swivel.cc.auth.enums.RoleType;
import com.swivel.cc.auth.enums.SuccessResponseStatusType;
import com.swivel.cc.auth.exception.AuthServiceException;
import com.swivel.cc.auth.exception.InvalidDateOptionException;
import com.swivel.cc.auth.exception.InvalidUserException;
import com.swivel.cc.auth.service.MerchantAudienceReachReportService;
import com.swivel.cc.auth.wrapper.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * Reports controller
 */
@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/v1/reports")
public class ReportsController extends Controller {

    private final MerchantAudienceReachReportService merchantAudienceReachReportService;

    @Autowired
    public ReportsController(Translator translator, MerchantAudienceReachReportService merchantAudienceReachReportService) {
        super(translator);
        this.merchantAudienceReachReportService = merchantAudienceReachReportService;
    }

    /**
     * This method is used to get audience reach report for merchant business profile.
     *
     * @param userId           userId
     * @param toUserId         toUserId
     * @param page             page
     * @param size             size
     * @param reportRequestDto reportRequestDto
     * @return audience reach report for merchant business profile
     */
    @PostMapping(value = "/{roleType}/{toUserId}/audience-reach/{page}/{size}",
            consumes = APPLICATION_JSON_UTF_8, produces = APPLICATION_JSON_UTF_8)
    public ResponseEntity<ResponseWrapper> audienceReachReportForBusinessProfile(
            @RequestHeader(name = HEADER_USER_ID) String userId,
            @RequestHeader(name = TIME_ZONE_HEADER) String timeZone,
            @PathVariable RoleType roleType,
            @PathVariable String toUserId,
            @Min(DEFAULT_PAGE) @PathVariable("page") Integer page,
            @Min(DEFAULT_PAGE) @Max(PAGE_MAX_SIZE)
            @Positive @PathVariable("size") Integer size,
            @RequestBody ReportRequestDto reportRequestDto) {

        try {
            if (!roleType.equals(RoleType.MERCHANT) && !roleType.equals(RoleType.BANK)) {
                return getBadRequestError(ErrorResponseStatusType.INVALID_USER_TYPE);
            }
            if (reportRequestDto.isRequiredAvailable()) {
                if (!reportRequestDto.isValidDateRange()) {
                    return getBadRequestError(ErrorResponseStatusType.INVALID_DATE_RANGE);
                }
                if (!reportRequestDto.isSupportedDateRange()) {
                    return getBadRequestError(ErrorResponseStatusType.UNSUPPORTED_OPTION_FOR_DATE_RANGE);
                }
                Pageable pageable = PageRequest.of(page, size);
                var profileViewsList = merchantAudienceReachReportService
                        .generateAudienceReachReport(reportRequestDto, toUserId, page, size, timeZone, roleType);
                var profileViewsPage = new PageImpl<>(profileViewsList, pageable, profileViewsList.size());
                return getSuccessResponse(SuccessResponseStatusType.GET_BUSINESS_PROFILE_VIEWS,
                        new AllProfileViewsResponseDto(profileViewsPage, profileViewsList));
            } else {
                return getBadRequestError(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS);
            }
        } catch (InvalidUserException e) {
            log.error("Invalid userId: {}. Failed to get business profile views for date option: {}.", toUserId,
                    reportRequestDto.getOption(), e);
            return getBadRequestError(ErrorResponseStatusType.INVALID_MERCHANT_ID);
        } catch (AuthServiceException e) {
            log.error("Getting audience reach report was failed toUserId: {}, toUserType: {} for option: {} by user: {}",
                    toUserId, roleType, reportRequestDto.getOption(), userId, e);
            return getInternalServerError();
        }
    }

    /**
     * This method is used to get top 10 business profile views.
     *
     * @param userId   userId
     * @param timeZone timeZone
     * @param option   graph date option
     * @return top 10 business profile views.
     */
    @GetMapping(value = "/{roleType}/audience-reach/top-ten/{option}",
            consumes = APPLICATION_JSON_UTF_8, produces = APPLICATION_JSON_UTF_8)
    public ResponseEntity<ResponseWrapper> audienceReachReportForTopTenBusinessProfiles(
            @RequestHeader(name = HEADER_USER_ID) String userId,
            @RequestHeader(name = TIME_ZONE_HEADER) String timeZone,
            @PathVariable RoleType roleType,
            @PathVariable String option) {

        try {
            if (!roleType.equals(RoleType.MERCHANT) && !roleType.equals(RoleType.BANK)) {
                return getBadRequestError(ErrorResponseStatusType.INVALID_USER_TYPE);
            }
            if (!isValidTimeZone(timeZone)) {
                log.debug(LOG_INVALID_TIMEZONE, userId, timeZone);
                return getBadRequestError(ErrorResponseStatusType.INVALID_TIMEZONE);
            }
            List<BusinessProfileViewsResponseDto> profileViewsDtoList =
                    merchantAudienceReachReportService.getTopTenViewCounts(GraphDateOption.getOption(option),
                            timeZone, roleType);
            return getSuccessResponse(SuccessResponseStatusType.GET_BUSINESS_PROFILE_VIEWS,
                    new TopTenBusinessProfileViewsDto(profileViewsDtoList));
        } catch (InvalidDateOptionException e) {
            log.error("Invalid dateOption. dateOption: {}, userId: {}", option, userId, e);
            return getBadRequestError(ErrorResponseStatusType.INVALID_DATE_OPTION);
        } catch (AuthServiceException e) {
            log.error("Getting top 10 audience reach report was failed for userId: {}, option: {}", userId, option, e);
            return getInternalServerError();
        }
    }

    /**
     * This method is used to get grouped audience reach report.
     *
     * @param userId                  userId
     * @param page                    page
     * @param size                    size
     * @param groupedReportRequestDto groupedReportRequestDto
     * @return grouped business profile views.
     */
    @PostMapping(value = "/{roleType}/grouped-audience-reach/{toUserId}/{page}/{size}",
            consumes = APPLICATION_JSON_UTF_8, produces = APPLICATION_JSON_UTF_8)
    public ResponseEntity<ResponseWrapper> groupedAudienceReachReportForBusinessProfile(
            @RequestHeader(name = HEADER_USER_ID) String userId,
            @PathVariable RoleType roleType,
            @PathVariable String toUserId,
            @Min(DEFAULT_PAGE) @PathVariable("page") Integer page,
            @Min(DEFAULT_PAGE) @Max(PAGE_MAX_SIZE) @Positive @PathVariable("size") Integer size,
            @RequestBody GroupedReportRequestDto groupedReportRequestDto) {

        try {
            if (!roleType.equals(RoleType.MERCHANT) && !roleType.equals(RoleType.BANK)) {
                return getBadRequestError(ErrorResponseStatusType.INVALID_USER_TYPE);
            }
            if (groupedReportRequestDto.isRequiredAvailable()) {
                if (!groupedReportRequestDto.isValidDateRange()) {
                    return getBadRequestError(ErrorResponseStatusType.INVALID_DATE_RANGE);
                }
                Pageable pageable = PageRequest.of(page, size);
                var profileViewsList = merchantAudienceReachReportService
                        .getReportForDateRange(groupedReportRequestDto.getStartDate(),
                                groupedReportRequestDto.getEndDate(), toUserId, page, size, roleType);
                var profileViewsPage = new PageImpl<>(profileViewsList, pageable, profileViewsList.size());
                return getSuccessResponse(SuccessResponseStatusType.GET_BUSINESS_PROFILE_VIEWS,
                        new AllGroupedProfileViewsResponseDto(profileViewsPage, profileViewsList));
            } else {
                return getBadRequestError(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS);
            }
        } catch (AuthServiceException e) {
            log.error("Getting grouped audience reach report was failed for userId: {}, toUserType: {}.",
                    userId, roleType, e);
            return getInternalServerError();
        }
    }
}
