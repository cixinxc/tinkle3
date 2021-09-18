package cn.cixinxc.tinkle.common.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/24
 */
public enum MessageTypeEnum {

  NONE((byte) 0, "none"),
  HEART((byte) 1, "heart"),
  REQUEST((byte) 2, "request"),
  RESPONSE((byte) 3, "response"),

  HEARTBEAT_REQUEST((byte) 11, "heartbeat_request_type"),
  HEARTBEAT_RESPONSE((byte) 12, "heartbeat_response_type"),

  PING((byte) 21, "PING"),
  PONG((byte) 22, "PONG"),
  ;

  private byte type;
  private String desc;

  MessageTypeEnum(byte type, String desc) {
    this.type = type;
    this.desc = desc;
  }

  public byte getType() {
    return this.type;
  }

  public String getDesc() {
    return this.desc;
  }

  private static final Map<Byte, MessageTypeEnum> TYPE_ENUM_MAP;

  static {
    TYPE_ENUM_MAP = new HashMap<>();
    Arrays.asList(MessageTypeEnum.values()).forEach(messageTypeEnum -> {
      TYPE_ENUM_MAP.put(messageTypeEnum.getType(), messageTypeEnum);
    });
  }

  public static MessageTypeEnum typeOf(byte type) {
    return TYPE_ENUM_MAP.getOrDefault(type, NONE);
  }
}
