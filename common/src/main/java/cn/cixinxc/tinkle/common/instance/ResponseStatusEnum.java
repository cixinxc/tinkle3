package cn.cixinxc.tinkle.common.instance;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/18
 */
public enum ResponseStatusEnum {
  SUCCESS(200, "success"),
  FAIL(500, "fail");

  private final int code;

  private final String info;

  ResponseStatusEnum(int code, String info) {
    this.code = code;
    this.info = info;
  }

  public int getCode() {
    return code;
  }

  public String getInfo() {
    return info;
  }
}
