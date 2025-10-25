package com.pig4cloud.pigx.admin.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.PageRequest;
import com.pig4cloud.pigx.admin.dto.demand.DemandPageRequest;
import com.pig4cloud.pigx.admin.dto.demand.DemandResponse;
import com.pig4cloud.pigx.admin.dto.patent.PatentInfoResponse;
import com.pig4cloud.pigx.admin.dto.patent.PatentPageRequest;
import com.pig4cloud.pigx.admin.dto.result.ResultPageRequest;
import com.pig4cloud.pigx.admin.dto.result.ResultResponse;
import com.pig4cloud.pigx.admin.entity.SupplyDemandMatchResultEntity;
import com.pig4cloud.pigx.admin.service.*;
import com.pig4cloud.pigx.admin.utils.PageUtil;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 供需匹配管理
 *
 * @author
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/match")
@Tag(name = "供需匹配管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class MatchController {

    private final MatchService matchService;
    private final PatentInfoService patentInfoService;
    private final ResultService resultService;
    private final DemandService demandService;
    private final SupplyDemandMatchResultService supplyDemandMatchResultService;

    @PostMapping("/demand")
    @Operation(summary = "触发企业需求匹配")
    //@PreAuthorize("@pms.hasPermission('demand_add')")
    public R<Boolean> demandMatch() {
        return R.ok(matchService.demandMatch());
    }

    @GetMapping("/demand/patent")
    @Operation(summary = "查询需求匹配到的专利列表")
    public R<IPage<PatentInfoResponse>> listMatchedPatentsForDemand(@ParameterObject PageRequest pageRequest,
                                                                    @RequestParam("demandId") Long demandId) {
        var mpPage = PageUtil.toPage(pageRequest);
        IPage<SupplyDemandMatchResultEntity> matchPage =
                supplyDemandMatchResultService.pageMatchByDemand(
                        DemandResponse.BIZ_CODE, demandId, PatentInfoResponse.BIZ_CODE, mpPage);

        IPage<PatentInfoResponse> page = assembleByMatches(
                matchPage,
                (ids) -> {
                    PatentPageRequest req = new PatentPageRequest();
                    req.setIds(ids);
                    // 这里不要再分页，直接按 ids 取齐（由 ids 数量决定本页大小）
                    var p = new Page<PatentInfoResponse>(1, ids.size());
                    return patentInfoService.pageResult(p, req);
                },
                (dto, score) -> dto.setMatchScore(score),
                PatentInfoResponse::getId
        );
        return R.ok(page);
    }

    @GetMapping("/patent/demand")
    @Operation(summary = "查询专利匹配到的需求列表")
    public R<IPage<DemandResponse>> listMatchedPatentDemand(@ParameterObject PageRequest pageRequest,
                                                            @RequestParam("patentId") Long patentId) {
        var mpPage = PageUtil.toPage(pageRequest);
        IPage<SupplyDemandMatchResultEntity> matchPage =
                supplyDemandMatchResultService.pageMatchBySupply(
                        DemandResponse.BIZ_CODE, patentId, PatentInfoResponse.BIZ_CODE, mpPage);

        IPage<DemandResponse> page = assembleByMatches(
                matchPage,
                (ids) -> {
                    // 如果已有 DemandPageRequest 则用它；没有就写一个只含 ids 的请求 DTO
                    var req = new DemandPageRequest();
                    req.setIds(ids);
                    var p = new Page<DemandResponse>(1, ids.size());
                    return demandService.pageResult(p, req);
                },
                (dto, score) -> dto.setMatchScore(score),
                DemandResponse::getId
        );
        return R.ok(page);
    }


    @GetMapping("/demand/result")
    @Operation(summary = "查询需求匹配到的成果列表")
    public R<IPage<ResultResponse>> listMatchedResultsForDemand(@ParameterObject PageRequest pageRequest,
                                                                @RequestParam("demandId") Long demandId) {
        var mpPage = PageUtil.toPage(pageRequest);
        IPage<SupplyDemandMatchResultEntity> matchPage =
                supplyDemandMatchResultService.pageMatchByDemand(
                        DemandResponse.BIZ_CODE, demandId, ResultResponse.BIZ_CODE, mpPage);

        IPage<ResultResponse> page = assembleByMatches(
                matchPage,
                (ids) -> {
                    ResultPageRequest req = new ResultPageRequest();
                    req.setIds(ids);
                    var p = new Page<ResultResponse>(1, ids.size());
                    return resultService.pageResult(p, req, true);
                },
                (dto, score) -> dto.setMatchScore(score),
                ResultResponse::getId
        );
        return R.ok(page);
    }


    @GetMapping("/result/demand")
    @Operation(summary = "查询成果匹配到的需求列表")
    public R<IPage<DemandResponse>> listMatchedResultDemand(@ParameterObject PageRequest pageRequest,
                                                            @RequestParam("resultId") Long resultId) {
        var mpPage = PageUtil.toPage(pageRequest);
        IPage<SupplyDemandMatchResultEntity> matchPage =
                supplyDemandMatchResultService.pageMatchBySupply(
                        DemandResponse.BIZ_CODE, resultId, ResultResponse.BIZ_CODE, mpPage);

        IPage<DemandResponse> page = assembleByMatches(
                matchPage,
                (ids) -> {
                    var req = new DemandPageRequest();
                    req.setIds(ids);
                    var p = new Page<DemandResponse>(1, ids.size());
                    return demandService.pageResult(p, req);
                },
                (dto, score) -> dto.setMatchScore(score),
                DemandResponse::getId
        );
        return R.ok(page);
    }


    @PostMapping("/patent")
    @Operation(summary = "专利列表")
    public R<IPage<PatentInfoResponse>> patentPage(@ParameterObject PageRequest pageRequest,
                                                   @ParameterObject PatentPageRequest request) {
        if (CollUtil.isEmpty(pageRequest.getOrders())) {
            pageRequest.setOrders(List.of(OrderItem.desc("max_match_score"), OrderItem.desc("id")));
        }
        return R.ok(patentInfoService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @PostMapping("/result")
    @Operation(summary = "成果列表")
    public R<IPage<ResultResponse>> resultPage(@ParameterObject PageRequest pageRequest,
                                               @ParameterObject ResultPageRequest request) {
        if (CollUtil.isEmpty(pageRequest.getOrders())) {
            pageRequest.setOrders(List.of(OrderItem.desc("max_match_score"), OrderItem.desc("id")));
        }
        return R.ok(resultService.pageResult(PageUtil.toPage(pageRequest), request, true));
    }

    private static <T> IPage<T> assembleByMatches(
            IPage<SupplyDemandMatchResultEntity> matchPage,
            Function<List<Long>, IPage<T>> detailPageLoader,
            BiConsumer<T, Integer> scoreSetter,
            Function<T, Long> idGetter) {

        // 1) 先拿到 supplyId 顺序，并去重（保持顺序）
        List<Long> orderedIds = matchPage.getRecords().stream()
                .map(SupplyDemandMatchResultEntity::getSupplyId)
                .filter(java.util.Objects::nonNull)
                .distinct() // 去重，避免同一供给被重复展示
                .toList();

        if (orderedIds.isEmpty()) {
            return new Page<>(matchPage.getCurrent(), matchPage.getSize(), matchPage.getTotal());
        }

        // 2) 分数取最大（已保证）
        Map<Long, Integer> scoreMap = matchPage.getRecords().stream()
                .filter(r -> r.getSupplyId() != null)
                .collect(Collectors.toMap(
                        SupplyDemandMatchResultEntity::getSupplyId,
                        SupplyDemandMatchResultEntity::getMatchScore,
                        Integer::max
                ));

        // 3) 取本页明细（调用方用 ids.size() 作为 pageSize，避免二次分页干扰）
        IPage<T> detailPage = detailPageLoader.apply(orderedIds);

        // 4) 明细映射为 map，key=实体 id
        Map<Long, T> detailMap = detailPage.getRecords().stream()
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toMap(
                        idGetter,
                        x -> x,
                        (a, b) -> a // 避免重复 key 冲突
                ));

        // 5) 按 orderedIds 重排，并回填 matchScore
        List<T> ordered = orderedIds.stream()
                .map(detailMap::get)
                .filter(java.util.Objects::nonNull)
                .peek(t -> {
                    Long id = idGetter.apply(t);
                    scoreSetter.accept(t, scoreMap.getOrDefault(id, 0));
                })
                .toList();

        // 6) 返回与匹配分页元信息一致的 Page，但 records 用我们重排后的
        Page<T> page = new Page<>(matchPage.getCurrent(), matchPage.getSize(), matchPage.getTotal());
        page.setRecords(ordered);
        return page;
    }

}
