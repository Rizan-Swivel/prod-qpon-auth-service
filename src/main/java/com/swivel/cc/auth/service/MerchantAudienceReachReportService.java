package com.swivel.cc.auth.service;

import com.google.analytics.data.v1beta.*;
import com.swivel.cc.auth.domain.entity.ApprovedBankBusiness;
import com.swivel.cc.auth.domain.entity.ApprovedBusiness;
import com.swivel.cc.auth.domain.entity.BusinessProfileViews;
import com.swivel.cc.auth.domain.request.ReportRequestDto;
import com.swivel.cc.auth.domain.response.BusinessProfileViewsResponseDto;
import com.swivel.cc.auth.domain.response.DisplayDateResponseDto;
import com.swivel.cc.auth.domain.response.GroupedProfileViewsResponseDto;
import com.swivel.cc.auth.enums.AnalyticsDimensionsAndMetrics;
import com.swivel.cc.auth.enums.GraphDateOption;
import com.swivel.cc.auth.enums.ReportDateOption;
import com.swivel.cc.auth.enums.RoleType;
import com.swivel.cc.auth.exception.AuthServiceException;
import com.swivel.cc.auth.repository.ApprovedBankBusinessRepository;
import com.swivel.cc.auth.repository.ApprovedBusinessRepository;
import com.swivel.cc.auth.util.DateRangeConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Audience reach report service
 */
@Service
@Slf4j
public class MerchantAudienceReachReportService {

    private static final String PROPERTY = "properties/";
    private static final String ANALYTICS_DATE_FORMAT = "yyyy-MM-dd";
    private static final String ALL = "ALL";
    private static final int ZERO_ELEMENT = 0;
    private static final int FIRST_ELEMENT = 1;
    private final int pageSize;
    private final String propertyId;
    private final MerchantService merchantService;
    private final ApprovedBusinessRepository approvedBusinessRepository;
    private final ApprovedBankBusinessRepository approvedBankBusinessRepository;
    private final BetaAnalyticsDataClient analyticsData;

    @Autowired
    public MerchantAudienceReachReportService(@Value("${googleAnalytics.pageSize}") int pageSize,
                                              @Value("${googleAnalytics.propertyId}") String propertyId,
                                              MerchantService merchantService, BetaAnalyticsDataClient analyticsData,
                                              ApprovedBusinessRepository approvedBusinessRepository,
                                              ApprovedBankBusinessRepository approvedBankBusinessRepository) {
        this.pageSize = pageSize;
        this.propertyId = propertyId;
        this.merchantService = merchantService;
        this.approvedBusinessRepository = approvedBusinessRepository;
        this.analyticsData = analyticsData;
        this.approvedBankBusinessRepository = approvedBankBusinessRepository;
    }

    /**
     * This method is used to create audience reach report using Google Analytics.
     *
     * @param reportRequestDto reportRequestDto
     * @param userId           userId/ALL
     * @param page             page
     * @param size             size
     * @param timeZone         timeZone
     * @param roleType         merchant/bank
     * @return list of business profile views
     */
    public List<BusinessProfileViewsResponseDto> generateAudienceReachReport(ReportRequestDto reportRequestDto,
                                                                             String userId, int page, int size,
                                                                             String timeZone, RoleType roleType) {
        try {
            if (!userId.equals("ALL")) {
                merchantService.validateBusinessProfileWithMerchantId(userId, roleType);
            }
            List<BusinessProfileViews> businessProfileViewsList =
                    getAudienceReachList(reportRequestDto, userId, page, size, timeZone, roleType);
            return createAudienceReachResponse(roleType, businessProfileViewsList);
        } catch (DataAccessException e) {
            throw new AuthServiceException("Generating audience reach report was failed.", e);
        }
    }

