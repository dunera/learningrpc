package com.dunera.rpc.client.loadbalance.impl;

import com.dunera.rpc.client.ServiceConsumerInfo;
import com.dunera.rpc.client.loadbalance.LoadBalancer;
import com.dunera.rpc.common.IPHelper;
import com.dunera.rpc.server.ServiceProviderInfo;

import java.util.List;

/**
 * 哈希
 */
public class HashStrategy extends LoadBalancer {

    @Override
    public ServiceProviderInfo select(ServiceConsumerInfo serviceConsumerInfo, List<ServiceProviderInfo> providerInfos) {
        //获取调用方ip
        String localIP = IPHelper.localIp();
        //获取源地址对应的hashcode
        int hashCode = localIP.hashCode();
        //获取服务列表大小
        int size = providerInfos.size();
        return providerInfos.get(hashCode % size);
    }
}
