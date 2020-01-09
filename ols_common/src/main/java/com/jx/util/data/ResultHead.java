package com.jx.util.data;

import java.io.Serializable;

public class ResultHead implements Serializable {
    private static final long serialVersionUID = 1L;
    private String retFlag;
    private String retMsg;

    public ResultHead() {}

    public ResultHead(String retFlag, String retMsg) {
        this.retFlag = retFlag;
        this.retMsg = retMsg;
    }

    public String getRetFlag() {
        return retFlag;
    }

    public void setRetFlag(String retFlag) {
        this.retFlag = retFlag;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    @Override
    public String toString() {
        return "ResultHead{" +
                "retFlag='" + retFlag + '\'' +
                ", retMsg='" + retMsg + '\'' +
                '}';
    }
}
