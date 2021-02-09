package cn.cixinxc.tinkle.start.test;

import cn.cixinxc.tinkle.common.annotation.TinkleClient;
import org.springframework.stereotype.Component;

/**
 * @author Cui Xinxin
 */
@Component
public class HelloController {

  @TinkleClient(version = "version1", group = "test1")
  private HelloService helloService;

  public void test() {
    try {
      String hello = helloService.hello("111sss sasasas");
      System.out.println(hello);
      Thread.sleep(1);
    } catch (Exception E) {
      E.printStackTrace();
    }


    for (int i = 0; i < 10; i++) {
      System.out.println(helloService.hello("sasasas  asdasddsa      " + i));
    }
  }
}
