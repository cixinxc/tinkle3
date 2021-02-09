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

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component
public class TinkleAnnotationPostProcessor implements BeanPostProcessor {

  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final ServiceProvider serviceProvider;
  private final NettyClient rpcClient;

  public TinkleAnnotationPostProcessor() {
    this.serviceProvider = ServiceProviderImpl.getInstance();
    this.rpcClient = new NettyClient();
  }

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    // 服务发布
    if (bean.getClass().getAnnotation(Tinkle.class) != null) {
      serviceProvider.publishService(bean);
    }
    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    // 代理被@TinkleClient修饰的属性
    Arrays.stream(bean.getClass().getDeclaredFields())
            .collect(Collectors.toMap(field -> field.getAnnotation(TinkleClient.class), Function.identity(), (k1, k2) -> k1))
            .forEach((annotation, field) -> {
              if (annotation == null) {
                return;
              }
              var serviceProperties = new ServiceProperties(annotation, field.getType().getCanonicalName());
              var clientProxy = new RpcClientProxy(rpcClient, serviceProperties).getProxy(field.getType());
              field.setAccessible(true);
              try {
                field.set(bean, clientProxy);
              } catch (IllegalAccessException e) {
                logger.error("[{}-{}] set clientProxy error.", beanName, bean, e);
              }
            });
    return bean;
  }
}
