package com.pig4cloud.pigx.admin.constants;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author zhaoliang
 */
public interface DiPatentConstants {
    /**
     * 公开的法律状态
     */
    List<String> PUBLICATION_STATUS = Lists.newArrayList("公开");
    /**
     * 实审的法律状态
     */
    List<String> EXAMINATION_STATUS = Lists.newArrayList("实质审查的生效", "实质审查请求的生效");
    /**
     * 授权的法律状态
     */
    List<String> AUTHORIZED_STATUS = Lists.newArrayList("授权");
    /**
     * 未授权的法律状态
     */
    List<String> UNAUTHORIZED_STATUS = Lists.newArrayList("发明专利申请公布后的撤回", "发明专利申请公布后的驳回", "发明专利申请公布后的视为撤回", "专利申请的视为撤回", "专利申请的驳回", "被视为撤回的申请", "被视为撤回的申请视为放弃取得专利权的权利", "驳回的专利申请", "视为撤回的专利申请", "撤回的专利申请", "其他有关事项避免重复授权放弃专利权", "发明专利申请公布后的撤回");
    /**
     * 无效的法律状态
     */
    List<String> UNEFFECTIVE_STATUS = Lists.newArrayList("未缴年费专利权终止",
            "专利权有效期届满",
            "避免重复授权放弃专利权",
            "专利权的主动放弃",
            "避免重复授予专利权",
            "专利权全部无效",
            "专利权的视为放弃",
            "专利权的终止未缴年费专利权终止",
            "其他有关事项(避免重复授权放弃专利权)",
            "专利权的终止(未缴年费专利权终止)",
            "专利权的终止专利权的主动放弃",
            "专利权的无效、部分无效宣告专利权全部无效",
            "专利权的终止专利权有效期届满",
            "专利权的终止未缴纳年费专利权终止",
            "专利权的终止",
            "专利权的终止(专利权有效期届满)",
            "专利权的终止(①未缴年费专利权终止)",
            "通知事项视为放弃专利权",
            "专利权的终止(②专利权有效期届满)",
            "专利权的无效宣告专利权全部无效",
            "其他有关事项专利权全部撤销",
            "专利权的主动放弃;其他有关事项避免重复授权放弃专利权",
            "专利权的无效宣告(专利权全部无效)",
            "专利权的撤销专利权全部撤销");
    String CLIENTVALUE = "fa7c5f290a0a01097ae5a366533ef455";
    String CLIENT = "client_id";
    String SCOPECODE = "read_cn";
    String SCOPE = "scope";
    int ONE = 1;
    int ZERO = 0;
    String AD = "ad";
    String PAGE = "page";
    String PAGE_ROW_KEY = "page_row";
    int PAGE_ROW_VALUE = 10;
    String CONTEXT = "context";
    String TIME = "yyyyMMdd";
    String RECORDS = "records";
    String ACCESS_TOKEN_KEY = "access_token";
    String TOTAL = "total";
    String IMGTITLE = "IMGTITLE";
    String IMGNAME = "IMGNAME";
    String IMGO = "IMGO";
    String EXPRESS = "express";
    String WHOILE = "ASPCLC!=大专院校  AND AP1A=中国 AND PD>= {}  AND PD< {}";
    String PNO = "PNO={}";
    String SORTKEY = "sort_column";
    String IMGLIST = "imgList";
    String SORTVALUE = "+PID";
    String PID = "pid";
    String LANG = "lang";
    String CHINA = "o";
    String ASC = "asc";
    String LIMIT = "limit 1";
    String ERRORCODE = "errorCode";
    String PARMS = "000000";
    /**
     * 错误代码[9999] ==> 超出接口调用次数限制
     */
    String EXCEED_MAX_REQUEST = "9999";
    String CATALOGPATENT = "catalogPatent";
    //文件上传路径
    //内网访问地址
    String FILEURL = "http://patent.resource.cnuip.com";
    //外网读
    String OCR = "https://patent-year-fee.oss-cn-shanghai.aliyuncs.com";
    //内网写
    String OCRREAD = "patent-year-fee.oss-cn-shanghai-internal.aliyuncs.com";
    //外网访问地址
    String FILEURLS = "oss-cn-shanghai.aliyuncs.com";
    String GIF = ".gif";
    String PNG = ".png";
    String PDF = ".pdf";
    String IMGOS = "imgo/";
    String SUDA = "suda/";
    String IMAGE = "image/gif";
    String APIADDRESS = "ocr-api.cn-hangzhou.aliyuncs.com";
    String EXACTSEARCH = "exactSearch";

    String SUZHOUExp = "((APC={} or APE ={} or APO={} or APO={} OR ASP={} OR ASP={})AND PD>= {} AND PD<{})";

    String SCHOOLEXP = "((APO={} OR ASPO={})AND PD>= {} AND PD<{})";
    String Exppst = "((APC='{}'  or APO='{}' or  ASP='{}' )AND PD>= {} AND PD<{})";
    String EXP_ASPCLC = "((ASPCLC='大专院校' AND LSSC=1 )AND PD>= {} AND PD<{})";
    String EXPPST_NOTIME = "(APC= {}  or APO= {} or  ASP= {})";

//    AND TS=是 AND ILSIO!='著录事项变更'

    String Expp = "(ASP={} OR ASP={}) AND PD>={} AND PD<{}";

    String Expps = "(ASP={} OR ASP={}) AND PD >{} AND PD <={}";
    String SEARCH = "( ANO = ( {} ))";
    String NJNYDXVALUE = "南京农业大学";
    String SZDX = "苏州大学";
    String MINING = "中国矿业大学";
    String SZDXENGLISH = "soochow university";
    String MININGENGLISH = "China University of Mining and Technology";

    String TIMEYEAR = "yyyy/MM/dd HH:mm:ss";
    String PNS = "pns";

    Integer PAGE_MAX_NUM = 10000;
}
