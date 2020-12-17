package cn.cixinxc.tinkle.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/17
 */
public class PropertiesFileUtils {

  private static final Logger logger = LoggerFactory.getLogger(PropertiesFileUtils.class);

  public static Properties parsePropertiesFile(String fileName) {
    var configFilePath = FileUtils.readFile(fileName);
    if (StringUtils.isBlank(configFilePath)) {
      return null;
    }
    try (var inputStreamReader = new InputStreamReader(new FileInputStream(configFilePath), StandardCharsets.UTF_8)) {
      var properties = new Properties();
      properties.load(inputStreamReader);
      return properties;
    } catch (IOException e) {
      logger.error("read properties from [{}] exception.", fileName, e);
      return null;
    }
  }

}
