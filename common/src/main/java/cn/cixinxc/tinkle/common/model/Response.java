package cn.cixinxc.tinkle.common.model;

import cn.cixinxc.tinkle.common.enums.ResponseStatusEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/17
 */
public class Response {
  private static final long serialVersionUID = 715745410605631233L;
  private String requestId;
  private Integer code;
  private String info;
  private Object data;

  public Response() {
  }

  public Response(String requestId, ResponseStatusEnum responseStatusEnum, String info) {
    this.requestId = requestId;
    this.code = responseStatusEnum.getCode();
    this.info = StringUtils.isBlank(info) ? responseStatusEnum.getInfo() : info;
  }

  public Response(String requestId, ResponseStatusEnum responseStatusEnum, Object data) {
    this.requestId = requestId;
    this.code = responseStatusEnum.getCode();
    this.info = responseStatusEnum.getInfo();
    this.data = data;
  }

  public static Response success(String requestId, Object data) {
    return new Response(requestId, ResponseStatusEnum.SUCCESS, data);
  }

  public static  Response fail(String requestId, String info) {
    return new Response(requestId, ResponseStatusEnum.FAIL, info);
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

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

}
