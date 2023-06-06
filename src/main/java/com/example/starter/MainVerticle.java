package com.example.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.openapi.contract.OpenAPIContract;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    OpenAPIContract.from(vertx, "api.yml")
      .map(openAPIContract -> {
        System.out.println(openAPIContract.getRawContract());
        return openAPIContract;
      })
      .compose(openAPIContract ->
        vertx.createHttpServer()
          .requestHandler(req -> {
            req.response()
              .putHeader("content-type", "text/plain")
              .end("Hello from Vert.x!");
          })
          .listen(8888)
          .onSuccess(httpServer -> System.out.println("HTTP server started on port 8888"))
          .<Void>mapEmpty()
      )
      .onComplete(startPromise);

  }
}