    /**
     * THis method creates audience reach response according to the user role.
     *
     * @param roleType                 merchant/bank
     * @param businessProfileViewsList businessProfileViewsList
     * @return list of business profile views
     */
    private List<BusinessProfileViewsResponseDto> createAudienceReachResponse
    (RoleType roleType, List<BusinessProfileViews> businessProfileViewsList) {

        List<BusinessProfileViewsResponseDto> businessProfileViewsDtoList = new ArrayList<>();
        if (roleType.equals(RoleType.MERCHANT)) {
            for (BusinessProfileViews businessProfileViews : businessProfileViewsList) {
                Optional<ApprovedBusiness> approvedBusiness =
                        approvedBusinessRepository.findByMerchantId(businessProfileViews.getUserId());
                if (approvedBusiness.isPresent()) {
                    var businessProfileViewsResponse = new DisplayDateResponseDto(businessProfileViews.getViewCount(),
                            businessProfileViews.getDisplayDate(), approvedBusiness.get().getBusiness());
                    businessProfileViewsDtoList.add(businessProfileViewsResponse);
                }
            }
        } else {
            for (BusinessProfileViews businessProfileViews : businessProfileViewsList) {
                Optional<ApprovedBankBusiness> approvedBankBusiness =
                        approvedBankBusinessRepository.findByBankId(businessProfileViews.getUserId());
                if (approvedBankBusiness.isPresent()) {
                    var businessProfileViewsResponse = new DisplayDateResponseDto(businessProfileViews.getViewCount(),
                            businessProfileViews.getDisplayDate(), approvedBankBusiness.get().getBankBusiness());
                    businessProfileViewsDtoList.add(businessProfileViewsResponse);
                }
            }
        }
        return businessProfileViewsDtoList;
    }

    /**
     * Query to get merchant views for date option.
     *
     * @param reportRequestDto reportRequestDto
     * @param userId           userId
     * @param page             page
     * @param size             size
     * @param timeZone         timeZone
     * @param roleType         merchant/bank
     * @return profile view list.
     */
    public List<BusinessProfileViews> getAudienceReachList(ReportRequestDto reportRequestDto, String userId, int page,
                                                           int size, String timeZone, RoleType roleType) {

        ReportDateOption dateOption = reportRequestDto.getOption();
        long startDate = reportRequestDto.getStartDate();
        long endDate = reportRequestDto.getEndDate();
        AnalyticsDimensionsAndMetrics userRoleDimension = roleType.equals(RoleType.MERCHANT) ?
                AnalyticsDimensionsAndMetrics.MERCHANT_ID_DIMENSION : AnalyticsDimensionsAndMetrics.BANK_ID_DIMENSION;
        var request = RunReportRequest.newBuilder()
                .setProperty(PROPERTY + propertyId)
                .addDateRanges(DateRange.newBuilder()
                        .setStartDate(dateFormatter(startDate))
                        .setEndDate(dateFormatter(endDate)))
                .addDimensions(Dimension.newBuilder().setName(userRoleDimension.getAnalyticsDimension()))
                .addDimensions(Dimension.newBuilder().setName(dateOption.getAnalyticsOption()))
                .addMetrics(Metric.newBuilder().setName(
                        AnalyticsDimensionsAndMetrics.EVENT_COUNT_METRIC.getAnalyticsDimension()))
                .setLimit(size)
                .setOffset(page);

        if (!userId.equals(ALL)) {
            var filter = FilterExpression.newBuilder()
                    .setFilter(Filter.newBuilder()
                            .setFieldName(userRoleDimension.getAnalyticsDimension())
                            .setStringFilter(Filter.StringFilter.newBuilder().setValue(userId)));
            request.setDimensionFilter(filter);
        }
        RunReportResponse response = analyticsData.runReport(request.build());

        List<BusinessProfileViews> businessProfileViewsList = new ArrayList<>();
        for (Row row : response.getRowsList()) {
            businessProfileViewsList.add(
                    new BusinessProfileViews(row.getDimensionValues(ZERO_ELEMENT).getValue(),
                            Long.parseLong(row.getMetricValues(ZERO_ELEMENT).getValue()),
                            row.getDimensionValues(FIRST_ELEMENT).getValue(), dateOption, startDate, endDate, timeZone));
        }
        return businessProfileViewsList;
    }

