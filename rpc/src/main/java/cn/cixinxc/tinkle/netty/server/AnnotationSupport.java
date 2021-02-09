package cn.cixinxc.tinkle.netty.server;

import cn.cixinxc.tinkle.common.annotation.Tinkle;
import cn.cixinxc.tinkle.common.annotation.TinkleClient;
import cn.cixinxc.tinkle.service.provider.ServiceProvider;
import cn.cixinxc.tinkle.service.provider.ServiceProviderImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/30
 */
public class AnnotationSupport {
  public static Map<String, Object> INSTANCE_MAP = new HashMap<>();

  private static final ServiceProvider serviceProvider = ServiceProviderImpl.getInstance();


  public static void loadAnnotationClass(String basePath, Class<? extends Annotation> annotation) {
    Reflections reflections = new Reflections(basePath);
    Set<Class<?>> classes = reflections.getTypesAnnotatedWith(annotation);
    if (CollectionUtils.isNotEmpty(classes)) {
      classes.forEach(clazz -> {
        try {
          Object service = clazz.getConstructor().newInstance();
          INSTANCE_MAP.put(clazz.getCanonicalName(), service);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
          e.printStackTrace();
        }
      });
    }
  }

  public static void main(String[] args) {
    loadAnnotationClass("cn.", Tinkle.class);
    loadAnnotationClass("cn.", TinkleClient.class);

    for (Object object : INSTANCE_MAP.values()) {
      //logger.info(clazz.getName());
      System.out.println("Found: " + object.getClass());
    }
  }
}
