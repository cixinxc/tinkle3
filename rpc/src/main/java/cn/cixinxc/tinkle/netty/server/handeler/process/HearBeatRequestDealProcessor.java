package cn.cixinxc.tinkle.netty.server.handeler.process;

import cn.cixinxc.tinkle.common.enums.MessageTypeEnum;
import cn.cixinxc.tinkle.common.model.Message;
import cn.cixinxc.tinkle.invoke.RequestHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Cui Xinxin
 * @createDate 2021/1/21
 */
public class HearBeatRequestDealProcessor implements RequestDealProcessor {

  @Override
  public byte getType() {
    return MessageTypeEnum.HEARTBEAT_REQUEST.getType();
  }

  @Override
  public Message dealMessage(ChannelHandlerContext ctx, RequestHandler requestHandler, Message requestMessage) {
    Message message = RequestDealProcessor.pre();
    message.setType(MessageTypeEnum.HEARTBEAT_RESPONSE.getType());
    message.setData(MessageTypeEnum.PONG);
    return message;
  }
}
