package cn.cixinxc.tinkle.compress;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/24
 */
public interface Compress {

  byte[] compress(byte[] bytes);

  byte[] decompress(byte[] bytes);
}
