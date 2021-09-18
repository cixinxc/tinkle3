package cn.cixinxc.tinkle.common.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/17
 */
public class RpcRequest implements Serializable {
  private static final long serialVersionUID = 761336506500754408L;

  private String requestId;
  private InvokeProperty invokeProperty;
  private ServiceProperty serviceProperty;

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public InvokeProperty getInvokeProperties() {
    return invokeProperty;
  }

  public void setInvokeProperties(InvokeProperty invokeProperty) {
    this.invokeProperty = invokeProperty;
  }

  public ServiceProperty getServiceProperties() {
    return serviceProperty;
  }

  public void setServiceProperties(ServiceProperty serviceProperty) {
    this.serviceProperty = serviceProperty;
  }

  public String getMethodName() {
    return Objects.isNull(invokeProperty) ? "" : invokeProperty.getMethodName();
  }

  public Class<?>[] getParamTypes() {
    return Objects.isNull(invokeProperty) ? new Class[]{} : invokeProperty.getParamTypes();
  }

  public Object[] getParameters() {
    return Objects.isNull(invokeProperty) ? new Object[]{} : invokeProperty.getParameters();
  }

  @Override
  public String toString() {
    return "";
  }

  public RpcRequest() {
  }

}
