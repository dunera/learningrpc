package com.dunera.rpc.client.loadbalance;

import com.dunera.rpc.client.loadbalance.impl.HashStrategy;
import com.dunera.rpc.client.loadbalance.impl.PollingStrategy;
import com.dunera.rpc.client.loadbalance.impl.RandomStrategy;

public class LoadBalancerFactory {

    public static LoadBalancer getLoadBalancer(LoadBalanceStrategy strategy) {
        switch (strategy) {
            case CONSISTENT_HASH:
                return new HashStrategy();
            case ROUND_ROBIN:
                return new PollingStrategy();
            case RANDOM:
            default:
                return new RandomStrategy();
        }
    }
}