    /**
     * This method is used to format date to support Google Analytics.
     *
     * @param timeStamp timeStamp
     * @return date in string
     */
    private String dateFormatter(long timeStamp) {
        Date date = new Date(timeStamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ANALYTICS_DATE_FORMAT);
        return simpleDateFormat.format(date);
    }

    /**
     * This method is used to create top 10 audience reach report using Google Analytics.
     *
     * @param graphDateOption graphDateOption
     * @param timeZone        timeZone
     * @return list of business profile views
     */
    public List<BusinessProfileViewsResponseDto> getTopTenViewCounts(GraphDateOption graphDateOption, String timeZone,
                                                                     RoleType roleType) {
        try {
            List<BusinessProfileViews> businessProfileViewsList =
                    getTopTenAudienceReachList(graphDateOption, timeZone, roleType);
            List<BusinessProfileViewsResponseDto> businessProfileViewsDtoList = new ArrayList<>();
            if (roleType.equals(RoleType.MERCHANT)) {
                for (BusinessProfileViews businessProfileViews : businessProfileViewsList) {
                    Optional<ApprovedBusiness> approvedBusiness =
                            approvedBusinessRepository.findByMerchantId(businessProfileViews.getUserId());
                    if (approvedBusiness.isPresent()) {
                        var businessProfileViewsDto =
                                new BusinessProfileViewsResponseDto(businessProfileViews.getViewCount(),
                                        approvedBusiness.get().getBusiness());
                        businessProfileViewsDtoList.add(businessProfileViewsDto);
                    }
                }
            } else {
                for (BusinessProfileViews businessProfileViews : businessProfileViewsList) {
                    Optional<ApprovedBankBusiness> approvedBankBusiness =
                            approvedBankBusinessRepository.findByBankId(businessProfileViews.getUserId());
                    if (approvedBankBusiness.isPresent()) {
                        var businessProfileViewsDto =
                                new BusinessProfileViewsResponseDto(businessProfileViews.getViewCount(),
                                        approvedBankBusiness.get().getBankBusiness());
                        businessProfileViewsDtoList.add(businessProfileViewsDto);
                    }
                }
            }
            return businessProfileViewsDtoList;
        } catch (DataAccessException e) {
            throw new AuthServiceException("Generating top 10 audience reach report was failed.", e);
        }
    }

    /**
     * Query to get top 10 merchant views.
     *
     * @param graphDateOption graphDateOption
     * @param timeZone        timeZone
     * @return profile view list.
     */
    public List<BusinessProfileViews> getTopTenAudienceReachList(GraphDateOption graphDateOption, String timeZone,
                                                                 RoleType roleType) {

        String startDate = new DateRangeConverter(timeZone, graphDateOption).getStartDate();
        String endDate = new DateRangeConverter(timeZone, graphDateOption).getEndDate();
        AnalyticsDimensionsAndMetrics profileDimension = roleType.equals(RoleType.MERCHANT) ?
                AnalyticsDimensionsAndMetrics.MERCHANT_ID_DIMENSION : AnalyticsDimensionsAndMetrics.BANK_ID_DIMENSION;
        var orderBy = OrderBy.newBuilder()
                .setDesc(true)
                .setMetric(OrderBy.MetricOrderBy.newBuilder()
                        .setMetricName(AnalyticsDimensionsAndMetrics.EVENT_COUNT_METRIC.getAnalyticsDimension()));

        RunReportRequest request = RunReportRequest.newBuilder()
                .setProperty(PROPERTY + propertyId)
                .addDateRanges(DateRange.newBuilder().setStartDate(startDate).setEndDate(endDate))
                .addDimensions(Dimension.newBuilder().setName(profileDimension.getAnalyticsDimension()))
                .addMetrics(Metric.newBuilder()
                        .setName(AnalyticsDimensionsAndMetrics.EVENT_COUNT_METRIC.getAnalyticsDimension()))
                .addOrderBys(orderBy)
                .setLimit(pageSize)
                .build();

        RunReportResponse response = analyticsData.runReport(request);
        return getViewList(response);
    }

