package cn.cixinxc.tinkle.common.enums;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/24
 */
public enum MessageEnum {

  HEART((byte) 0, "heart"),
  REQUEST((byte) 1, "request"),
  RESPONSE((byte) 2, "response"),

  HEARTBEAT_REQUEST((byte) 11, "heartbeat_request_type"),
  HEARTBEAT_RESPONSE((byte) 12, "heartbeat_response_type"),

  PING((byte) 21, "PING"),
  PONG((byte) 22, "PONG"),
  ;

  private byte type;
  private String desc;

  public byte getByte() {
    return this.getByte();
  }

  MessageEnum(byte type, String desc) {
    this.type = type;
    this.desc = desc;
  }
}
