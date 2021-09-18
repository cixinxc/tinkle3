package cn.cixinxc.tinkle.netty.server.handeler.process;

import cn.cixinxc.tinkle.common.model.Message;
import cn.cixinxc.tinkle.invoke.RequestHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Cui Xinxin
 * @createDate 2021/1/21
 */
public class DefaultDealProcessor implements RequestDealProcessor {

  @Override
  public byte getType() {
    return (byte) 0;
  }

  @Override
  public Message dealMessage(ChannelHandlerContext ctx, RequestHandler requestHandler, Message requestMessage) {
    return null;
  }
}
