package cn.cixinxc.tinkle.netty.server;

import cn.cixinxc.tinkle.netty.decoder.MessageDecoder;
import cn.cixinxc.tinkle.netty.encoder.MessageEncoder;
import cn.cixinxc.tinkle.service.ServiceProvider;
import cn.cixinxc.tinkle.service.ServiceProviderImpl;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/23
 */
public class NettyServer {
  public static final int PORT = 9090;

  private final ServiceProvider serviceProvider = ServiceProviderImpl.getInstance();

  public void registerService(Object service) {
    serviceProvider.publishService(service);
  }

  public void start() {
    CustomShutdownHook.getCustomShutdownHook().clearAll();
    String host = null;
    try {
      host = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      e.printStackTrace();
      return;
    }
    EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    DefaultEventExecutorGroup serviceHandlerGroup = new DefaultEventExecutorGroup(6);
    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(bossGroup, workerGroup)
              .channel(NioServerSocketChannel.class)
              .childOption(ChannelOption.TCP_NODELAY, true)
              .childOption(ChannelOption.SO_KEEPALIVE, true)
              .option(ChannelOption.SO_BACKLOG, 128)
//              .handler(new LoggingHandler(LogLevel.INFO))
              .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                  ChannelPipeline p = ch.pipeline();
                  p.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
                  p.addLast(new MessageEncoder());
                  p.addLast(new MessageDecoder());
                  p.addLast(serviceHandlerGroup, new NettyServerHandler());
                }
              });

      ChannelFuture f = b.bind(host, PORT).sync();
      f.channel().closeFuture().sync();
    } catch (InterruptedException e) {
//      log.error("occur exception when start server:", e);
    } finally {
//      log.error("shutdown bossGroup and workerGroup");
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
      serviceHandlerGroup.shutdownGracefully();
    }
  }
}