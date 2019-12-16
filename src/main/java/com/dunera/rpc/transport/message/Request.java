package com.dunera.rpc.transport.message;

import com.dunera.rpc.transport.serialize.SerializationType;

import java.io.Serializable;

public class Request implements Serializable {

    private String requestId;

    private String serviceName;

    private String methodName;

    private Object[] methodParams;

    private Object[] methodParamTypes;

    private SerializationType serializationType;

    public Request() {
    }

    public Request(Builder builder) {
        this.requestId = builder.requestId;
        this.serviceName = builder.serviceName;
        this.methodName = builder.methodName;
        this.methodParams = builder.methodParams;
        this.methodParamTypes = builder.methodParamTypes;
        this.serializationType = builder.serializationType;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getMethodParams() {
        return methodParams;
    }

    public void setMethodParams(Object[] methodParams) {
        this.methodParams = methodParams;
    }

    public Object[] getMethodParamTypes() {
        return methodParamTypes;
    }

    public void setMethodParamTypes(Object[] methodParamTypes) {
        this.methodParamTypes = methodParamTypes;
    }

    public SerializationType getSerializationType() {
        return serializationType;
    }

    public void setSerializationType(SerializationType serializationType) {
        this.serializationType = serializationType;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public static class Builder {

        private String requestId;

        private String serviceName;

        private String methodName;

        private Object[] methodParams;

        private SerializationType serializationType;

        private Class<?>[] methodParamTypes;

        public Builder requestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        public Builder methodParamTypes(Class<?>[] methodParamTypes) {
            this.methodParamTypes = methodParamTypes;
            return this;
        }

        public Builder serviceName(String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public Builder methodName(String methodName) {
            this.methodName = methodName;
            return this;
        }

        public Builder methodParams(Object[] methodParams) {
            this.methodParams = methodParams;
            return this;
        }

        public Builder serializationType(SerializationType serializationType) {
            this.serializationType = serializationType;
            return this;
        }

        public Request build() {
            return new Request(this);
        }

    }

}
