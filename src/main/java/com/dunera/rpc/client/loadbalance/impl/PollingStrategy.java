package com.dunera.rpc.client.loadbalance.impl;

import com.dunera.rpc.client.ServiceConsumerInfo;
import com.dunera.rpc.client.loadbalance.LoadBalancer;
import com.dunera.rpc.server.ServiceProviderInfo;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 轮询
 */
public class PollingStrategy extends LoadBalancer {

    //计数器
    private int index = 0;
    private Lock lock = new ReentrantLock();

    @Override
    public ServiceProviderInfo select(ServiceConsumerInfo serviceConsumerInfo, List<ServiceProviderInfo> providerInfos) {
        ServiceProviderInfo service = null;
        try {
            lock.tryLock(10, TimeUnit.MILLISECONDS);
            //若计数大于服务提供者个数,将计数器归0
            if (index >= providerInfos.size()) {
                index = 0;
            }
            service = providerInfos.get(index);
            index++;

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

        //兜底,保证程序健壮性,若未取到服务,则直接取第一个
        if (service == null) {
            service = providerInfos.get(0);
        }
        return service;
    }
}
