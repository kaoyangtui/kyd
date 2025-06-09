package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pig4cloud.pigx.admin.dto.assetNews.AssetNewsResponse;
import com.pig4cloud.pigx.admin.dto.assetPolicy.AssetPolicyResponse;
import com.pig4cloud.pigx.admin.dto.demand.DemandResponse;
import com.pig4cloud.pigx.admin.dto.demandIn.DemandInResponse;
import com.pig4cloud.pigx.admin.dto.eventMeeting.EventMeetingResponse;
import com.pig4cloud.pigx.admin.dto.ipTransform.IpTransformResponse;
import com.pig4cloud.pigx.admin.dto.patent.PatentDetailResponse;
import com.pig4cloud.pigx.admin.dto.patent.PatentInfoResponse;
import com.pig4cloud.pigx.admin.dto.pc.PortalStatisticResponse;
import com.pig4cloud.pigx.admin.dto.researchNews.ResearchNewsResponse;
import com.pig4cloud.pigx.admin.dto.researchTeam.ResearchTeamResponse;
import com.pig4cloud.pigx.admin.dto.result.ResultResponse;
import com.pig4cloud.pigx.admin.dto.transformCase.TransformCaseResponse;
import com.pig4cloud.pigx.admin.entity.*;
import com.pig4cloud.pigx.admin.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PcServiceImpl implements PcService {

    private final PatentInfoService patentInfoService;
    private final ResultService resultService;
    private final DemandService demandService;
    private final ExpertService expertService;
    private final AssetNewsService assetNewsService;
    private final AssetPolicyService assetPolicyService;
    private final DemandInService demandInService;
    private final EventMeetingService eventMeetingService;
    private final IpTransformService ipTransformService;
    private final ResearchNewsService researchNewsService;
    private final ResearchTeamService researchTeamService;
    private final TransformCaseService transformCaseService;

    @Override
    public PortalStatisticResponse getPortalStatistic() {
        PortalStatisticResponse resp = new PortalStatisticResponse();
        resp.setPatentCount(
                ObjectUtil.defaultIfNull(
                        patentInfoService.count(new LambdaQueryWrapper<PatentInfoEntity>().eq(PatentInfoEntity::getDelFlag, "0")),
                        0L
                )
        );
        resp.setResultCount(
                ObjectUtil.defaultIfNull(
                        resultService.count(new LambdaQueryWrapper<ResultEntity>().eq(ResultEntity::getShelfStatus, 1)),
                        0L
                )
        );
        resp.setDemandCount(
                ObjectUtil.defaultIfNull(
                        demandService.count(new LambdaQueryWrapper<DemandEntity>().eq(DemandEntity::getDelFlag, "0")),
                        0L
                )
        );
        resp.setExpertCount(
                ObjectUtil.defaultIfNull(
                        expertService.count(new LambdaQueryWrapper<ExpertEntity>().eq(ExpertEntity::getShelfStatus, 1)),
                        0L
                )
        );
        return resp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean increaseViewCount(String bizCode, Long bizId) {
        switch (bizCode) {
            case AssetNewsResponse.BIZ_CODE:
                assetNewsService.lambdaUpdate()
                        .eq(AssetNewsEntity::getId, bizId)
                        .setSql("view_count = ifnull(view_count,0) + 1")
                        .update();
                break;
            case AssetPolicyResponse.BIZ_CODE:
                assetPolicyService.lambdaUpdate()
                        .eq(AssetPolicyEntity::getId, bizId)
                        .setSql("view_count = ifnull(view_count,0) + 1")
                        .update();
                break;
            case DemandResponse.BIZ_CODE:
                demandService.lambdaUpdate()
                        .eq(DemandEntity::getId, bizId)
                        .setSql("view_count = ifnull(view_count,0) + 1")
                        .update();
                break;
            case DemandInResponse.BIZ_CODE:
                demandInService.lambdaUpdate()
                        .eq(DemandInEntity::getId, bizId)
                        .setSql("view_count = ifnull(view_count,0) + 1")
                        .update();
                break;
            case EventMeetingResponse.BIZ_CODE:
                eventMeetingService.lambdaUpdate()
                        .eq(EventMeetingEntity::getId, bizId)
                        .setSql("view_count = ifnull(view_count,0) + 1")
                        .update();
                break;
            case IpTransformResponse.BIZ_CODE:
                ipTransformService.lambdaUpdate()
                        .eq(IpTransformEntity::getId, bizId)
                        .setSql("view_count = ifnull(view_count,0) + 1")
                        .update();
                break;
            case PatentInfoResponse.BIZ_CODE:
                patentInfoService.lambdaUpdate()
                        .eq(PatentInfoEntity::getId, bizId)
                        .setSql("view_count = ifnull(view_count,0) + 1")
                        .update();
                break;
            case ResearchNewsResponse.BIZ_CODE:
                researchNewsService.lambdaUpdate()
                        .eq(ResearchNewsEntity::getId, bizId)
                        .setSql("view_count = ifnull(view_count,0) + 1")
                        .update();
                break;
            case ResearchTeamResponse.BIZ_CODE:
                researchTeamService.lambdaUpdate()
                        .eq(ResearchTeamEntity::getId, bizId)
                        .setSql("view_count = ifnull(view_count,0) + 1")
                        .update();
                break;
            case ResultResponse.BIZ_CODE:
                resultService.lambdaUpdate()
                        .eq(ResultEntity::getId, bizId)
                        .setSql("view_count = ifnull(view_count,0) + 1")
                        .update();
                break;
            case TransformCaseResponse.BIZ_CODE:
                transformCaseService.lambdaUpdate()
                        .eq(TransformCaseEntity::getId, bizId)
                        .setSql("view_count = ifnull(view_count,0) + 1")
                        .update();
                break;
            default:
                throw new IllegalArgumentException("不支持的 BIZ_CODE: " + bizCode);
        }
        return Boolean.TRUE;
    }
}
