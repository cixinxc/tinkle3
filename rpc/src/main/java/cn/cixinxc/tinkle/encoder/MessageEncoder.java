package cn.cixinxc.tinkle.encoder;

import cn.cixinxc.tinkle.common.enums.CompressTypeEnum;
import cn.cixinxc.tinkle.common.enums.MessageTypeEnum;
import cn.cixinxc.tinkle.common.instance.CommonConstants;
import cn.cixinxc.tinkle.common.model.Message;
import cn.cixinxc.tinkle.common.serialize.ProtostuffSerializer;
import cn.cixinxc.tinkle.common.serialize.Serializer;
import cn.cixinxc.tinkle.compress.api.Compress;
import cn.cixinxc.tinkle.compress.impl.GzipCompress;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/24
 */
public class MessageEncoder extends MessageToByteEncoder<Message> {

  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(0);
  public static final byte VERSION = 1;
  @Override
  protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
    byte messageType = msg.getType();
    try {
      out.writeBytes(CommonConstants.MAGIC_NUMBER);
      out.writeByte(VERSION);
      out.writerIndex(out.writerIndex() + 4);
      out.writeByte(messageType);
      out.writeByte(msg.getCodec());
      out.writeByte(CompressTypeEnum.GZIP.getCode());
      out.writeInt(ATOMIC_INTEGER.getAndIncrement());
      // build full length
      byte[] bodyBytes = null;
      int fullLength = 16;
      // if messageType is not heartbeat message,fullLength = head length + body length
      if (messageType != MessageTypeEnum.HEARTBEAT_REQUEST.getType() && messageType != MessageTypeEnum.HEARTBEAT_RESPONSE.getType()) {
        String codecName = CompressTypeEnum.getName(msg.getCodec());
        Serializer serializer = new ProtostuffSerializer();
        bodyBytes = serializer.serialize(msg.getData());
        // compress the bytes
        String compressName = CompressTypeEnum.getName(msg.getCompress());
        Compress compress = new GzipCompress();
        bodyBytes = compress.compress(bodyBytes);
        fullLength += bodyBytes.length;
      }

      if (bodyBytes != null) {
        out.writeBytes(bodyBytes);
      }
      int writeIndex = out.writerIndex();
      out.writerIndex(writeIndex - fullLength + CommonConstants.MAGIC_NUMBER.length + 1);
      out.writeInt(fullLength);
      out.writerIndex(writeIndex);
    } catch (Exception e) {
      logger.error("encode error.", e);
    }
  }

}
