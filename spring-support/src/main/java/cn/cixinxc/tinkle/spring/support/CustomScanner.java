package cn.cixinxc.tinkle.spring.support;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;


public class CustomScanner extends ClassPathBeanDefinitionScanner {

  public CustomScanner(BeanDefinitionRegistry registry, Class<? extends Annotation> type) {
    super(registry);
    super.addIncludeFilter(new AnnotationTypeFilter(type));
  }

  @Override
  public int scan(String... basePackages) {
    return super.scan(basePackages);
  }
}