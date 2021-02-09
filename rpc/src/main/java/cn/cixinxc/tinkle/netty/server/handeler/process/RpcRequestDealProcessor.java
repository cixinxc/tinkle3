package cn.cixinxc.tinkle.netty.server.handeler.process;

import cn.cixinxc.tinkle.common.enums.MessageTypeEnum;
import cn.cixinxc.tinkle.common.model.Message;
import cn.cixinxc.tinkle.common.model.Request;
import cn.cixinxc.tinkle.common.model.Response;
import cn.cixinxc.tinkle.invoke.RequestHandler;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 * @author Cui Xinxin
 * @createDate 2021/1/21
 */
@Component
public class RpcRequestDealProcessor implements RequestDealProcessor {

  @Override
  public byte getType() {
    return MessageTypeEnum.HEARTBEAT_REQUEST.getType();
  }

  @Override
  public Message dealMessage(ChannelHandlerContext ctx, RequestHandler requestHandler, Message requestMessage) {
    Message message = RequestDealProcessor.pre();
    Request request = (Request) ((Message) requestMessage).getData();
    Object result = requestHandler.handle(request);
    message.setType(MessageTypeEnum.RESPONSE.getType());
    if (ctx.channel().isActive() && ctx.channel().isWritable()) {
      Response<Object> rpcResponse = Response.success(request.getRequestId(), result);
      message.setData(rpcResponse);
    } else {
      Response<Object> rpcResponse = Response.fail("", "");
      message.setData(rpcResponse);
    }
    return message;
  }
}
