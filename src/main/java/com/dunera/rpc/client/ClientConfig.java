package com.dunera.rpc.client;

import com.dunera.rpc.registry.RegistryConfig;
import com.dunera.rpc.transport.serialize.SerializationType;

public class ClientConfig {

    /**
     * 直连地址信息
     */
    private String host;

    /**
     * 直连端口信息
     */
    private Integer port;

    /**
     * 序列化方式
     */
    private SerializationType serializationType;

    /**
     * 注册中心信息
     */
    private RegistryConfig registryConfig;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public SerializationType getSerializationType() {
        return serializationType;
    }

    public void setSerializationType(SerializationType serializationType) {
        this.serializationType = serializationType;
    }

    public RegistryConfig getRegistryConfig() {
        return registryConfig;
    }

    public void setRegistryConfig(RegistryConfig registryConfig) {
        this.registryConfig = registryConfig;
    }
}
