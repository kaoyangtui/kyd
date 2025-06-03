package com.pig4cloud.pigx.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.eventMeeting.EventMeetingPageRequest;
import com.pig4cloud.pigx.admin.dto.eventMeeting.EventMeetingResponse;
import com.pig4cloud.pigx.admin.dto.pc.NewsResponse;
import com.pig4cloud.pigx.admin.dto.pc.PortalStatisticResponse;
import com.pig4cloud.pigx.admin.dto.researchNews.ResearchNewsPageRequest;
import com.pig4cloud.pigx.admin.dto.researchNews.ResearchNewsResponse;
import com.pig4cloud.pigx.admin.dto.transformCase.TransformCasePageRequest;
import com.pig4cloud.pigx.admin.dto.transformCase.TransformCaseResponse;
import com.pig4cloud.pigx.admin.service.*;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhaoliang
 */
@RestController
@RequestMapping("/pc")
@Tag(description = "PC平台", name = "PC平台")
@RequiredArgsConstructor
public class PcController {
    private final PortalStatisticService portalStatisticService;
    private final ResearchNewsService researchNewsService;
    private final TransformCaseService transformCaseService;
    private final EventMeetingService eventMeetingService;


    @GetMapping("/portal/statistic")
    @Operation(summary = "获取门户统计数量")
    public R<PortalStatisticResponse> getPortalStatistic() {
        return R.ok(portalStatisticService.getPortalStatistic());
    }


    @GetMapping("/news")
    @Operation(summary = "资讯动态")
    public R<List<NewsResponse>> newsPage() {
        IPage<ResearchNewsResponse> newsResponsePage = researchNewsService.pageResult(new Page(1, 10), null);
        IPage<TransformCaseResponse> transformCaseResponsePage = transformCaseService.pageResult(new Page(1, 10), null);
        IPage<EventMeetingResponse> eventMeetingResponsePage = eventMeetingService.pageResult(new Page(1, 10), null);
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
}
