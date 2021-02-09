package cn.cixinxc.tinkle.start.test;


import cn.cixinxc.tinkle.common.annotation.Tinkle;
import cn.cixinxc.tinkle.common.annotation.TinkleClient;

@Tinkle(group = "test1", version = "version1")
public class HelloServiceImpl2 implements HelloService2 {

  static {
    System.out.println("cn.cixinxc.carbon.example.HelloServiceImpl created");
  }

  @Override
  public String hello(String hello) {
    String result = "Hello2 description is " + hello;
    return result;
  }

  @TinkleClient(version = "version1", group = "test1")
  private HelloService helloService;
}
