package com.dunera.rpc.client;

import com.dunera.rpc.transport.message.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RPCClient extends Client {

    private final static Logger logger = LoggerFactory.getLogger(RPCClient.class);

    private String host;
    private int port;
    private Bootstrap bootstrap;
    private Channel channel;
    private EventLoopGroup group;

    public RPCClient(ClientConfig clientConfig) {
        super(clientConfig);
        this.host = clientConfig.getHost();
        this.port = clientConfig.getPort();
    }

    public RPCClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public boolean sendMessage(Object msg) throws Exception {
        if (channel != null) {
            channel.writeAndFlush(msg).sync();
            logger.debug("send message success !");
            return true;
        } else {
            logger.debug("send message fail, the channel is null !");
            return false;
        }
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    /**
     * 初始化Bootstrap
     */
    public final Bootstrap getBootstrap() {
        group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class);
        b.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
                pipeline.addLast("decoder", new ByteArrayDecoder());
                pipeline.addLast("encoder", new ByteArrayEncoder());
                pipeline.addLast("handler", new ClientMessageHandler());
            }
        });
        b.option(ChannelOption.SO_KEEPALIVE, true);
        return b;
    }

    /**
     * 连接，获取Channel
     */
    public final Channel getChannel(String host, int port) {
        Channel channel;
        try {
            channel = bootstrap.connect(host, port).sync().channel();
            return channel;
        } catch (Exception e) {
            shutdown();
            logger.info(String.format("connect Server(IP[%s],PORT[%s]) fail!", host, port));
            return null;
        }
    }

    public boolean shutdown() {
        Future<?> future = group.shutdownGracefully();
        future.syncUninterruptibly();
        return true;
    }

    @Override
    public void connect() {
        // 获取注册中心信息，
        // 从注册中心订阅服务提供者信息
        // 根据服务提供者信息，建立连接
        // 给服务消费者生成代理
        if (clientConfig.getHost() != null && clientConfig.getPort() != null) {
            bootstrap = getBootstrap();
            channel = getChannel(clientConfig.getHost(), clientConfig.getPort());
        }
    }

    @Override
    public void close() {
        shutdown();
    }

    @Override
    public Response sendMessage(Request request, Long timeOut) throws InterruptedException, ExecutionException, TimeoutException {
        if (channel != null) {
            byte[] data = ClientMessageHandler.buildRequest(request);
            try {
                channel.writeAndFlush(data).sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.debug("send message success!");
            RPCFuture<Response> syncFuture = new RPCFuture<>();
            RPCFutureCache.rpcFutureCache.put(request.getRequestId(), syncFuture);
            try {
                return syncFuture.get(timeOut == null ? 1 : timeOut, TimeUnit.MILLISECONDS);
            } finally {
                RPCFutureCache.rpcFutureCache.remove(request.getRequestId());
            }
        } else {
            logger.debug("send message fail, the channel is null !");
        }
        return null;
    }

}
