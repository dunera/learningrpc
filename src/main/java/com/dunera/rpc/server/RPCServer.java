package com.dunera.rpc.server;

import com.dunera.rpc.exception.RPCException;
import com.dunera.rpc.registry.Registry;
import com.dunera.rpc.registry.RegistryFactory;
import com.dunera.rpc.transport.message.ServerMessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class RPCServer extends Server {

    private final static Logger logger = LoggerFactory.getLogger(RPCServer.class);
    private volatile boolean started = false;
    private volatile boolean stopped = true;

    /**
     * 用于分配处理业务线程的线程组个数
     */
    private static final int GROUP_SIZE = Runtime.getRuntime().availableProcessors() * 2;

    /**
     * 业务出现线程大小
     */
    private static final int THREAD_SIZE = 4;

    /*
     * NioEventLoopGroup实际上就是个线程,
     * NioEventLoopGroup在后台启动了n个NioEventLoop来处理Channel事件,
     * 每一个NioEventLoop负责处理m个Channel,
     * NioEventLoopGroup从NioEventLoop数组里挨个取出NioEventLoop来处理Channel
     */
    private static final EventLoopGroup bossGroup = new NioEventLoopGroup(GROUP_SIZE);

    private static final EventLoopGroup workerGroup = new NioEventLoopGroup(THREAD_SIZE);

    public RPCServer(ServerConfig serverConfig) {
        super(serverConfig);
    }

    public RPCServer(ServerConfig serverConfig, ServiceProviderInfo<?> provider) {
        super(serverConfig, provider);
    }

    public RPCServer(ServerConfig serverConfig, List<ServiceProviderInfo<?>> providers) {
        super(serverConfig, providers);
    }

    @Override
    public void start() throws RPCException {
        if (started) {
            logger.warn("server already started!");
            return;
        }
        // TODO 扫描注解，获取服务提供者信息，放到缓存中  addServiceProvider();
        // 注册到注册中心
        if (serverConfig.getRegistryConfig() != null) {
            register();
        }
        // 启动服务监听
        logger.info("netty tcp service running.....");
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup);
        b.channel(NioServerSocketChannel.class);
        b.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
                pipeline.addLast("decoder", new ByteArrayDecoder());
                pipeline.addLast("encoder", new ByteArrayEncoder());
                pipeline.addLast(new ServerMessageHandler());
            }
        });
        try {
            b.bind(serverConfig.getHost(), serverConfig.getPort()).sync();
        } catch (InterruptedException e) {
            throw new RPCException("start service failed! " + e.getMessage());
        }
        started = true;
        logger.info("netty tcp service start success on port : " + serverConfig.getPort());
    }

    /**
     * 停止服务并释放资源
     */
    @Override
    public void stop() {
        if (stopped) {
            return;
        }
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        stopped = true;
    }

    private void register() {
        // 注册服务到注册中心
        Registry registry = RegistryFactory.getRegistry(serverConfig.getRegistryConfig());
        registry.init();
        for (Map.Entry<String, ServiceProviderInfo<?>> entry : serviceProviderInfoCache.entrySet()) {
            registry.register(entry.getValue());
        }
    }

}