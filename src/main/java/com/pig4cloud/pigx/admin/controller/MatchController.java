package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pig4cloud.pigx.admin.dto.PageRequest;
import com.pig4cloud.pigx.admin.dto.demand.DemandResponse;
import com.pig4cloud.pigx.admin.dto.patent.PatentInfoResponse;
import com.pig4cloud.pigx.admin.dto.patent.PatentPageRequest;
import com.pig4cloud.pigx.admin.dto.result.ResultPageRequest;
import com.pig4cloud.pigx.admin.dto.result.ResultResponse;
import com.pig4cloud.pigx.admin.entity.SupplyDemandMatchResultEntity;
import com.pig4cloud.pigx.admin.service.MatchService;
import com.pig4cloud.pigx.admin.service.PatentInfoService;
import com.pig4cloud.pigx.admin.service.ResultService;
import com.pig4cloud.pigx.admin.service.SupplyDemandMatchResultService;
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
                                                                    @RequestParam(name = "demandId") Long demandId) {
        List<SupplyDemandMatchResultEntity> matchResultList = supplyDemandMatchResultService
                .getMatchEntity(DemandResponse.BIZ_CODE, demandId, PatentInfoResponse.BIZ_CODE);

        List<Long> ids = matchResultList.stream()
                .map(SupplyDemandMatchResultEntity::getSupplyId)
                .toList();

        if (ids.isEmpty()) {
            return R.ok(PageUtil.emptyPage(PageUtil.toPage(pageRequest)));
        }

        Map<Long, Integer> scoreMap = buildMatchScoreMap(matchResultList);

        PatentPageRequest request = new PatentPageRequest();
        request.setIds(ids);
        IPage<PatentInfoResponse> page = patentInfoService.pageResult(PageUtil.toPage(pageRequest), request);

        page.getRecords().forEach(item ->
                item.setMatchScore(scoreMap.getOrDefault(item.getId(), 0))
        );

        return R.ok(page);
    }

    @GetMapping("/demand/result")
    @Operation(summary = "查询需求匹配到的成果列表")
    public R<IPage<ResultResponse>> listMatchedResultsForDemand(@ParameterObject PageRequest pageRequest,
                                                                @RequestParam(name = "demandId") Long demandId) {
        List<SupplyDemandMatchResultEntity> matchResultList = supplyDemandMatchResultService
                .getMatchEntity(DemandResponse.BIZ_CODE, demandId, ResultResponse.BIZ_CODE);

        List<Long> ids = matchResultList.stream()
                .map(SupplyDemandMatchResultEntity::getSupplyId)
                .toList();

        if (ids.isEmpty()) {
            return R.ok(PageUtil.emptyPage(PageUtil.toPage(pageRequest)));
        }

        Map<Long, Integer> scoreMap = buildMatchScoreMap(matchResultList);

        ResultPageRequest request = new ResultPageRequest();
        request.setIds(ids);
        IPage<ResultResponse> page = resultService.pageResult(PageUtil.toPage(pageRequest), request);

        page.getRecords().forEach(item ->
                item.setMatchScore(scoreMap.getOrDefault(item.getId(), 0))
        );

        return R.ok(page);
    }

    @PostMapping("/patent")
    @Operation(summary = "专利列表")
    public R<IPage<PatentInfoResponse>> patentPage(@ParameterObject PageRequest pageRequest) {
        List<Long> ids = supplyDemandMatchResultService.getMatchId(PatentInfoResponse.BIZ_CODE);
        PatentPageRequest request = new PatentPageRequest();
        request.setIds(ids);
        return R.ok(patentInfoService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @PostMapping("/result")
    @Operation(summary = "成果列表")
    public R<IPage<ResultResponse>> resultPage(@ParameterObject PageRequest pageRequest) {
        List<Long> ids = supplyDemandMatchResultService.getMatchId(ResultResponse.BIZ_CODE);
        ResultPageRequest request = new ResultPageRequest();
        request.setIds(ids);
        return R.ok(resultService.pageResult(PageUtil.toPage(pageRequest), request));
    }
    /**
     * 构建匹配分数字典
     */
    private Map<Long, Integer> buildMatchScoreMap(List<SupplyDemandMatchResultEntity> matchResultList) {
        return matchResultList.stream()
                .collect(Collectors.toMap(
                        SupplyDemandMatchResultEntity::getSupplyId,
                        SupplyDemandMatchResultEntity::getMatchScore,
                        Integer::max
                ));
    }
}
