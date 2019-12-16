package com.dunera.rpc.client.loadbalance.impl;

import com.dunera.rpc.client.ServiceConsumerInfo;
import com.dunera.rpc.client.loadbalance.LoadBalancer;
import com.dunera.rpc.server.ServiceProviderInfo;

import java.util.List;
import java.util.Random;

/**
 * 随机
 *
 * @author Administrator
 */
public class RandomStrategy extends LoadBalancer {

    @Override
    public ServiceProviderInfo select(ServiceConsumerInfo serviceConsumerInfo, List<ServiceProviderInfo> providerInfos) {
        int MAX_LEN = providerInfos.size();
        Random random = new Random();
        int index = random.nextInt(MAX_LEN);
        return providerInfos.get(index);
    }
}
