package com.jx.common;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FormatUtil {
    public FormatUtil() {
    }

    /**
     * global logger
     */
    public static final Log logger = LogFactory.getLog(FormatUtil.class);
    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String FAIL_PARSE_DATA = "fail to parse json data";
    public static final String FAIL_READ_WRITE_DATA = "fail to read/write json data";
    public static final String ERROR = "Error";
    /**
     * reuse ObjectMapper
     */
    private static ObjectMapper jsonMapper = new ObjectMapper();

    static {
        jsonMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
        jsonMapper.setDateFormat(sdf);
        DeserializationConfig deCfg = jsonMapper.getDeserializationConfig();
        jsonMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    }

    /********************************Json操作工具类****************************************/
    /**
     * 将json格式字符串转换为List列表
     */

    public static <E> List<E> toObjectList(String jsonString, Class<E> clazz) {

        List<E> list = new ArrayList<>();
        try {
            TypeFactory typeFactory = jsonMapper.getTypeFactory();
            list = jsonMapper.readValue(jsonString, typeFactory.constructCollectionType(List.class, clazz));
        } catch (JsonProcessingException e) {
            logger.error(FAIL_PARSE_DATA + jsonString);
            logger.debug(ERROR, e);
        } catch (IOException e) {
            logger.error(FAIL_READ_WRITE_DATA + jsonString);
            logger.debug(ERROR, e);
        }
        return list;
    }

    /**
     * 将json转换成List<Map>
     */
    public static List<Map> toMapList(String jsonString) {
        List<Map> list = new ArrayList<>();
        try {
            list = jsonMapper.readValue(jsonString, List.class);
        } catch (JsonProcessingException e) {
            logger.error(FAIL_PARSE_DATA + jsonString);
            logger.debug(ERROR, e);
        } catch (IOException e) {
            logger.error(FAIL_READ_WRITE_DATA + jsonString);
            logger.debug(ERROR, e);
        }
        return list;
    }

    /**
     * 将json转换成相应对象
     */
    public static <E> E toObject(String jsonString, Class<E> clazz) {
        E result = null;
        try {
            result = jsonMapper.readValue(jsonString, clazz);
        } catch (JsonMappingException e) {
            logger.error(FAIL_PARSE_DATA + jsonString);
            logger.debug(ERROR, e);
        } catch (JsonProcessingException e) {
            logger.error("fail to mapping json data" + jsonString);
            logger.error(ERROR, e);
        } catch (IOException e) {
            logger.error(FAIL_READ_WRITE_DATA + jsonString);
            logger.error(ERROR, e);
        }
        return result;
    }

    /**
     * 将对象转换为Json
     */
    public static String toJson(Object obj) {
        if (StringUtils.isEmpty(obj)) {
            return "";
        }
        try {
            return jsonMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new NoSuchElementException();
        }
    }

    /************************************日期操作工具类**************************************/

    /**
     * 格式化日期，将日期转换为yyyy-MM-dd HH:mm:ss格式
     *
     * @param date date to be format
     * @return formatted string, like yyyy-MM-dd HH:mm:ss
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
        return sdf.format(date);
    }

    /**
     * 解析日期字符串，将字符串转换为Date对象
     *
     * @param dateStr
     * @return
     */
    public static Date parseDate(String dateStr) {
        if (dateStr == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            throw new NoSuchElementException();
        }
    }

    /********************************其他工具****************************************/

    /**
     * 将一个pojo类转换为map
     *
     * @param object
     * @return
     */
    public static Map<String, Object> obj2Map(Object object) {
        String objJson = JSON.toJSONString(object);
        return JSON.parseObject(objJson, Map.class);
    }
}
