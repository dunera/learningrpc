package com.dunera.rpc.transport.message;

import java.io.Serializable;

public class Response implements Serializable {

    private String requestId;

    private Object result;

    private String errorMsg;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "Response{" +
                "requestId='" + requestId + '\'' +
                ", result=" + result +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
