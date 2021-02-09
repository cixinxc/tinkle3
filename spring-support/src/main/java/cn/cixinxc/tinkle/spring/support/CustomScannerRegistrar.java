package cn.cixinxc.tinkle.spring.support;


import cn.cixinxc.tinkle.common.annotation.Tinkle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;


public class CustomScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final String SPRING_BEAN_BASE_PACKAGE = "cn.cixinxc.tinkle";
  private static final String BASE_PACKAGE_ATTRIBUTE_NAME = "basePackage";
  private ResourceLoader resourceLoader;

  @Override
  public void setResourceLoader(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  @Override
  public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
    //
    AnnotationAttributes rpcScanAnnotationAttributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(RpcScan.class.getName()));
    String[] rpcScanBasePackages = new String[0];
    if (rpcScanAnnotationAttributes != null) {
      // get the value of the basePackage property
      rpcScanBasePackages = rpcScanAnnotationAttributes.getStringArray(BASE_PACKAGE_ATTRIBUTE_NAME);
    }
    if (rpcScanBasePackages.length == 0) {
      rpcScanBasePackages = new String[]{((StandardAnnotationMetadata) annotationMetadata).getIntrospectedClass().getPackage().getName()};
    }
    // Scan the RpcService annotation
    CustomScanner rpcServiceScanner = new CustomScanner(beanDefinitionRegistry, Tinkle.class);
    // Scan the Component annotation
    CustomScanner springBeanScanner = new CustomScanner(beanDefinitionRegistry, Component.class);
    if (resourceLoader != null) {
      rpcServiceScanner.setResourceLoader(resourceLoader);
      springBeanScanner.setResourceLoader(resourceLoader);
    }
    int springBeanAmount = springBeanScanner.scan(SPRING_BEAN_BASE_PACKAGE);
    logger.info("springBeanScanners scan [{}] beans.", springBeanAmount);
    int rpcServiceCount = rpcServiceScanner.scan(rpcScanBasePackages);
    logger.info("rpcServiceScanners scan [{}] beans.", rpcServiceCount);
  }

}
