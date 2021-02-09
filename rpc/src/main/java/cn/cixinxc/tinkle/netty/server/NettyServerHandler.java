package cn.cixinxc.tinkle.netty.server;

import cn.cixinxc.tinkle.common.enums.MessageTypeEnum;
import cn.cixinxc.tinkle.common.model.Message;
import cn.cixinxc.tinkle.invoke.RequestHandler;
import cn.cixinxc.tinkle.netty.server.handeler.process.HearBeatRequestDealProcessor;
import cn.cixinxc.tinkle.netty.server.handeler.process.RpcRequestDealProcessor;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/27
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

  private final RequestHandler requestHandler;

  public NettyServerHandler() {
    this.requestHandler = new RequestHandler();
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    try {
      if (msg instanceof Message) {
        Message requestMsg = (Message) msg;
        Message message;
        if (requestMsg.getType() == MessageTypeEnum.HEARTBEAT_REQUEST.getType()) {
          message = new HearBeatRequestDealProcessor().dealMessage(ctx, requestHandler, requestMsg);
        } else {
          message = new RpcRequestDealProcessor().dealMessage(ctx, requestHandler, requestMsg);
        }
        ctx.writeAndFlush(message).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
      }
    } finally {
      ReferenceCountUtil.release(msg);
    }
  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    if (evt instanceof IdleStateEvent) {
      IdleState state = ((IdleStateEvent) evt).state();
      if (state == IdleState.READER_IDLE) {
        ctx.close();
      }
    } else {
      super.userEventTriggered(ctx, evt);
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//        log.error("server catch exception");
    cause.printStackTrace();
    ctx.close();
  }
}
