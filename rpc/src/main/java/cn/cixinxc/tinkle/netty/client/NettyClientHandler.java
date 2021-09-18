package cn.cixinxc.tinkle.netty.client;

import cn.cixinxc.tinkle.common.enums.CompressTypeEnum;
import cn.cixinxc.tinkle.common.enums.MessageTypeEnum;
import cn.cixinxc.tinkle.common.model.Message;
import cn.cixinxc.tinkle.common.model.Response;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.InetSocketAddress;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/25
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final UnprocessedRequest unprocessedRequest = new UnprocessedRequest();
  private final NettyClient nettyRpcClient;

  public NettyClientHandler() {
    this.nettyRpcClient = new NettyClient();
  }

  /**
   * Read the message transmitted by the server
   */
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    logger.info("client receive msg:[{}].", msg);
    if (!(msg instanceof Message)) {
      return;
    }
    try {
      Message message = (Message) msg;
      MessageTypeEnum messageType = MessageTypeEnum.typeOf(message.getType());
      switch (messageType) {
        case HEARTBEAT_RESPONSE:
          logger.info("heart [{}]", message.getData());
          break;
        case RESPONSE:
          Response rpcResponse = (Response) message.getData();
          unprocessedRequest.complete(rpcResponse);
        default:
          throw new IllegalStateException("Unexpected value: " + messageType);
      }
    } finally {
      ReferenceCountUtil.release(msg);
    }
  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    if (evt instanceof IdleStateEvent) {
      IdleState state = ((IdleStateEvent) evt).state();
      if (state == IdleState.WRITER_IDLE) {
//                log.info("write idle happen [{}]", ctx.channel().remoteAddress());
        Channel channel = nettyRpcClient.getChannel((InetSocketAddress) ctx.channel().remoteAddress());
        Message rpcMessage = new Message();
        rpcMessage.setCompress(CompressTypeEnum.GZIP.getCode());
        rpcMessage.setType(MessageTypeEnum.HEARTBEAT_REQUEST.getType());
        rpcMessage.setData(MessageTypeEnum.PING);
        channel.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
      }
    } else {
      super.userEventTriggered(ctx, evt);
    }
  }

  /**
   * Called when an exception occurs in processing a client message
   */
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//        log.error("client catch exception", cause);
    cause.printStackTrace();
    ctx.close();
  }
}
