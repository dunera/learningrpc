package com.dunera.rpc.client;

import com.dunera.rpc.transport.message.Request;
import com.dunera.rpc.transport.message.Response;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public abstract class Client {

    public static volatile Map<String, ServiceConsumerInfo<?>> serviceConsumerInfoCache = new ConcurrentHashMap<>();

    public Client() {
    }

    public Client(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    /**
     * 客户端配置信息
     */
    ClientConfig clientConfig;

    /**
     * 建立连接
     */
    public abstract void connect();

    /**
     * 关闭连接
     */
    public abstract void close();

    public abstract Response sendMessage(Request request, Long timeOut) throws InterruptedException, ExecutionException, TimeoutException;

}
