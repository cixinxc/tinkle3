package cn.cixinxc.tinkle.start;


import cn.cixinxc.tinkle.netty.server.NettyServer;
import cn.cixinxc.tinkle.spring.support.RpcScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.invoke.MethodHandles;

@RpcScan(basePackage = {"cn.cixinxc.tinkle"})
public class NettyServerMain {

  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public static void main(String[] args) {
    // NettyServer server = new NettyServer();
    // server.start();
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyServerMain.class);
    NettyServer nettyRpcServer = new NettyServer();

    nettyRpcServer.start();
  }
}
