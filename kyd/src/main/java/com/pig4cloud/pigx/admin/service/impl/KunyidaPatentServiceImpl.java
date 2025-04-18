package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.admin.constants.CnirpDisplayColsConstants;
import com.pig4cloud.pigx.admin.service.CniprService;
import com.pig4cloud.pigx.admin.service.KunyidaPatentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zhaoliang
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class KunyidaPatentServiceImpl implements KunyidaPatentService {

    private final CniprService cniprService;

    @Override
    public void fetchAllPatents() {
//        String college = "'昆明医科大学' or '昆明医科大学第一附属医院' or '昆明医科大学第二附属医院' or '昆明医科大学第三附属医院'";
        String college = "'辽宁传媒学院'";
        String exp = StrUtil.format("专利权人=({}) or 历史专利权人=({}) or 申请（专利权）人=({})", college, college, college);
        String dbs = "FMZL,FMSQ,SYXX,WGZL,USPATENT,GBPATENT,FRPATENT,DEPATENT,CHPATENT,JPPATENT,RUPATENT,KRPATENT,EPPATENT,WOPATENT";
        //检索类型，默认值：2 （按字检索）其它值含义见附录：https://open.cnipr.com/oauth/doc/appendix#.option
        int option = 2;
        String order = "+appDate";
        String displayCols = CnirpDisplayColsConstants.ALL_FIELDS;
        boolean highLight = false;
        boolean isDbAgg = false;
        cniprService.fetchAllPatents(exp, dbs, option, order, displayCols, highLight, isDbAgg);
    }
}