    /**
     * This method is used to get audience reach report for date range.
     *
     * @param startDate start date
     * @param endDate   end date
     * @param page      page
     * @param size      size
     * @return merchant list with view counts.
     */
    public List<GroupedProfileViewsResponseDto> getReportForDateRange(long startDate, long endDate, String userId,
                                                                      int page, int size, RoleType roleType) {
        try {
            String formattedStartDate = dateFormatter(startDate);
            String formattedEndDate = dateFormatter(endDate);
            List<BusinessProfileViews> businessProfileViewsList =
                    getGroupedAudienceReachList(formattedStartDate, formattedEndDate, userId, page, size, roleType);
            List<GroupedProfileViewsResponseDto> groupedProfileViewsResponseDtoList = new ArrayList<>();

            if (roleType.equals(RoleType.MERCHANT)) {
                for (BusinessProfileViews businessProfileViews : businessProfileViewsList) {
                    Optional<ApprovedBusiness> approvedBusiness =
                            approvedBusinessRepository.findByMerchantId(businessProfileViews.getUserId());
                    approvedBusiness.ifPresent(value ->
                            groupedProfileViewsResponseDtoList.add(new GroupedProfileViewsResponseDto(
                                    value.getBusiness(), businessProfileViews.getViewCount())));
                }
            } else {
                for (BusinessProfileViews businessProfileViews : businessProfileViewsList) {
                    Optional<ApprovedBankBusiness> approvedBankBusiness =
                            approvedBankBusinessRepository.findByBankId(businessProfileViews.getUserId());
                    approvedBankBusiness.ifPresent(value ->
                            groupedProfileViewsResponseDtoList.add(new GroupedProfileViewsResponseDto(
                                    value.getBankBusiness(), businessProfileViews.getViewCount())));
                }
            }
            return groupedProfileViewsResponseDtoList;
        } catch (DataAccessException e) {
            throw new AuthServiceException("Generating report for date range was failed.", e);
        }
    }

    /**
     * Query to get merchant views in date range.
     *
     * @param startDate startDate
     * @param endDate   endDate
     * @param page      page
     * @param size      size
     * @return profile view list.
     */
    public List<BusinessProfileViews> getGroupedAudienceReachList(String startDate, String endDate, String userId,
                                                                  int page, int size, RoleType roleType) {

        AnalyticsDimensionsAndMetrics userRoleDimension = roleType.equals(RoleType.MERCHANT) ?
                AnalyticsDimensionsAndMetrics.MERCHANT_ID_DIMENSION : AnalyticsDimensionsAndMetrics.BANK_ID_DIMENSION;

        var request = RunReportRequest.newBuilder()
                .setProperty(PROPERTY + propertyId)
                .addDateRanges(DateRange.newBuilder().setStartDate(startDate).setEndDate(endDate))
                .addDimensions(Dimension.newBuilder()
                        .setName(userRoleDimension.getAnalyticsDimension()))
                .addMetrics(Metric.newBuilder()
                        .setName(AnalyticsDimensionsAndMetrics.EVENT_COUNT_METRIC.getAnalyticsDimension()))
                .setLimit(size)
                .setOffset(page);

        if (!userId.equals(ALL)) {
            var filter = FilterExpression.newBuilder()
                    .setFilter(Filter.newBuilder()
                            .setFieldName(userRoleDimension.getAnalyticsDimension())
                            .setStringFilter(Filter.StringFilter.newBuilder().setValue(userId)));
            request.setDimensionFilter(filter);
        }

        RunReportResponse response = analyticsData.runReport(request.build());
        return getViewList(response);
    }

    /**
     * This method will add analytics response into local list.
     *
     * @param response analytics response
     * @return profile view list.
     */
    private List<BusinessProfileViews> getViewList(RunReportResponse response) {
        List<BusinessProfileViews> businessProfileViewsList = new ArrayList<>();
        for (Row row : response.getRowsList()) {
            businessProfileViewsList.add(new BusinessProfileViews(row.getDimensionValues(ZERO_ELEMENT).getValue(),
                    Long.parseLong(row.getMetricValues(ZERO_ELEMENT).getValue())));
        }
        return businessProfileViewsList;
    }
}
