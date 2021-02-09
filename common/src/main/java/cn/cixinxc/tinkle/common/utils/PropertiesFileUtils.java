package cn.cixinxc.tinkle.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/17
 */
public class PropertiesFileUtils {

  public static final Map<String, Properties> propertiesMap = new ConcurrentHashMap<>(128);
  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private static final String FILE_NAME = "application.properties";

  public static Properties parsePropertiesFile(String fileName) {
    return propertiesMap.getOrDefault(fileName, parseFile(fileName));
  }

  private static Properties parseFile(String fileName) {
    var configFilePath = FileUtils.readFile(fileName);
    if (StringUtils.isBlank(configFilePath)) {
      return null;
    }
    try (var inputStreamReader = new InputStreamReader(new FileInputStream(configFilePath), StandardCharsets.UTF_8)) {
      var properties = new Properties();
      properties.load(inputStreamReader);
      propertiesMap.put(fileName, properties);
      return properties;
    } catch (IOException e) {
      logger.error("read properties from [{}] exception.", fileName, e);
      return null;
    }
  }

  public static String getValue(String key) {
    return getValue(key, "");
  }

  public static String getValue(String key, String defaultValue) {
    var properties = parsePropertiesFile(FILE_NAME);
    return properties == null ? defaultValue : properties.getProperty(key);
  }

}
