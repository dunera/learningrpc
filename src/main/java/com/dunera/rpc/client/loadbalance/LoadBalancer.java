package com.dunera.rpc.client.loadbalance;

import com.dunera.rpc.client.ServiceConsumerInfo;
import com.dunera.rpc.server.ServiceProviderInfo;

import java.util.List;

public abstract class LoadBalancer {

    public abstract ServiceProviderInfo select(ServiceConsumerInfo serviceConsumerInfo, List<ServiceProviderInfo> providerInfos);

}
