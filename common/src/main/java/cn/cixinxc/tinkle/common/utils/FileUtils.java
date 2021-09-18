package cn.cixinxc.tinkle.common.utils;

import java.net.URL;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/17
 */
public class FileUtils {
  public static String readFile(String fileName) {
    URL url = Thread.currentThread().getContextClassLoader().getResource(fileName);
    return url == null ? "" : url.getPath();
  }
}
