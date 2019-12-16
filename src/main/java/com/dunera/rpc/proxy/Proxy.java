package com.dunera.rpc.proxy;

public abstract class Proxy {

    /**
     * 获取代理对象
     *
     * @param interfaceClass 接口类
     * @param <T>            类型
     * @return 代理对象
     */
    public abstract <T> T getProxy(Class<T> interfaceClass);


}
