package com.dunera.rpc.server;

import java.util.List;

public class ServiceProviderInfo<T> {

    /**
     * 服务接口的类全限定名
     */
    private String serviceName;

    /**
     * 服务提供者class
     */
    private Class<T> service;

    /**
     * 服务提供者实现对象
     */
    private T impl;

    /**
     * 服务配置信息
     */
    private ServerConfig serverConfig;

    public ServiceProviderInfo() {
    }

    public ServiceProviderInfo(String serviceName, Class<T> service, T impl) {
        this.serviceName = serviceName;
        this.service = service;
        this.impl = impl;
    }

    public String getServiceName() {
        return serviceName;
    }

    public ServiceProviderInfo<T> serviceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public Class<T> getService() {
        return service;
    }

    public ServiceProviderInfo<T> service(Class<T> service) {
        this.service = service;
        return this;
    }

    public T getImpl() {
        return impl;
    }

    public ServiceProviderInfo<T> impl(T impl) {
        this.impl = impl;
        return this;
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }
}
