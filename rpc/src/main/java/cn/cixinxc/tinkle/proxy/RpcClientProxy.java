package cn.cixinxc.tinkle.proxy;


import cn.cixinxc.tinkle.common.model.InvokeProperties;
import cn.cixinxc.tinkle.common.model.Request;
import cn.cixinxc.tinkle.common.model.Response;
import cn.cixinxc.tinkle.common.model.ServiceProperties;
import cn.cixinxc.tinkle.common.utils.Builder;
import cn.cixinxc.tinkle.netty.client.NettyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class RpcClientProxy<T> implements InvocationHandler {

  private static final Logger logger = LoggerFactory.getLogger(RpcClientProxy.class);


  private final NettyClient nettyClient;
  private final ServiceProperties serviceProperties;

  public RpcClientProxy(NettyClient nettyClient, ServiceProperties serviceProperties) {
    this.nettyClient = nettyClient;
    serviceProperties.setGroupName(serviceProperties.getGroupName() == null ? "" : serviceProperties.getGroupName());
    serviceProperties.setVersion(serviceProperties.getVersion() == null ? "" : serviceProperties.getVersion());
    this.serviceProperties = serviceProperties;
  }


  @SuppressWarnings("unchecked")
  public T getProxy(Class<T> clazz) {
    return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
  }

  @Override
  public T invoke(Object proxy, Method method, Object[] args) {
    var properties = Builder.of(InvokeProperties::new)
            .with(InvokeProperties::setMethodName, method.getName())
            .with(InvokeProperties::setParameters, args)
            .with(InvokeProperties::setParamTypes, method.getParameterTypes())
            .build();

    var rpcRequest = Builder.of(Request::new)
            .with(Request::setRequestId, UUID.randomUUID().toString())
            .with(Request::setServiceProperties, serviceProperties)
            .with(Request::setInvokeProperties, properties)
            .build();

    Response<T> rpcResponse = null;
    CompletableFuture<Response<T>> completableFuture = nettyClient.sendRpcRequest(rpcRequest);
    try {
      rpcResponse = completableFuture.get();
    } catch (InterruptedException | ExecutionException e) {
      logger.error("invoke error.proxy:{}, method:{}, args:{}.", proxy.getClass().getCanonicalName(), method.getName(), args, e);
    }
    preCheck(rpcResponse, rpcRequest);
    return rpcResponse == null ? null : rpcResponse.getData();
  }

  private void preCheck(Response<T> response, Request request) {
    if (response == null) {
      logger.error("response is null.");
      return;
    }
    if (request == null) {
      logger.error("request is null.");
      return;
    }
    if (request.getRequestId() != null && !request.getRequestId().equals(response.getRequestId())) {
      logger.error("request is not adapt response.");
    }
  }
}
