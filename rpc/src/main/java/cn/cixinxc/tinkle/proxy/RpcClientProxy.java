package cn.cixinxc.tinkle.proxy;


import cn.cixinxc.tinkle.common.model.InvokeProperty;
import cn.cixinxc.tinkle.common.model.RpcRequest;
import cn.cixinxc.tinkle.common.model.Response;
import cn.cixinxc.tinkle.common.model.ServiceProperty;
import cn.cixinxc.tinkle.common.utils.Builder;
import cn.cixinxc.tinkle.netty.client.NettyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class RpcClientProxy implements InvocationHandler {

  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final NettyClient nettyClient;
  private final ServiceProperty serviceProperty;

  public RpcClientProxy(NettyClient nettyClient, ServiceProperty serviceProperty) {
    this.nettyClient = nettyClient;
    serviceProperty.setGroupName(serviceProperty.getGroupName() == null ? "" : serviceProperty.getGroupName());
    serviceProperty.setVersion(serviceProperty.getVersion() == null ? "" : serviceProperty.getVersion());
    this.serviceProperty = serviceProperty;
  }


  @SuppressWarnings("unchecked")
  public Object getProxy(Class<?> clazz) {
    return Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) {
    InvokeProperty properties = Builder.of(InvokeProperty::new)
            .with(InvokeProperty::setMethodName, method.getName())
            .with(InvokeProperty::setParameters, args)
            .with(InvokeProperty::setParamTypes, method.getParameterTypes())
            .build();

    RpcRequest rpcRequest = Builder.of(RpcRequest::new)
            .with(RpcRequest::setRequestId, UUID.randomUUID().toString())
            .with(RpcRequest::setServiceProperties, serviceProperty)
            .with(RpcRequest::setInvokeProperties, properties)
            .build();

    Response rpcResponse = null;
    CompletableFuture<Response> completableFuture = nettyClient.sendRpcRequest(rpcRequest);
    try {
      rpcResponse = completableFuture.get();
    } catch (InterruptedException | ExecutionException e) {
      logger.error("invoke error.proxy:{}, method:{}, args:{}.", proxy.getClass().getCanonicalName(), method.getName(), args, e);
    }
    preCheck(rpcResponse, rpcRequest);
    return rpcResponse == null ? null : rpcResponse.getData();
  }

  private void preCheck(Response response, RpcRequest rpcRequest) {
    if (response == null) {
      logger.error("response is null.");
      return;
    }
    if (rpcRequest == null) {
      logger.error("request is null.");
      return;
    }
    if (rpcRequest.getRequestId() != null && !rpcRequest.getRequestId().equals(response.getRequestId())) {
      logger.error("request is not adapt response.");
    }
  }
}
