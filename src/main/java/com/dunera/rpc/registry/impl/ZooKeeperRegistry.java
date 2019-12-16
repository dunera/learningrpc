package com.dunera.rpc.registry.impl;

import com.dunera.rpc.client.ServiceConsumerInfo;
import com.dunera.rpc.registry.Registry;
import com.dunera.rpc.registry.RegistryConfig;
import com.dunera.rpc.server.ServerConfig;
import com.dunera.rpc.server.ServiceProviderInfo;
import com.dunera.rpc.transport.serialize.SerializationType;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ZooKeeperRegistry extends Registry {

    private static final Logger log = LoggerFactory.getLogger(ZooKeeperRegistry.class);

    private CountDownLatch latch = new CountDownLatch(1);

    private ZooKeeper zooKeeper;

    public static int ZK_SESSION_TIMEOUT = 5000;// 会话超时时间

    public static String ZK_RPC_DATA_PATH = "/rpc";

    public static char DELIMITER = '-';

    /**
     * 注册中心配置
     *
     * @param registryConfig 注册中心配置
     */
    public ZooKeeperRegistry(RegistryConfig registryConfig) {
        super(registryConfig);
    }

    @Override
    public boolean init() {
        if (zooKeeper != null) {
            return true;
        }
        String zkAddress = registryConfig.getHost() + ":" + registryConfig.getPort();
        try {
            zooKeeper = new ZooKeeper(zkAddress, ZK_SESSION_TIMEOUT, event -> {
                if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                    latch.countDown();
                }
            });
            latch.await();
        } catch (IOException | InterruptedException e) {
            log.error(e.toString());
            return false;
        }
        addRoot();
        return true;
    }

    @Override
    public boolean destroy() {
        if (zooKeeper != null) {
            try {
                zooKeeper.close();
                return true;
            } catch (InterruptedException e) {
                log.error(e.toString());
                return false;
            }
        }
        return true;
    }

    @Override
    public void register(ServiceProviderInfo<?> provider) {
        ServerConfig config = provider.getServerConfig();
        String host = config.getHost();
        int port = config.getPort();
        SerializationType serializationType = config.getSerializationType();
        createNode(provider.getServiceName(), host + ":" + port + ":" + serializationType);
    }

    @Override
    public List<ServiceProviderInfo<?>> subscribe(ServiceConsumerInfo<?> consumer) {
        List<ServiceProviderInfo<?>> providerInfos = new ArrayList<>();
        try {
            List<String> nodeList = zooKeeper.getChildren(ZK_RPC_DATA_PATH, false, null);
            for (String node : nodeList) {
                byte[] bytes = zooKeeper.getData(ZK_RPC_DATA_PATH + "/" + node, false, null);
                String value = new String(bytes);
                String[] val = value.split(":");
                ServiceProviderInfo<?> providerInfo = new ServiceProviderInfo<>();
                ServerConfig serverConfig = new ServerConfig(val[0], Integer.parseInt(val[1]), 0, SerializationType.valueOf(val[2]));
                providerInfo.setServerConfig(serverConfig);
                providerInfos.add(providerInfo);
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
        return providerInfos;
    }

    /**
     * 添加root节点
     */
    public boolean addRoot() {
        try {
            Stat s = zooKeeper.exists(ZK_RPC_DATA_PATH, false);
            if (s == null) {
                //同步创建临时持久节点
                zooKeeper.create(ZK_RPC_DATA_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (KeeperException | InterruptedException e) {
            log.error(e.toString());
            return false;
        }
        return true;
    }

    /**
     * 创建node节点
     */
    public boolean createNode(String node, String data) {
        try {
            byte[] bytes = data.getBytes();
            //同步创建临时顺序节点
            String path = zooKeeper.create(ZK_RPC_DATA_PATH + "/" + node + "-", bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            log.info("create zookeeper node ({} => {})", path, data);
        } catch (KeeperException | InterruptedException e) {
            log.error("", e);
            return false;
        }
        return true;
    }

}
