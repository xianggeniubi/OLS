package com.jx.common.util;

import com.jx.common.util.data.ResultHead;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class RestUtil {
    public static Log logger = LogFactory.getLog(RestUtil.class);
    private static String SUCCESS_CODE = "0000";
    private static String SUCCESS_MSG = "处理成功";
    public static String ERROR_INTERNAL_CODE = "99";
    public static String ERROR_INTERNAL_MSG = "网络通讯异常";
    public static String ERROR_UNKNOW_CODE = "98";
    public static String ERROR_UNKNOW_MSG = "未知错误";
    @Autowired
    private RestTemplate restTemplate;
    private static RestUtil restUtil;

    public RestUtil() {

    }

    @PostConstruct
    public void init() {
        restUtil = this;
        restUtil.restTemplate = this.restTemplate;
    }

    public static Map<String, Object> success() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("head", new ResultHead(SUCCESS_CODE, SUCCESS_MSG));
        return resultMap;
    }

    public static Map<String, Object> success(Object result) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("head", new ResultHead(SUCCESS_CODE, SUCCESS_MSG));
        resultMap.put("body", result);
        return resultMap;
    }

    public static Map<String, Object> fail(String reFlag, String retMsg, Object body) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("head", new ResultHead(reFlag, retMsg));
        if (body != null) {
            resultMap.put("body", body);
        }
        return resultMap;
    }

    public static boolean isSuccess(Map<String, Object> resultMap) {
        return HttpUtil.isSuccess(resultMap);
    }

    public static String restPostString(String url, String data, int responseCode) {
        return null;
    }

    public static String getString(JSONObject jsonObject, String key) {
        return jsonObject != null && jsonObject.has(key) ? String.valueOf(jsonObject.get(key)) : null;
    }

    public static JSONObject getObject(JSONObject jsonObject, String key) {
        return jsonObject != null && jsonObject.has(key) ? jsonObject.getJSONObject(key) : null;
    }

    public static String getString(Map<String, Object> map, String key) {
        return map != null ? String.valueOf(map.get(key)) : null;
    }

    public static Map<String, Object> getMap(Map<String, Object> map, String key) {
        return map != null ? (Map) map.get(key) : null;
    }

    public static String getGuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String getSerial() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis() + String.valueOf((int) Math.random() * 1000.0D);
    }

    public static enum Type {
        ERROR_REQUEST(9001, "请求参数异常"),
        ERROR_VALIDATION(9002, "请求非法"),
        ERROR_INTERNAL(9003, "网络通讯异常"),
        ERROR_BUSINESS(9004, "业务校验异常");

        private int code;
        private String message;

        private Type(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return this.code;
        }

        public String getMessage() {
            return this.message;
        }
    }
}
