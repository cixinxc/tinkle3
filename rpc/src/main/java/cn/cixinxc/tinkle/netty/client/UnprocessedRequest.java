package cn.cixinxc.tinkle.netty.client;


import cn.cixinxc.tinkle.common.model.Response;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;


public class UnprocessedRequest {
  private static final Map<String, CompletableFuture<Response>> UNPROCESSED_RESPONSE_FUTURES = new ConcurrentHashMap<>();

  public void put(String requestId, CompletableFuture<Response> future) {
    UNPROCESSED_RESPONSE_FUTURES.put(requestId, future);
  }

  public void complete(Response rpcResponse) {
    CompletableFuture<Response> future = UNPROCESSED_RESPONSE_FUTURES.remove(rpcResponse.getRequestId());
    if (null != future) {
      future.complete(rpcResponse);
    } else {
      throw new IllegalStateException();
    }
  }
}
