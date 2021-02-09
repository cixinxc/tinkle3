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
    // HelloController helloController = new HelloController();
    // AnnotationSupport.loadAnnotationClass("cn.", Tinkle.class);
    // System.out.println(AnnotationSupport.INSTANCE_MAP);
    // Request request = new Request();
    // InvokeProperties properties = new InvokeProperties();
    // properties.setMethodName("hello");
    // properties.setParameters(new Object[]{"123"});
    // properties.setParamTypes(new Class[]{String.class});
    // request.setRequestId("123");
    // request.setInvokeProperties(properties);
    // //InvokeUtils.invokeTargetMethod(AnnotationSupport.INSTANCE_MAP.get("cn.cixinxc.tinkle.start.test.HelloServiceImpl"), request);
    // ServiceProperties serviceProperties = new ServiceProperties();
    // serviceProperties.setGroupName("test1");
    // serviceProperties.setVersion("version1");
    // serviceProperties.setServiceName("cn.cixinxc.tinkle.start.test.HelloServiceImpl");
    // request.setServiceProperties(serviceProperties);
    //
    // NettyClient<Object> client = new NettyClient<Object>();
    // CompletableFuture<Response<Object>> completableFuture = client.sendRpcRequest(request);
    //
    // Response response = null;
    // try {
    //   response = completableFuture.get();
    //   System.out.println(response);
    // } catch (InterruptedException e) {
    //   e.printStackTrace();
    // } catch (ExecutionException e) {
    //   e.printStackTrace();
    // }
    //
    // System.out.println(response);
    //helloController.test();
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyClientMain.class);
    HelloController helloController = (HelloController) applicationContext.getBean("helloController");
    helloController.test();
  }
}
