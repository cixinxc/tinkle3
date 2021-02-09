package cn.cixinxc.tinkle.common.model;

import java.io.Serializable;

public class InvokeProperties implements Serializable {

  private static final long serialVersionUID = -4566056608889651858L;

  private String methodName;
  private Object[] parameters;
  private Class<?>[] paramTypes;

  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public Object[] getParameters() {
    return parameters;
  }

  public void setParameters(Object[] parameters) {
    this.parameters = parameters;
  }

  public Class<?>[] getParamTypes() {
    return paramTypes;
  }

  public void setParamTypes(Class<?>[] paramTypes) {
    this.paramTypes = paramTypes;
  }

  public InvokeProperties() {
  }

}
