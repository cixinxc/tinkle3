package cn.cixinxc.tinkle.common.utils;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/17
 */
public class FileUtils {
  public static String readFile(String fileName) {
    var url = Thread.currentThread().getContextClassLoader().getResource(fileName);
    return url == null ? "" : url.getPath();
  }
}
