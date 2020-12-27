package cn.cixinxc.tinkle.netty.client;

import cn.cixinxc.tinkle.common.enums.CompressTypeEnum;
import cn.cixinxc.tinkle.common.enums.MessageEnum;
import cn.cixinxc.tinkle.common.model.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

import java.net.InetSocketAddress;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/25
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

  private final NettyClient nettyRpcClient;

  public NettyClientHandler() {
    this.nettyRpcClient = new NettyClient();
  }

  /**
   * Read the message transmitted by the server
   */
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    try {
//            log.info("client receive msg: [{}]", msg);
      if (msg instanceof Message) {
        Message tmp = (Message) msg;
        byte messageType = tmp.getType();
        if (messageType == MessageEnum.HEARTBEAT_RESPONSE.getByte()) {
//                    log.info("heart [{}]", tmp.getData());
        } else if (messageType == MessageEnum.RESPONSE.getByte()) {
          Message<Object> rpcResponse = (Message<Object>) tmp.getData();
        }
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
        rpcMessage.setType(MessageEnum.HEARTBEAT_REQUEST.getByte());
        rpcMessage.setData(MessageEnum.PING);
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
