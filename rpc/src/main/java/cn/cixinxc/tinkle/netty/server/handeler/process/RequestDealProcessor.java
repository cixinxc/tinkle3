package cn.cixinxc.tinkle.netty.server.handeler.process;

import cn.cixinxc.tinkle.common.enums.CompressTypeEnum;
import cn.cixinxc.tinkle.common.model.Message;
import cn.cixinxc.tinkle.invoke.RequestHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Cui Xinxin
 * @createDate 2021/1/21
 */
public interface RequestDealProcessor {

  byte getType();

  Message dealMessage(ChannelHandlerContext ctx, RequestHandler requestHandler, Message message);

  static Message pre() {
    Message message = new Message();
    message.setCompress(CompressTypeEnum.GZIP.getCode());
    return message;
  }

}
