package com.dunera.rpc.transport.message;

import com.dunera.rpc.server.Server;
import com.dunera.rpc.server.ServiceProviderInfo;
import com.dunera.rpc.transport.serialize.SerializationFactory;
import com.dunera.rpc.transport.serialize.SerializationType;
import com.dunera.rpc.transport.serialize.Serializer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 服务端处理器
 */
public class ServerMessageHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(ServerMessageHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        Response rpcResponse = new Response();
        byte[] requestData = (byte[]) msg;
        Header header = new Header(requestData);

        byte[] data = new byte[requestData.length - 8];
        System.arraycopy(requestData, 8, data, 0, data.length);

        SerializationType serializationType = SerializationType.getSerializationType(header.getSerializationType());
        Serializer serializer = SerializationFactory.getSerializer(serializationType);

        Request rpcRequest = serializer.decode(data, Request.class);
        rpcResponse.setRequestId(rpcRequest.getRequestId());
        if (logger.isDebugEnabled()) {
            logger.debug("server received message : {}", rpcRequest);
        }
        if (!Objects.isNull(Server.serviceProviderInfoCache)) {
            ServiceProviderInfo<?> serviceProviderInfo = Server.serviceProviderInfoCache.get(rpcRequest.getServiceName());
            try {
                Object obj = invoke(serviceProviderInfo.getImpl(), rpcRequest.getMethodName(), rpcRequest.getMethodParamTypes(), rpcRequest.getMethodParams());
                rpcResponse.setResult(obj);
            } catch (Exception e) {
                rpcResponse.setErrorMsg("invoke error ！");
                logger.error("invoke exception : {}", e.getMessage());
            }
        } else {
            rpcResponse.setErrorMsg("no service provider !");
        }
        byte[] responseData = serializer.encode(rpcResponse);
        Header respHeader = new Header(responseData.length, 0, 1, 0);
        byte[] responseHeaderData = respHeader.getHeader(responseData);
        System.arraycopy(responseData, 0, responseHeaderData, 8, responseData.length);

        ctx.channel().writeAndFlush(responseHeaderData);
        if (logger.isDebugEnabled()) {
            logger.debug("response data ：" + rpcResponse);
        }
    }

    public static Object invoke(Object instance, String methodName, Object[] paramTypes, Object[] params) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method[] methods = instance.getClass().getMethods();
        for (Method method : methods) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (methodName.equals(method.getName()) && equals(paramTypes, parameterTypes)) {
                return method.invoke(instance, params);
            }
        }
        return null;
    }

    private static boolean equals(Object[] paramTypes, Class<?>[] clazz) {
        if (paramTypes == null && clazz == null) {
            return true;
        } else if (paramTypes == null || clazz == null || paramTypes.length != clazz.length) {
            return false;
        } else {
            for (int i = 0; i < paramTypes.length; i++) {
                if (!paramTypes[i].getClass().getName().equals(clazz[i].getName())) {
                    return false;
                }
            }
            return true;
        }
    }

}
