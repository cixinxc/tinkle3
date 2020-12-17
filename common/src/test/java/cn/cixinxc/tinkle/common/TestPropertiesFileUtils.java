package cn.cixinxc.tinkle.common;

import cn.cixinxc.tinkle.common.utils.PropertiesFileUtils;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/17
 */
public class TestPropertiesFileUtils {
  public static void main(String[] args) {
    var properties = PropertiesFileUtils.parsePropertiesFile("test.properties");
    System.out.println(properties);
  }
}
