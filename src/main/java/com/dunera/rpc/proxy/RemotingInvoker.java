package com.dunera.rpc.proxy;

import com.dunera.rpc.client.Client;
import com.dunera.rpc.client.RPCClient;
import com.dunera.rpc.client.ServiceConsumerInfo;
import com.dunera.rpc.common.RequestId;
import com.dunera.rpc.transport.message.Request;
import com.dunera.rpc.transport.message.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 远程调用
 *
 * @author v_wangshiyu
 */
public class RemotingInvoker {

    private static final Logger logger = LoggerFactory.getLogger(RemotingInvoker.class);

    /**
     * 组装远程调用
     */
    public static Object invoke(ServiceConsumerInfo<?> serviceConsumerInfo, Method method, Object[] parameters) throws Exception {
        Request rpcRequest = new Request();
        rpcRequest.setServiceName(serviceConsumerInfo.getServiceName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setMethodParamTypes(method.getParameterTypes());
        rpcRequest.setMethodParams(parameters);
        rpcRequest.setRequestId(RequestId.next());
        rpcRequest.setSerializationType(serviceConsumerInfo.getSerializationType());

        Client client = serviceConsumerInfo.getClient();
        if (client == null) {
            client = new RPCClient(serviceConsumerInfo.getClientConfig());
            serviceConsumerInfo.setClient(client);
        }

        client.connect();
        Response response = client.sendMessage(rpcRequest, serviceConsumerInfo.getTimeOut());
        if (response != null) {
            logger.debug("Tcp Client send data：" + rpcRequest);
            return response.getResult();
        }
        return null;
    }
}
