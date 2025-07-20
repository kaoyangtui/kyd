package com.pig4cloud.pigx.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.PageRequest;
import com.pig4cloud.pigx.admin.dto.demand.DemandCreateRequest;
import com.pig4cloud.pigx.admin.dto.demand.DemandPageRequest;
import com.pig4cloud.pigx.admin.dto.demand.DemandResponse;
import com.pig4cloud.pigx.admin.dto.demandIn.DemandInPageRequest;
import com.pig4cloud.pigx.admin.dto.demandIn.DemandInResponse;
import com.pig4cloud.pigx.admin.dto.eventMeeting.EventMeetingPageRequest;
import com.pig4cloud.pigx.admin.dto.eventMeeting.EventMeetingResponse;
import com.pig4cloud.pigx.admin.dto.expert.ExpertPageRequest;
import com.pig4cloud.pigx.admin.dto.expert.ExpertResponse;
import com.pig4cloud.pigx.admin.dto.patent.PatentSearchRequest;
import com.pig4cloud.pigx.admin.dto.patent.PatentSearchResponse;
import com.pig4cloud.pigx.admin.dto.pc.NewsResponse;
import com.pig4cloud.pigx.admin.dto.pc.PortalStatisticResponse;
import com.pig4cloud.pigx.admin.dto.pc.ViewCountIncreaseRequest;
import com.pig4cloud.pigx.admin.dto.researchNews.ResearchNewsPageRequest;
import com.pig4cloud.pigx.admin.dto.researchNews.ResearchNewsResponse;
import com.pig4cloud.pigx.admin.dto.researchPlatform.ResearchPlatformPageRequest;
import com.pig4cloud.pigx.admin.dto.researchPlatform.ResearchPlatformResponse;
import com.pig4cloud.pigx.admin.dto.researchTeam.ResearchTeamPageRequest;
import com.pig4cloud.pigx.admin.dto.researchTeam.ResearchTeamResponse;
import com.pig4cloud.pigx.admin.dto.result.ResultPageRequest;
import com.pig4cloud.pigx.admin.dto.result.ResultResponse;
import com.pig4cloud.pigx.admin.dto.transformCase.TransformCasePageRequest;
import com.pig4cloud.pigx.admin.dto.transformCase.TransformCaseResponse;
import com.pig4cloud.pigx.admin.entity.WebFooterInfoEntity;
import com.pig4cloud.pigx.admin.service.*;
import com.pig4cloud.pigx.admin.utils.PageUtil;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhaoliang
 */
@RestController
@RequestMapping("/pc")
@Tag(description = "PC平台", name = "PC平台")
@RequiredArgsConstructor
public class PcController {
    private final PcService pcService;
    private final ResearchNewsService researchNewsService;
    private final TransformCaseService transformCaseService;
    private final EventMeetingService eventMeetingService;
    private final PatentInfoService patentInfoService;
    private final ResultService resultService;
    private final DemandService demandService;
    private final DemandInService demandInService;
    private final ExpertService expertService;
    private final ResearchPlatformService researchPlatformService;
    private final ResearchTeamService researchTeamService;
    private final WebFooterInfoService webFooterInfoService;


    @GetMapping("/portal/statistic")
    @Operation(summary = "获取门户统计数量")
    public R<PortalStatisticResponse> portalStatistic() {
        return R.ok(pcService.getPortalStatistic());
    }


    @GetMapping("/news")
    @Operation(summary = "资讯动态")
    public R<List<NewsResponse>> news() {
        IPage<ResearchNewsResponse> newsResponsePage = researchNewsService.pageResult(new Page(1, 10), new ResearchNewsPageRequest());
        IPage<TransformCaseResponse> transformCaseResponsePage = transformCaseService.pageResult(new Page(1, 10), new TransformCasePageRequest());
        IPage<EventMeetingResponse> eventMeetingResponsePage = eventMeetingService.pageResult(new Page(1, 10), new EventMeetingPageRequest());
        List<NewsResponse> newsList = CollUtil.newArrayList();

        // 转换每种数据为 NewsResponse 并添加 sourceType
        if (CollUtil.isNotEmpty(newsResponsePage.getRecords())) {
            newsResponsePage.getRecords().forEach(item -> {
                NewsResponse news = BeanUtil.copyProperties(item, NewsResponse.class);
                news.setSourceType(ResearchNewsResponse.BIZ_CODE);
                newsList.add(news);
            });
        }
        if (CollUtil.isNotEmpty(transformCaseResponsePage.getRecords())) {
            transformCaseResponsePage.getRecords().forEach(item -> {
                NewsResponse news = BeanUtil.copyProperties(item, NewsResponse.class);
                news.setSourceType(TransformCaseResponse.BIZ_CODE);
                newsList.add(news);
            });
        }
        if (CollUtil.isNotEmpty(eventMeetingResponsePage.getRecords())) {
            eventMeetingResponsePage.getRecords().forEach(item -> {
                NewsResponse news = BeanUtil.copyProperties(item, NewsResponse.class);
                news.setSourceType(EventMeetingResponse.BIZ_CODE);
                newsList.add(news);
            });
        }

        // 按 createTime 字段倒序排列
        newsList.sort((o1, o2) -> DateUtil.parse(o2.getCreateTime()).compareTo(DateUtil.parse(o1.getCreateTime())));

        // 只返回前10条
        List<NewsResponse> result = newsList.size() > 10 ? newsList.subList(0, 10) : newsList;

        return R.ok(result);
    }

