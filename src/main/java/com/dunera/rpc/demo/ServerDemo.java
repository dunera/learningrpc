package com.dunera.rpc.demo;

import com.dunera.rpc.registry.RegistryConfig;
import com.dunera.rpc.server.RPCServer;
import com.dunera.rpc.server.ServerConfig;
import com.dunera.rpc.server.ServiceProviderInfo;
import com.dunera.rpc.transport.serialize.SerializationType;

public class ServerDemo {

    public static void main(String[] args) {

        // 注册中心配置
        RegistryConfig registryConfig = new RegistryConfig("localhost", 2181, "zookeeper");

        // 服务端配置
        ServerConfig serverConfig = new ServerConfig("localhost", 8888, 1.0, SerializationType.JSON);
        serverConfig.setRegistryConfig(registryConfig);

        // 一个服务端可以为多个服务提供者提供服务
        // 声明服务提供者与实现关系，也可以通过注解进行定义
        ServiceProviderInfo<HelloWorld> serviceProviderInfo = new ServiceProviderInfo<HelloWorld>()
                .service(HelloWorld.class)
                .impl(new HelloWorldImpl());

        serviceProviderInfo.setServerConfig(serverConfig);

        RPCServer server = new RPCServer(serverConfig, serviceProviderInfo);
        // 启动服务
        server.start();
    }

}
