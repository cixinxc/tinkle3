package cn.cixinxc.tinkle.netty.client;

import cn.cixinxc.tinkle.common.enums.CompressTypeEnum;
import cn.cixinxc.tinkle.common.enums.MessageTypeEnum;
import cn.cixinxc.tinkle.common.model.Message;
import cn.cixinxc.tinkle.common.model.Request;
import cn.cixinxc.tinkle.common.model.Response;
import cn.cixinxc.tinkle.common.model.ServiceProperties;
import cn.cixinxc.tinkle.decoder.MessageDecoder;
import cn.cixinxc.tinkle.encoder.MessageEncoder;
import cn.cixinxc.tinkle.service.provider.ServiceProvider;
import cn.cixinxc.tinkle.service.provider.ServiceProviderImpl;
import cn.cixinxc.tinkle.service.register.RegisterService;
import cn.cixinxc.tinkle.service.register.ZookeeperRegisterService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/23
 */
public class NettyClient<T> {

  private final RegisterService registerService;
  private final UnprocessedRequests unprocessedRequests = new UnprocessedRequests();
  private final ServiceProvider serviceProvider;
  private final ChannelProvider channelProvider;
  private final Bootstrap bootstrap;
  private final EventLoopGroup eventLoopGroup;


  public NettyClient() {
    registerService = new ZookeeperRegisterService();
    serviceProvider = ServiceProviderImpl.getInstance();
    eventLoopGroup = new NioEventLoopGroup();
    bootstrap = new Bootstrap();
    bootstrap.group(eventLoopGroup)
            .channel(NioSocketChannel.class)
            .handler(new LoggingHandler(LogLevel.INFO))
            //  The timeout period of the connection.
            //  If this time is exceeded or the connection cannot be established, the connection fails.
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .handler(new ChannelInitializer<SocketChannel>() {
              @Override
              protected void initChannel(SocketChannel ch) {
                ChannelPipeline p = ch.pipeline();
                // If no data is sent to the server within 15 seconds, a heartbeat request is sent
                p.addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
                p.addLast(new MessageEncoder());
                p.addLast(new MessageDecoder());
                p.addLast(new NettyClientHandler());
              }
            });
    this.channelProvider = new ChannelProvider();
  }

  public CompletableFuture<Response<T>> sendRpcRequest(Request rpcRequest) {
    // build return value
    CompletableFuture<Response<T>> resultFuture = new CompletableFuture<>();
    // build rpc service name by rpcRequest
    ServiceProperties properties = rpcRequest.getServiceProperties();
    // get server address
    InetSocketAddress inetSocketAddress = registerService.lookup(properties);
    // get  server address related channel
    Channel channel = getChannel(inetSocketAddress);
    if (channel.isActive()) {
      // put unprocessed request
      unprocessedRequests.put(rpcRequest.getRequestId(), resultFuture);
      Message message = new Message();
      message.setData(rpcRequest);

      message.setCompress(CompressTypeEnum.GZIP.getCode());
      message.setType(MessageTypeEnum.REQUEST.getType());
      channel.writeAndFlush(message).addListener((ChannelFutureListener) future -> {
        if (future.isSuccess()) {

        } else {
          future.channel().close();
          resultFuture.completeExceptionally(future.cause());
        }
      });
    } else {
      throw new IllegalStateException();
    }

    return resultFuture;
  }

  public Channel getChannel(InetSocketAddress inetSocketAddress) {
    Channel channel = channelProvider.get(inetSocketAddress);
    if (channel == null) {
      try {
        channel = doConnect(inetSocketAddress);
      } catch (ExecutionException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      channelProvider.set(inetSocketAddress, channel);
    }
    return channel;
  }

  public void close() {
    eventLoopGroup.shutdownGracefully();
  }

  public Channel doConnect(InetSocketAddress inetSocketAddress) throws ExecutionException, InterruptedException {
    CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
    bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
      if (future.isSuccess()) {
//                log.info("The client has connected [{}] successful!", inetSocketAddress.toString());
        completableFuture.complete(future.channel());
      } else {
        throw new IllegalStateException();
      }
    });
    return completableFuture.get();
  }


}