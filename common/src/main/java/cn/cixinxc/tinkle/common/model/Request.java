package cn.cixinxc.tinkle.common.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/17
 */
public class Request implements Serializable {
  private static final long serialVersionUID = 761336506500754408L;

  private String requestId;
  private InvokeProperties invokeProperties;
  private ServiceProperties serviceProperties;

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public InvokeProperties getInvokeProperties() {
    return invokeProperties;
  }

  public void setInvokeProperties(InvokeProperties invokeProperties) {
    this.invokeProperties = invokeProperties;
  }

  public ServiceProperties getServiceProperties() {
    return serviceProperties;
  }

  public void setServiceProperties(ServiceProperties serviceProperties) {
    this.serviceProperties = serviceProperties;
  }

  public String getMethodName() {
    return Objects.isNull(invokeProperties) ? null : invokeProperties.getMethodName();
  }

  public Class<?>[]  getParamTypes(){
    return Objects.isNull(invokeProperties) ? null : invokeProperties.getParamTypes();
  }

  public Object[] getParameters(){
    return Objects.isNull(invokeProperties) ? null : invokeProperties.getParameters();
  }
}