    @GetMapping("/result")
    @Operation(summary = "科研成果查询")
    public R<IPage<ResultResponse>> result(@ParameterObject PageRequest pageRequest,
                                           @ParameterObject ResultPageRequest request) {
        if (CollUtil.isEmpty(pageRequest.getOrders())) {
            List<OrderItem> orders = CollUtil.newArrayList();
            OrderItem orderItem = new OrderItem();
            orderItem.setColumn("shelf_time");
            orderItem.setAsc(false);
            orders.add(orderItem);
            pageRequest.setOrders(orders);
        }
        request.setShelfStatus(1);
        return R.ok(resultService.pageResult(PageUtil.toPage(pageRequest), request));
    }


    @GetMapping("/patent/keyword")
    @Operation(summary = "专利查询")
    public R<IPage<PatentSearchResponse>> patentKeyword(
            @ParameterObject PageRequest pageRequest,
            @ParameterObject PatentSearchRequest request) {
        return R.ok(patentInfoService.searchPatent(PageUtil.toPage(pageRequest), request));
    }

    @GetMapping("/demand")
    @Operation(summary = "企业技术需求查询")
    public R<IPage<DemandResponse>> demand(@ParameterObject PageRequest pageRequest,
                                           @ParameterObject DemandPageRequest request) {
        List<OrderItem> orders = CollUtil.newArrayList();
        OrderItem orderItem = new OrderItem();
        orderItem.setColumn("shelf_time");
        orderItem.setAsc(false);
        orders.add(orderItem);
        pageRequest.setOrders(orders);
        request.setShelfStatus(1);
        request.setCategory(1);
        return R.ok(demandService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @PostMapping("/demand/create")
    @Operation(summary = "需求发布")
    public R<Boolean> demandCreate(@RequestBody @Valid DemandCreateRequest request) {
        request.setCategory(1);
        return R.ok(demandService.create(request));
    }


    @GetMapping("/demandIn")
    @Operation(summary = "校内科研需求")
    public R<IPage<DemandInResponse>> demandIn(@ParameterObject PageRequest pageRequest,
                                               @ParameterObject DemandInPageRequest request) {
        List<OrderItem> orders = CollUtil.newArrayList();
        OrderItem orderItem = new OrderItem();
        orderItem.setColumn("shelf_time");
        orderItem.setAsc(false);
        orders.add(orderItem);
        pageRequest.setOrders(orders);
        request.setShelfStatus(1);
        return R.ok(demandInService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @GetMapping("/expert")
    @Operation(summary = "专家名片")
    public R<IPage<ExpertResponse>> expert(@ParameterObject PageRequest pageRequest,
                                           @ParameterObject ExpertPageRequest request) {
        List<OrderItem> orders = CollUtil.newArrayList();
        OrderItem orderItem = new OrderItem();
        orderItem.setColumn("shelf_time");
        orderItem.setAsc(false);
        orders.add(orderItem);
        pageRequest.setOrders(orders);
        request.setShelfStatus(1);
        return R.ok(expertService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @GetMapping("/researchPlatform")
    @Operation(summary = "科研平台")
    public R<IPage<ResearchPlatformResponse>> page(@ParameterObject PageRequest pageRequest,
                                                   @ParameterObject ResearchPlatformPageRequest request) {
        List<OrderItem> orders = CollUtil.newArrayList();
        OrderItem orderItem = new OrderItem();
        orderItem.setColumn("shelf_time");
        orderItem.setAsc(false);
        orders.add(orderItem);
        pageRequest.setOrders(orders);
        request.setShelfStatus(1);
        return R.ok(researchPlatformService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @GetMapping("/researchTeam")
    @Operation(summary = "科研团队")
    public R<IPage<ResearchTeamResponse>> researchTeam(@ParameterObject PageRequest pageRequest,
                                                       @ParameterObject ResearchTeamPageRequest request) {
        List<OrderItem> orders = CollUtil.newArrayList();
        OrderItem orderItem = new OrderItem();
        orderItem.setColumn("shelf_time");
        orderItem.setAsc(false);
        orders.add(orderItem);
        pageRequest.setOrders(orders);
        request.setShelfStatus(1);
        return R.ok(researchTeamService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @PostMapping("/increaseViewCount")
    @Operation(summary = "增加浏览量")
    public R<Boolean> increaseViewCount(@RequestBody ViewCountIncreaseRequest request) {
        return R.ok(pcService.increaseViewCount(request.getBizCode(), request.getBizId()));
    }

    @Operation(summary = "网站底部信息", description = "网站底部信息")
    @GetMapping("/webFooterInfo")
    public R<WebFooterInfoEntity> getById() {
        WebFooterInfoEntity entity = webFooterInfoService.lambdaQuery()
                .last("limit 1")
                .orderByDesc(WebFooterInfoEntity::getCreateTime)
                .one();
        return R.ok(entity);
    }

}
