package cn.cixinxc.tinkle.start;

import cn.cixinxc.tinkle.spring.support.RpcScan;
import cn.cixinxc.tinkle.start.test.HelloController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Cui Xinxin
 * @createTime 2020年05月10日 07:25:00
 */
@RpcScan(basePackage = {"cn.cixinxc.tinkle"})
public class NettyClientMain {
  public static void main(String[] args) {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyClientMain.class);
    HelloController helloController = (HelloController) applicationContext.getBean("helloController");
    helloController.test();
  }
}
