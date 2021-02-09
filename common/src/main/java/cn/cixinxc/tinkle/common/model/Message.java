package cn.cixinxc.tinkle.common.model;

import cn.cixinxc.tinkle.common.enums.MessageTypeEnum;

import java.io.Serializable;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/24
 */
public class Message implements Serializable {
  private static final long serialVersionUID = -6772821163040259355L;
  /**
   * @see MessageTypeEnum
   */
  private byte type;
  private byte codec;
  private byte compress;
  private int requestId;
  private Object data;

  public Message() {

  }

  public byte getType() {
    return type;
  }

  public void setType(byte type) {
    this.type = type;
  }

  public byte getCodec() {
    return codec;
  }

  public void setCodec(byte codec) {
    this.codec = codec;
  }

  public byte getCompress() {
    return compress;
  }

  public void setCompress(byte compress) {
    this.compress = compress;
  }

  public int getRequestId() {
    return requestId;
  }

  public void setRequestId(int requestId) {
    this.requestId = requestId;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }
}
