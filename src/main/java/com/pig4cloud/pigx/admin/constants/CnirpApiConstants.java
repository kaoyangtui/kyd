package com.pig4cloud.pigx.admin.constants;

/**
 * 专利属性字段常量类
 *
 * @author zhaoliang
 */
public interface CnirpApiConstants {

    String API_URL = "https://open.cnipr.com";
    /**
     * 使用 Password Grant 方式获取 Access Token
     */
    String LOGIN = API_URL + "/oauth/json/user/login";

    /**
     * sf1-v1，专利概览检索接口
     */
    String GET_PATENT_LIST = API_URL + "/cnipr-api/v1/api/search/sf1/{client_id}";
}
