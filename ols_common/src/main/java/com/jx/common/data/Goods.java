package com.jx.common.data;

import lombok.Data;

import java.math.BigInteger;

@Data
//测试用
public class Goods {
    //商品id
    private BigInteger goodsId;

    private String data;
    //商品是否可用标识，true可用，false不可用
    private String status;
    //数据插入或者更新时间
    private String ctime;
    //商品版本号
    private String version;
}
