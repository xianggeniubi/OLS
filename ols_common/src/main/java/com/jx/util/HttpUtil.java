package com.jx.util;

import com.jx.util.data.ResultHead;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class HttpUtil {
    private static Log logger = LogFactory.getLog(HttpUtil.class);
    @Autowired
    private RestTemplate restTemplate;
    private static HttpUtil httpUtil;

    public HttpUtil() {
    }

    @PostConstruct
    public void init() {
        httpUtil = this;
        httpUtil.restTemplate = this.restTemplate;
    }

    public static HttpHeaders getHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        if (!StringUtils.isEmpty(token)) {
            headers.add("access_token", token);
            headers.add("Authorization", "Bearer" + token);
        }

        return headers;
    }

    public static HttpHeaders getHeaders(String token, Map<String, Object> map) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        if (!StringUtils.isEmpty(token)) {
            headers.add("access_token", token);
            headers.add("Authorization", "Bearer" + token);
        }

        return headers;
    }

    public static String restPost(String url, String token, String data, int responseCode) {
        return restExchange(HttpMethod.POST, url, token, data, responseCode);
    }

    public static String restPost(String url, String token, String data, int responseCode, Boolean isPrintData) {
        return restExchange(HttpMethod.POST, url, token, data, responseCode, isPrintData);
    }

    public static String restPut(String url, String token, String data, int responseCode) {
        return restExchange(HttpMethod.PUT, url, token, data, responseCode);
    }

    public static String restGet(String url, String token, Integer responseCode) {
        return restExchange(HttpMethod.GET, url, token, null, responseCode);
    }

    public static String restGet(String url, String token) {
        return restGet(url, token, HttpStatus.OK.value());
    }

    public static String restGet(String url) {
        return restGet(url, null);
    }

    public static String restDelete(String url, String token, Integer responseCode) {
        return restExchange(HttpMethod.DELETE, url, token, null, responseCode);
    }

    public static String restDelete(String url, String token, String data, Integer responseCode) {
        return restExchange(HttpMethod.DELETE, url, token, data, responseCode);
    }

    public static String restExchange(HttpMethod method, String url, String token, String data, Integer responseCode) {
        return restExchange(method, url, token, data, responseCode, true);
    }

    public static String restExchange(HttpMethod method, String url, String token, String data, Integer responseCode, Boolean isPrintData) {
        HttpStatus status = null;

        try {
            HttpHeaders headers = getHeaders(token);
            logger.info("==request url:" + url);
            logger.info("==request header:" + (new JSONObject(headers)).toString());
            if (isPrintData) {
                logger.info("==request data" + data);
            }

            long startTime = System.currentTimeMillis();
            ResponseEntity responseEntity;
            HttpEntity reqE;
            if (data != null) {
                reqE = new HttpEntity(data, headers);
                responseEntity = httpUtil.restTemplate.exchange(url, method, reqE, String.class, new Object[0]);
            } else {
                reqE = new HttpEntity(headers);
                responseEntity = httpUtil.restTemplate.exchange(url, method, reqE, String.class, new Object[0]);
            }

            long endTime = System.currentTimeMillis();
            status = responseEntity.getStatusCode();
            if (responseCode != null && status.value() != responseCode) {
                return null;
            } else {
                logger.info("response==" + responseEntity.getBody());
                logger.info("内部接口响应时间: url==" + url + " --" + (endTime - startTime));
                return (String) responseEntity.getBody();
            }
        } catch (Exception e) {
            Stream.of(e.getStackTrace()).forEach(stackTraceElement -> {
                logger.error(stackTraceElement);
            });
            logger.error("restExchangeMap失败:" + e.getMessage() + "，statusCode" + status);
            return null;
        }
    }

    public static Map<String, Object> restPostMap(String url, String token, Map<String, Object> data, Integer responseCode) {
        return restExchangeMap(HttpMethod.POST, url, token, data, responseCode);
    }

    public static Map<String, Object> restPostMapOrigin(String url, String token, Map<String, Object> data, Integer responseCode) {
        return restExchangeMapOrigin(HttpMethod.POST, url, token, data, responseCode);
    }

    public static Map<String, Object> restPostMap(String url, Map<String, Object> data, Integer responseCode) {
        return restExchangeMap(HttpMethod.POST, url, null, data, responseCode);
    }

    public static Map<String, Object> restPostMap(String url, String token, Map<String, Object> data) {
        return restPostMap(url, token, data, HttpStatus.OK.value());
    }

    public static Map<String, Object> restPostMapOrigin(String url, String token, Map<String, Object> data) {
        return restPostMapOrigin(url, token, data, HttpStatus.OK.value());
    }

    public static Map<String, Object> restPostMap(String url, Map<String, Object> data) {
        return restPostMap(url, null, data, HttpStatus.OK.value());
    }

    public static Map<String, Object> restPostMap(String url, Map<String, Object> data, Boolean isPrintData) {
        return restExchangeMap(HttpMethod.POST, false, url, null, data, HttpStatus.OK.value(), isPrintData);
    }

    public static Map<String, Object> restDeleteMap(String url, Map<String, Object> data) {
        return restDeleteMap(url, null, data, HttpStatus.OK.value());
    }

    public static Map<String, Object> restDeleteMap(String url, String token, Map<String, Object> data, Integer responseCode) {
        return restExchangeMap(HttpMethod.DELETE, url, token, data, responseCode);
    }

    public static Map<String, Object> restPutMap(String url, String token, Map<String, Object> data, Integer responseCode) {
        return restExchangeMap(HttpMethod.PUT, url, token, data, responseCode);
    }

    public static Map<String, Object> restPutMap(String url, Map<String, Object> data, Integer responseCode) {
        return restExchangeMap(HttpMethod.PUT, url, null, data, responseCode);
    }

    public static Map<String, Object> restPutMap(String url, String token, Map<String, Object> data) {
        return restPostMap(url, token, data, HttpStatus.OK.value());
    }

    public static Map<String, Object> restPutMap(String url, Map<String, Object> data) {
        return restPutMap(url, null, data, HttpStatus.OK.value());
    }

    public static Map<String, Object> restGetMap(String url, String token, int responseCode) {
        return restExchangeMap(HttpMethod.GET, url, token, null, responseCode);
    }

    public static Map<String, Object> restGetMap(String url, int responseCode) {
        return restGetMap(url, null, responseCode);
    }

    public static Map<String, Object> restExchangeMapOrigin(HttpMethod method, String url, String token, Map<String, Object> data, Integer responseCode) {
        return restExchangeMap(method, true, url, token, data, responseCode);
    }

    public static Map<String, Object> restExchangeMap(HttpMethod method, String url, String token, Map<String, Object> data, Integer responseCode) {
        return restExchangeMap(method, false, url, token, data, responseCode);
    }

    public static Map<String, Object> restExchangeMap(HttpMethod method, Boolean isOrigin, String url, String token,
                                                      Map<String, Object> data, Integer responseCode) {
        return restExchangeMap(method, isOrigin, url, token, data, responseCode, true);
    }

    public static Map<String, Object> restExchangeMap(HttpMethod method, boolean isOrigin, String url,
                                                      String token, Map<String, Object> data, Integer responseCode, Boolean isPrintData) {
        try {
            HttpHeaders headers = getHeaders(token, data);
            logger.info("request url==" + url);
            logger.info("request header==" + (new JSONObject(headers)).toString());
            if (isPrintData) {
                logger.info("request data==" + new JSONObject(data));
            }

            long startTime = System.currentTimeMillis();
            ResponseEntity responseEntity;
            HttpEntity reqE;
            if (data != null) {
                reqE = new HttpEntity(data, headers);
                if (isOrigin) {
                    responseEntity = new RestTemplate().exchange(url, method, reqE, Map.class, new Object[0]);
                } else {
                    responseEntity = httpUtil.restTemplate.exchange(url, method, reqE, Map.class, new Object[0]);
                }
            } else {
                reqE = new HttpEntity(headers);
                if (isOrigin) {
                    responseEntity = new RestTemplate().exchange(url, method, reqE, Map.class, new Object[0]);
                } else {
                    responseEntity = httpUtil.restTemplate.exchange(url, method, reqE, Map.class, new Object[0]);
                }
            }

            long endTime = System.currentTimeMillis();
            HttpStatus status = responseEntity.getStatusCode();
            if (responseCode != null && status.value() != responseCode) {
                return null;
            } else {
                logger.info("respponse==" + responseEntity.getBody());
                logger.info("内部接口相应时间: url==" + url + " --" + (endTime - startTime));
                return (Map) responseEntity.getBody();
            }
        } catch (Exception e) {
            Stream.of(e.getStackTrace()).forEach((stackTraceElement -> {
                logger.error(stackTraceElement);
            }));
            logger.error("restExchangeMap失败: " + e.getMessage());
            return null;
        }
    }

    public static Map<String, Object> json2Map(String json) {
        if (json == null) {
            return null;
        } else {
            JSONObject jo = new JSONObject(json);
            Map<String, Object> map = new LinkedHashMap<>();

            Object k;
            Object v;
            for (Iterator var3 = jo.keySet().iterator(); var3.hasNext(); map.put(k.toString(), v)) {
                k = var3.next();
                v = jo.get(k.toString());
                if (v == JSONObject.NULL) {
                    v = "";
                } else if (v instanceof JSONArray) {
                    v = json2List(v.toString());
                }
            }
            return map;
        }
    }

    public static List<Map<String, Object>> json2List(String json) {
        if (json == null) {
            return null;
        } else {
            List<Map<String, Object>> list = new ArrayList<>();
            JSONArray ja = new JSONArray(json);

            for (int i = 0; i < ja.length(); i++) {
                String subJson = ja.get(i).toString();
                Map<String, Object> map = json2Map(subJson);
                list.add(map);
            }
            return list;
        }
    }

    public static HashMap<String, Object> json2DeepMap(String json) {
        if (json == null) {
            return null;
        } else {
            JSONObject jo = new JSONObject(json);
            HashMap<String, Object> map = new HashMap<>();

            Object k;
            Object v;
            for (Iterator var3 = jo.keySet().iterator(); var3.hasNext(); map.put(k.toString(), v)) {
                k = var3.next();
                v = jo.get(k.toString());
                if (v == JSONObject.NULL) {
                    v = "";
                } else if (v instanceof JSONArray) {
                    v = json2DeepList(v.toString());
                } else if (v instanceof JSONObject) {
                    v = json2DeepMap(JSONObject.valueToString(v));
                }
            }
            return map;
        }
    }

    public static List<Object> json2DeepList(String json) {
        if (json == null) {
            return null;
        } else {
            List<Object> list = new ArrayList<>();
            JSONArray ja = new JSONArray(json);

            for (int i = 0; i < ja.length(); ++i) {
                String subJson = ja.get(i).toString();
                if (subJson.startsWith("{") && subJson.endsWith("}")) {
                    Map<String, Object> map = json2DeepMap(subJson);
                    list.add(map);
                } else {
                    list.add(subJson);
                }
            }
            return list;
        }
    }

    public static String getReturnCode(String json) {
        Map<String, Object> map = json2Map(json);
        return null;
    }

    public static String getReturnCode(Map<String, Object> map) {
        if (map == null) {
            return "";
        } else {
            if (!StringUtils.isEmpty(map.get("response"))) {
                map = (Map) map.get("response");
            }

            Map mapHead;
            if (map.get("head") instanceof Map) {
                mapHead = (Map) map.get("head");
            } else {
                if (map.get("head") instanceof ResultHead) {
                    return ((ResultHead) map.get("head")).getRetFlag();
                }

                mapHead = json2Map(map.get("head").toString());
            }

            return mapHead.get("retFlag").toString();
        }
    }

    public static boolean isSuccess(String json) {
        return getReturnCode(json).equals("00000") || getReturnCode(json).equals("0000");
    }

    public static boolean isSuccess(Map<String, Object> map) {
        return getReturnCode(map).equals("00000") || getReturnCode(map).equals("0000");
    }

    public static Map<String, Object> getHeadMap(Map<String, Object> resultMap) {
        if (resultMap != null && !resultMap.isEmpty()) {
            if (resultMap.get("response") != null) {
                resultMap = (Map) resultMap.get("response");
            }

            if (resultMap.get("head") instanceof Map) {
                return (Map) resultMap.get("head");
            } else if (resultMap.get("head") instanceof ResultHead) {
                Map<String, Object> map = new HashMap<>();
                map.put("retFlag", ((ResultHead) resultMap.get("head")).getRetFlag());
                map.put("retMsg", ((ResultHead) resultMap.get("head")).getRetFlag());
                return map;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static  String getRetFlag(Map<String, Object> resultMap) {
        Map<String, Object> headMap = getHeadMap(resultMap);
        if(headMap == null) {
            return null;
        } else {
            return StringUtils.isEmpty(headMap.get("retMsg")) ? "" : headMap.get("retMsg").toString();
        }
    }

    public static String getRetMsg(Map<String, Object> resultMap) {
        Map<String, Object> headMap = getHeadMap(resultMap);
        if(headMap == null) {
            return  null;
        } else {
            return StringUtils.isEmpty(headMap.get("reMsg")) ? "" : headMap.get("retMsg").toString();
        }
    }
}
