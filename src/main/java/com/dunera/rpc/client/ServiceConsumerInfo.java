package com.dunera.rpc.client;

import com.dunera.rpc.client.loadbalance.LoadBalanceStrategy;
import com.dunera.rpc.client.loadbalance.LoadBalancer;
import com.dunera.rpc.exception.RPCException;
import com.dunera.rpc.proxy.JDKProxy;
import com.dunera.rpc.server.ServiceProviderInfo;
import com.dunera.rpc.transport.serialize.SerializationType;

import java.util.List;

public class ServiceConsumerInfo<T> {

    /**
     * 客户端配置
     */
    private Client client;

    /**
     * 客户端配置
     */
    private ClientConfig clientConfig;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务接口
     */
    private Class<T> service;

    /**
     * 代理实例
     */
    private T proxyInstance;

    /**
     * 序列化方式
     */
    private SerializationType serializationType;

    /**
     * 负载均衡策略
     */
    private LoadBalanceStrategy loadBalanceStrategy;

    /**
     * 服务提供者信息列表
     */
    private List<ServiceProviderInfo<T>> providerInfos;

    /**
     * 负载均衡实现
     */
    private LoadBalancer loadBalancer;

    /**
     * 超时时间
     */
    private Long timeOut;

    /**
     * 获取代理对象
     *
     * @return 代理对象
     */
    public T refer() {
        if (proxyInstance != null) {
            return proxyInstance;
        }

        if (clientConfig == null) {
            throw new RPCException("please set client config info!");
        }

        if (serviceName == null) {
            throw new RPCException("please set service and service name!");
        }
        this.proxyInstance = (T) JDKProxy.getInstance(this);
        return proxyInstance;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getServiceName() {
        return serviceName;
    }

    public ServiceConsumerInfo<T> serviceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public Class<T> getService() {
        return service;
    }

    public ServiceConsumerInfo<T> service(Class<T> service) {
        this.service = service;
        return this;
    }

    public T getProxyInstance() {
        return proxyInstance;
    }

    public void setProxyInstance(T proxyInstance) {
        this.proxyInstance = proxyInstance;
    }

    public List<ServiceProviderInfo<T>> getProviderInfos() {
        return providerInfos;
    }

    public void setProviderInfos(List<ServiceProviderInfo<T>> providerInfos) {
        this.providerInfos = providerInfos;
    }

    public SerializationType getSerializationType() {
        return serializationType;
    }

    public ServiceConsumerInfo<T> serializationType(SerializationType serializationType) {
        this.serializationType = serializationType;
        return this;
    }

    public LoadBalanceStrategy getLoadBalanceStrategy() {
        return loadBalanceStrategy;
    }

    public ServiceConsumerInfo<T> loadBalanceStrategy(LoadBalanceStrategy loadBalanceStrategy) {
        this.loadBalanceStrategy = loadBalanceStrategy;
        return this;
    }

    public ClientConfig getClientConfig() {
        return clientConfig;
    }

    public ServiceConsumerInfo<T> clientConfig(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
        return this;
    }

    public LoadBalancer getLoadBalancer() {
        return loadBalancer;
    }

    public void setLoadBalancer(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    public Long getTimeOut() {
        return timeOut;
    }

    public ServiceConsumerInfo<T> timeOut(Long timeOut) {
        this.timeOut = timeOut;
        return this;
    }
}
