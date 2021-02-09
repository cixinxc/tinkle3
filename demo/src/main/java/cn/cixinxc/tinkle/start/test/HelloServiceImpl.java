package cn.cixinxc.tinkle.start.test;


import cn.cixinxc.tinkle.common.annotation.Tinkle;

@Tinkle(group = "test1", version = "version1")
public class HelloServiceImpl implements HelloService {

  static {
    System.out.println("cn.cixinxc.carbon.example.HelloServiceImpl created");
  }

  @Override
  public String hello(String hello) {
    String result = "Hello description is " + hello;
    return result;
  }

  // @TinkleClient(version = "version1", group = "test1")
  // private HelloService2 helloService2;
}
