package cn.cixinxc.tinkle.common.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/24
 */
public enum CompressTypeEnum {
  GZIP((byte) 1, "gzip"),
  ;

  private final byte code;
  private final String name;

  CompressTypeEnum(byte code, String name) {
    this.code = code;
    this.name = name;
  }

  public byte getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public static final Map<Byte, String> CODE_NAME_MAP;

  static {
    CODE_NAME_MAP = new HashMap<>(CompressTypeEnum.values().length);
    Arrays.stream(CompressTypeEnum.values()).forEach(type -> CODE_NAME_MAP.put(type.code, type.name));
  }

  public static String getName(byte code) {
    return CODE_NAME_MAP.get(code);
  }
}
