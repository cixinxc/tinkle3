package cn.cixinxc.tinkle.netty.server;

import cn.cixinxc.tinkle.common.enums.CompressTypeEnum;
import cn.cixinxc.tinkle.common.enums.MessageEnum;
import cn.cixinxc.tinkle.common.model.Message;
import cn.cixinxc.tinkle.common.model.Request;
import cn.cixinxc.tinkle.common.model.Response;
import cn.cixinxc.tinkle.invoke.RequestHandler;
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
//                log.info("server receive msg: [{}] ", msg);
        byte messageType = ((Message) msg).getType();
        Message rpcMessage = new Message();
        rpcMessage.setCompress(CompressTypeEnum.GZIP.getCode());
        if (messageType == MessageEnum.HEARTBEAT_REQUEST.getByte()) {
          rpcMessage.setType(MessageEnum.HEARTBEAT_RESPONSE.getByte());
          rpcMessage.setData(MessageEnum.PONG);
        } else {
          Request rpcRequest = (Request) ((Message<Object>) msg).getData();
          // Execute the target method (the method the client needs to execute) and return the method result
          Object result = RequestHandler.handle(rpcRequest);
//                    log.info(String.format("server get result: %s", result.toString()));
          rpcMessage.setType(MessageEnum.RESPONSE.getByte());
          if (ctx.channel().isActive() && ctx.channel().isWritable()) {
            Response<Object> rpcResponse = Response.success(rpcRequest.getRequestId(), result);
            rpcMessage.setData(rpcResponse);
          } else {
            Response<Object> rpcResponse = Response.fail("", "");
            rpcMessage.setData(rpcResponse);
//                        log.error("not writable now, message dropped");
          }
        }
        ctx.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
      }
    } finally {
      //Ensure that ByteBuf is released, otherwise there may be memory leaks
      ReferenceCountUtil.release(msg);
    }
  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    if (evt instanceof IdleStateEvent) {
      IdleState state = ((IdleStateEvent) evt).state();
      if (state == IdleState.READER_IDLE) {
//                log.info("idle check happen, so close the connection");
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
