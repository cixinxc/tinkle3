package cn.cixinxc.tinkle.common;

import cn.cixinxc.tinkle.common.utils.FilePropertiesUtils;

import java.util.Properties;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/17
 */
public class TestPropertiesFileUtils {
  public static void main(String[] args) {
    Properties properties = FilePropertiesUtils.parsePropertiesFile("test.properties");
    System.out.println(properties);
  }
}
