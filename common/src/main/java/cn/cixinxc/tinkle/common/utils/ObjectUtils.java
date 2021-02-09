package cn.cixinxc.tinkle.common.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Cui Xinxin
 * @createDate 2021/1/12
 */
public class ObjectUtils {

  public static List<String> getInterfaceNames(Object service) {
    if (service == null) {
      return Collections.emptyList();
    }
    return Arrays.stream(service.getClass().getInterfaces())
            .map(Class::getCanonicalName)
            .collect(Collectors.toList());
  }

  public static String getFirstInterfaceNames(Object service) {
    if (service == null) {
      return null;
    }
    return Arrays.stream(service.getClass().getInterfaces())
            .map(Class::getCanonicalName)
            .findFirst().orElse(null);
  }

}
