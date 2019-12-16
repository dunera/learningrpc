package com.dunera.rpc.registry;

import com.dunera.rpc.client.ServiceConsumerInfo;
import com.dunera.rpc.server.ServiceProviderInfo;

import java.util.List;

/**
 * 注册中心
 */
public abstract class Registry {

    /**
     * 注册中心服务配置
     */
    protected RegistryConfig registryConfig;

    /**
     * 注册中心配置
     *
     * @param registryConfig 注册中心配置
     */
    public Registry(RegistryConfig registryConfig) {
        this.registryConfig = registryConfig;
    }

    /**
     * 启动
     */
    public abstract boolean init();

    /**
     * 停止
     */
    public abstract boolean destroy();

    /**
     * 注册服务提供者
     */
    public abstract void register(ServiceProviderInfo<?> provider);

    /**
     * 订阅服务列表
     */
    public abstract List<ServiceProviderInfo<?>> subscribe(ServiceConsumerInfo<?> consumer);


    public void setRegistryConfig(RegistryConfig registryConfig) {
        this.registryConfig = registryConfig;
    }
}
