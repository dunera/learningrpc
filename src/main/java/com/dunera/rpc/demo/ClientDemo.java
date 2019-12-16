package com.dunera.rpc.demo;

import com.dunera.rpc.client.ClientConfig;
import com.dunera.rpc.client.ServiceConsumerInfo;
import com.dunera.rpc.registry.RegistryConfig;
import com.dunera.rpc.transport.serialize.SerializationType;

public class ClientDemo {

    public static void main(String[] args) {
        // 注册中心配置
        RegistryConfig registryConfig = new RegistryConfig("localhost", 2181, "zookeeper");

        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setHost("localhost");
        clientConfig.setPort(8888);
        clientConfig.setRegistryConfig(registryConfig);

        HelloWorld helloWorld = new ServiceConsumerInfo<HelloWorld>()
                .clientConfig(clientConfig)
                .serializationType(SerializationType.JSON)
                .serviceName(HelloWorld.class.getName())
                .service(HelloWorld.class)
                .refer();
        while (true) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String result = helloWorld.sayHelloTo("hello ni hao");
            System.out.println("client response : " + result);
        }


    }
}
