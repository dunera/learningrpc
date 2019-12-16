package com.dunera.rpc.proxy;

import com.dunera.rpc.client.ClientConfig;
import com.dunera.rpc.client.ServiceConsumerInfo;
import com.dunera.rpc.client.loadbalance.LoadBalanceStrategy;
import com.dunera.rpc.client.loadbalance.LoadBalancer;
import com.dunera.rpc.client.loadbalance.LoadBalancerFactory;
import com.dunera.rpc.exception.RPCException;
import com.dunera.rpc.registry.Registry;
import com.dunera.rpc.registry.RegistryFactory;
import com.dunera.rpc.server.ServiceProviderInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class JDKProxy {

    private final static Logger logger = LoggerFactory.getLogger(JDKProxy.class);

    public static Object getInstance(ServiceConsumerInfo serviceConsumerInfo) {
        JDKInvocationHandler invocationHandler = new JDKInvocationHandler(serviceConsumerInfo);
        ClientConfig clientConfig = serviceConsumerInfo.getClientConfig();
        if (clientConfig.getHost() != null && clientConfig.getPort() != null) {
            logger.info("direct connect to " + clientConfig.getHost() + ":" + clientConfig.getPort());
        } else if (clientConfig.getRegistryConfig() != null) {
            Registry registry = RegistryFactory.getRegistry(clientConfig.getRegistryConfig());
            registry.init();
            List<ServiceProviderInfo<?>> providerInfos = registry.subscribe(serviceConsumerInfo);
            if (providerInfos == null || providerInfos.isEmpty()) {
                throw new RPCException("not such provider info in registry :" + serviceConsumerInfo.getServiceName());
            }
            serviceConsumerInfo.setProviderInfos(providerInfos);
            LoadBalancer loadBalancer = LoadBalancerFactory.getLoadBalancer(serviceConsumerInfo.getLoadBalanceStrategy() == null ? LoadBalanceStrategy.RANDOM : serviceConsumerInfo.getLoadBalanceStrategy());
            ServiceProviderInfo serviceProviderInfo = loadBalancer.select(serviceConsumerInfo, serviceConsumerInfo.getProviderInfos());
            if (serviceProviderInfo == null) {
                throw new RPCException("not find provider info of :" + serviceConsumerInfo.getServiceName());
            }
            clientConfig.setHost(serviceProviderInfo.getServerConfig().getHost());
            clientConfig.setPort(serviceProviderInfo.getServerConfig().getPort());
            serviceConsumerInfo.clientConfig(clientConfig);
        } else {
            throw new RPCException("can not get proxy instance for no client info config");
        }
        return java.lang.reflect.Proxy.newProxyInstance(
                serviceConsumerInfo.getService().getClassLoader(),
                new Class[]{serviceConsumerInfo.getService()},
                invocationHandler);
    }

}
