package cn.cixinxc.tinkle.common.serialize;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/24
 */
public interface Serializer {

  byte[] serialize(Object obj);

  <T> T deserialize(byte[] bytes, Class<T> clazz);
}
