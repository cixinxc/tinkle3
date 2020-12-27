package cn.cixinxc.tinkle.netty.decoder;

import cn.cixinxc.tinkle.common.enums.CompressTypeEnum;
import cn.cixinxc.tinkle.common.enums.MessageEnum;
import cn.cixinxc.tinkle.common.instance.CommonConstants;
import cn.cixinxc.tinkle.common.model.Message;
import cn.cixinxc.tinkle.common.model.Request;
import cn.cixinxc.tinkle.common.model.Response;
import cn.cixinxc.tinkle.common.serialize.ProtostuffSerializer;
import cn.cixinxc.tinkle.common.serialize.Serializer;
import cn.cixinxc.tinkle.compress.Compress;
import cn.cixinxc.tinkle.compress.GzipCompress;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.Arrays;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/24
 */
public class MessageDecoder extends LengthFieldBasedFrameDecoder {
  public MessageDecoder() {

    this(8 * 1024 * 1024, 5, 4, -9, 0);
  }

  public MessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
                        int lengthAdjustment, int initialBytesToStrip) {
    super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
  }

  @Override
  protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
    byte[] bytes = new byte[in.readableBytes()];
    in.getBytes(in.readerIndex(), bytes);
    Object decoded = super.decode(ctx, in);
    if (decoded instanceof ByteBuf) {
      ByteBuf frame = (ByteBuf) decoded;
      if (frame.readableBytes() >= 16) {
        try {
          return decodeFrame(frame);
        } catch (Exception e) {
        } finally {
          frame.release();
        }
      }
    }
    return decoded;
  }


  private Object decodeFrame(ByteBuf in) {
    // note: must read ByteBuf in order
    checkMagicNumber(in);
    checkVersion(in);
    int fullLength = in.readInt();
    // build RpcMessage object
    byte messageType = in.readByte();
    byte codecType = in.readByte();
    byte compressType = in.readByte();
    int requestId = in.readInt();
    Message rpcMessage = new Message();
    rpcMessage.setCodec(codecType);
    rpcMessage.setRequestId(requestId);
    rpcMessage.setType(messageType);
    if (messageType == MessageEnum.PING.getByte()) {
      rpcMessage.setData("PING");
      return rpcMessage;
    }
    if (messageType == MessageEnum.PONG.getByte()) {
      rpcMessage.setData("pong");
      return rpcMessage;
    }
    int bodyLength = fullLength - 16;
    if (bodyLength > 0) {
      byte[] bs = new byte[bodyLength];
      in.readBytes(bs);
      // decompress the bytes
      String compressName = CompressTypeEnum.getName(compressType);
      Compress compress = new GzipCompress();
      bs = compress.decompress(bs);
      // deserialize the object
      Serializer serializer = new ProtostuffSerializer();

      if (messageType == MessageEnum.REQUEST.getByte()) {
        Request tmpValue = serializer.deserialize(bs, Request.class);
        rpcMessage.setData(tmpValue);
      } else {
        Response tmpValue = serializer.deserialize(bs, Response.class);
        rpcMessage.setData(tmpValue);
      }
    }
    return rpcMessage;

  }

  private void checkVersion(ByteBuf in) {
    // read the version and compare
    byte version = in.readByte();
    // if (version != RpcConstants.VERSION) {
    //   throw new RuntimeException("version isn't compatible" + version);
    // }
  }

  private void checkMagicNumber(ByteBuf in) {
    // read the first 4 bit, which is the magic number, and compare
    int len = CommonConstants.MAGIC_NUMBER.length;
    byte[] tmp = new byte[len];
    in.readBytes(tmp);
    for (int i = 0; i < len; i++) {
      if (tmp[i] != CommonConstants.MAGIC_NUMBER[i]) {
        throw new IllegalArgumentException("Unknown magic code: " + Arrays.toString(tmp));
      }
    }
  }
}
