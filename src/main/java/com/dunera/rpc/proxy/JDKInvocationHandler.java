package com.dunera.rpc.proxy;

import com.dunera.rpc.client.ServiceConsumerInfo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class JDKInvocationHandler implements InvocationHandler {

    /**
     * 服务提供者信息
     */
    ServiceConsumerInfo<?> serviceConsumerInfo;

    public JDKInvocationHandler(ServiceConsumerInfo<?> serviceConsumerInfo) {
        this.serviceConsumerInfo = serviceConsumerInfo;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] parameters) throws Throwable {
        // 本地调用
        if (Object.class.equals(method.getDeclaringClass())) {
            try {
                return method.invoke(this, parameters);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        } else {
            return RemotingInvoker.invoke(serviceConsumerInfo, method, parameters);
        }
        return null;
    }
}