package com.dunera.rpc.server;

import com.dunera.rpc.registry.RegistryConfig;
import com.dunera.rpc.transport.serialize.SerializationType;

import java.util.concurrent.ThreadPoolExecutor;

public class ServerConfig {

    /**
     * 地址
     */
    private String host;

    /**
     * 端口
     */
    private int port;

    /**
     * 权值
     */
    private double weight;

    /**
     * 序列化方式
     */
    private SerializationType serializationType;

    /**
     * 注册中心信息
     */
    private RegistryConfig registryConfig;

    private int ioThreads;

    private int workerThreads;

    private ThreadPoolExecutor executor;

    public ServerConfig(String host, int port, double weight, SerializationType serializationType) {
        this.host = host;
        this.port = port;
        this.weight = weight;
        this.serializationType = serializationType;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
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
