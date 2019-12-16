package com.dunera.rpc.server;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务端抽象接口，定义服务端具备的能力
 */
public abstract class Server {

    /**
     * 服务端服务缓存
     */
    public static volatile Map<String, ServiceProviderInfo<?>> serviceProviderInfoCache = new ConcurrentHashMap<>();

    /**
     * 服务配置信息
     */
    ServerConfig serverConfig;

    public Server(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public Server(ServerConfig serverConfig, ServiceProviderInfo<?> provider) {
        this.serverConfig = serverConfig;
        String serviceName = provider.getServiceName() == null ? provider.getService().getName() : provider.getServiceName();
        serviceProviderInfoCache.put(serviceName, provider.serviceName(serviceName));
    }

    public Server(ServerConfig serverConfig, List<ServiceProviderInfo<?>> providers) {
        this.serverConfig = serverConfig;
        if (Objects.nonNull(providers)) {
            providers.forEach(s -> {
                String serviceName = s.getServiceName() == null ? s.getService().getName() : s.getServiceName();
                serviceProviderInfoCache.put(serviceName, s.serviceName(serviceName));
            });
        }
    }

    /**
     * 启动服务
     */
    protected abstract void start();

    /**
     * 停止服务
     */
    protected abstract void stop();

    /**
     * 添加服务提供者
     *
     * @param provider 服务提供者
     * @return 服务对象
     */
    public Server addServiceProvider(ServiceProviderInfo<?> provider) {
        String serviceName = provider.getServiceName() == null ? provider.getService().getName() : provider.getServiceName();
        serviceProviderInfoCache.put(serviceName, provider);
        return this;
    }

}
