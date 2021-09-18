package cn.cixinxc.tinkle.common.utils;

import cn.cixinxc.tinkle.common.constant.CommonConstants;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Zookeeper Operation Utils
 *
 * @author Cui Xinxin
 * @createDate 2020/12/19
 */
public class CuratorUtils {

  public static final String ZK_REGISTER_ROOT_PATH = CommonConstants.ZK_REGISTER_ROOT_PATH;
  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  // default setting
  private static final int SLEEP_MILL_SECONDS = 1000;
  private static final int RETRY_TIMES = 3;
  private static final String DEFAULT_ZOOKEEPER_ADDRESS = CommonConstants.DEFAULT_ZOOKEEPER_ADDRESS;

  // local cache
  private static final Map<String, List<String>> SERVICE_ADDRESS_MAP = new ConcurrentHashMap<>();
  private static final Set<String> REGISTERED_PATH_SET = ConcurrentHashMap.newKeySet();

  // single instance
  private static CuratorFramework CURATOR_INSTANCE = getInstance();

  private CuratorUtils() {
  }

  /**
   * get instance
   */
  public synchronized static CuratorFramework getInstance() {
    if (CURATOR_INSTANCE != null && CURATOR_INSTANCE.getState() == CuratorFrameworkState.STARTED) {
      return CURATOR_INSTANCE;
    }
    ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(SLEEP_MILL_SECONDS, RETRY_TIMES);
    CURATOR_INSTANCE = CuratorFrameworkFactory.builder()
            .connectString(DEFAULT_ZOOKEEPER_ADDRESS)
            .retryPolicy(retryPolicy)
            .build();
    CURATOR_INSTANCE.start();
    return CURATOR_INSTANCE;
  }

  /**
   * create default(PERSISTENT) node
   *
   * @param framework CuratorFramework instance
   * @param path      node path
   */
  public static boolean createNode(CuratorFramework framework, String path) {
    return createNode(framework, path, CreateMode.PERSISTENT.name());
  }

  /**
   * create node
   *
   * @param framework  CuratorFramework instance
   * @param path       node path
   * @param createMode see org.apache.zookeeper.CreateMode's name
   */
  public static boolean createNode(CuratorFramework framework, String path, String createMode) {
    try {
      if (REGISTERED_PATH_SET.contains(path) || framework.checkExists().forPath(path) != null) {
        return true;
      }
      framework.create().creatingParentsIfNeeded().withMode(CreateMode.valueOf(createMode)).forPath(path);
      return true;
    } catch (Exception e) {
      logger.error("create persistent node error.path:{}.", path, e);
    }
    return false;
  }

  /**
   * list child nodes from parent node
   *
   * @param framework CuratorFramework instance
   * @param nodeName  parent node name
   */
  public static List<String> listChildNodes(CuratorFramework framework, String nodeName) {
    if (SERVICE_ADDRESS_MAP.containsKey(nodeName)) {
      return SERVICE_ADDRESS_MAP.get(nodeName);
    }
    String servicePath = ZK_REGISTER_ROOT_PATH + "/" + nodeName;
    try {
      List<String> nodes = framework.getChildren().forPath(servicePath);
      registerWatcher(framework, nodeName);
      return nodes;
    } catch (Exception e) {
      logger.error("list child nodes error.nodeName:{}.", nodeName, e);
    }
    return null;
  }

  /**
   * delete path from zookeeper
   *
   * @param framework CuratorFramework instance
   * @param address   node's address
   */
  public static void clear(CuratorFramework framework, InetSocketAddress address) {
    REGISTERED_PATH_SET.parallelStream()
            .filter(path -> path.endsWith(address.toString()))
            .forEach(path -> deletePath(framework, path));
  }

  private static void deletePath(CuratorFramework framework, String path) {
    try {
      framework.delete().forPath(path);
    } catch (Exception e) {
      logger.error("delete path error.path:{}.", path, e);
    }
  }

  /**
   * register watcher
   *
   * @param framework CuratorFramework instance
   * @param nodeName  node's name
   */
  private static void registerWatcher(CuratorFramework framework, String nodeName) {
    String servicePath = ZK_REGISTER_ROOT_PATH + "/" + nodeName;
    CuratorCache curatorCache = CuratorCache.build(framework, servicePath, CuratorCache.Options.SINGLE_NODE_CACHE);
    CuratorCacheListener listener = CuratorCacheListener.builder()
            .forTreeCache(framework, (c, e) -> dealTreeCache(e))
            .build();
    curatorCache.listenable().addListener(listener);
    curatorCache.start();
  }

  private static void dealTreeCache(TreeCacheEvent event) {
    switch (event.getType()) {
      case INITIALIZED:
        whenInit(event);
        break;
      case NODE_ADDED:
        whenAdded(event);
        break;
      case NODE_UPDATED:
        whenUpdated(event);
        break;
      case NODE_REMOVED:
        whenRemoved(event);
        break;
      default:
        break;
    }
  }

  private static void whenInit(TreeCacheEvent event) {
    logger.info("TreeCacheListener initialized. type:{}.", event.getType());
  }

  private static void whenAdded(TreeCacheEvent event) {
    REGISTERED_PATH_SET.add(event.getData().getPath().replaceFirst(ZK_REGISTER_ROOT_PATH + "/", ""));
    logger.info("node created:{} with stat:{}.", event.toString(), event.getData().getStat());
  }

  private static void whenUpdated(TreeCacheEvent event) {
    ChildData old = event.getOldData();
    ChildData now = event.getData();
    SERVICE_ADDRESS_MAP.put(
            now.getPath().replaceFirst(ZK_REGISTER_ROOT_PATH + "/", ""),
            new Gson().fromJson(new String(now.getData()), new TypeToken<List<String>>() {
            }.getType()));
    logger.info("node update form old:{}-{} to new:{}-{}.", old.getPath(), old.getData(), now.getPath(), now.getData());
  }

  private static void whenRemoved(TreeCacheEvent event) {
    ChildData data = event.getData();
    SERVICE_ADDRESS_MAP.remove(data.getPath().replaceFirst(ZK_REGISTER_ROOT_PATH + "/", ""));
    REGISTERED_PATH_SET.remove(event.getData().getPath().replaceFirst(ZK_REGISTER_ROOT_PATH + "/", ""));
    logger.info("node removed:{}-{}.", event.getData().getPath(), event.getData().getData());
  }

}
