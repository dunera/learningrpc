package com.dunera.rpc.registry;

import com.dunera.rpc.exception.RPCException;
import com.dunera.rpc.registry.impl.ZooKeeperRegistry;

public class RegistryFactory {

    public static Registry getRegistry(RegistryConfig config) {
        if ("zookeeper".equals(config.getRegistryType())) {
            return new ZooKeeperRegistry(config);
        }
        throw new RPCException("Unsupported registry type:" + config.getRegistryType());
    }
}
