package cn.cixinxc.tinkle.spring.support;

import cn.cixinxc.tinkle.common.annotation.Tinkle;
import cn.cixinxc.tinkle.common.annotation.TinkleClient;
import cn.cixinxc.tinkle.common.model.ServiceProperties;
import cn.cixinxc.tinkle.netty.client.NettyClient;
import cn.cixinxc.tinkle.proxy.RpcClientProxy;
import cn.cixinxc.tinkle.service.provider.ServiceProvider;
import cn.cixinxc.tinkle.service.provider.ServiceProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Component
public class TinkleAnnotationPostProcessor implements BeanPostProcessor {

  private static final Logger logger = LoggerFactory.getLogger(TinkleAnnotationPostProcessor.class);

  private final ServiceProvider serviceProvider;
  private final NettyClient<Object> rpcClient;

  public TinkleAnnotationPostProcessor() {
    this.serviceProvider = ServiceProviderImpl.getInstance();
    this.rpcClient = new NettyClient<Object>();
  }

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    var annotation = bean.getClass().getAnnotation(Tinkle.class);

    if (annotation != null) {
      serviceProvider.publishService(bean);
    }
    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    Arrays.stream(bean.getClass().getDeclaredFields()).forEach(declaredField -> {
      var annotation = declaredField.getAnnotation(TinkleClient.class);
      if (annotation != null) {
        Class<?> serviceRelatedInterface = declaredField.getType();
        var serviceName = serviceRelatedInterface.getCanonicalName();
        var serviceProperties = new ServiceProperties(annotation, serviceName);
        var rpcClientProxy = new RpcClientProxy(rpcClient, serviceProperties);
        var clientProxy = rpcClientProxy.getProxy(declaredField.getType());
        declaredField.setAccessible(true);
        try {
          declaredField.set(bean, clientProxy);
        } catch (IllegalAccessException e) {
          logger.error("[{}] set clientProxy error.", bean, e);
        }
      }
    });
    return bean;
  }
}
