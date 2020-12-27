package cn.cixinxc.tinkle.common.model;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/24
 */
public class Message<T> {
  /**
   * @see cn.cixinxc.tinkle.common.enums.MessageEnum
   */
  private byte type;
  private byte codec;
  private byte compress;
  private int requestId;
  private T data;

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

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }
}
