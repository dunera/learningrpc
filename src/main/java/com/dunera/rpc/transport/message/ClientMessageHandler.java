package com.dunera.rpc.transport.message;

import com.dunera.rpc.transport.serialize.SerializationFactory;
import com.dunera.rpc.transport.serialize.SerializationType;
import com.dunera.rpc.transport.serialize.Serializer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientMessageHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(ClientMessageHandler.class);

    public static byte[] buildRequest(Request request) {
        Serializer serializer = SerializationFactory.getSerializer(request.getSerializationType());
        byte[] req = serializer.encode(request);
        Header header = new Header(req.length, 0, request.getSerializationType().getVal(), 0);
        byte[] head_data = header.getHeader(req);
        System.arraycopy(req, 0, head_data, 8, req.length);
        return head_data;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        byte[] head_data = (byte[]) msg;
        Header header = new Header(head_data);
        byte[] data = new byte[head_data.length - 8];
        System.arraycopy(head_data, 8, data, 0, data.length);
        SerializationType serializationType = SerializationType.getSerializationType(header.getSerializationType());
        Serializer serializer = SerializationFactory.getSerializer(serializationType);
        Response rpcResponse = serializer.decode(data, Response.class);
        logger.debug("Tcp Client receive data：" + rpcResponse);

        RPCFuture<Response> rpcFuture = RPCFutureCache.rpcFutureCache.get(rpcResponse.getRequestId());
        if (rpcFuture != null){
            rpcFuture.success(rpcResponse);
        }
    }
}
