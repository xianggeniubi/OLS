package com.jx.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jx.common.data.Goods;
import org.junit.Test;

import java.math.BigInteger;

public class JacksonTest {
    private ObjectMapper objectMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY) //类级别的设置
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    @Test
    public void writeJson() throws JsonProcessingException {
        Goods goods = new Goods();
        goods.setGoodsId(BigInteger.valueOf(1124));
        goods.setCtime("2019-12-25");

        String jsonStr = objectMapper.writeValueAsString(goods);
        System.out.println("JSON" + jsonStr);
    }
}
