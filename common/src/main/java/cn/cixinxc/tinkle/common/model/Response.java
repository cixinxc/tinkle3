package cn.cixinxc.tinkle.common.model;

import cn.cixinxc.tinkle.common.enums.ResponseStatusEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/17
 */
public class Response<T> {
  private static final long serialVersionUID = 715745410605631233L;
  private String requestId;
  private Integer code;
  private String info;
  private T data;

  public Response() {
  }

  public Response(String requestId, ResponseStatusEnum responseStatusEnum, String info) {
    this.requestId = requestId;
    this.code = responseStatusEnum.getCode();
    this.info = StringUtils.isBlank(info) ? responseStatusEnum.getInfo() : info;
  }

  public Response(String requestId, ResponseStatusEnum responseStatusEnum, T data) {
    this.requestId = requestId;
    this.code = responseStatusEnum.getCode();
    this.info = responseStatusEnum.getInfo();
    this.data = data;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public static <T> Response<T> success(String requestId, T data) {
    return new Response<T>(requestId, ResponseStatusEnum.SUCCESS, data);
  }

  public static <T> Response<T> fail(String requestId, String info) {
    return new Response<>(requestId, ResponseStatusEnum.FAIL, info);
  }

}
