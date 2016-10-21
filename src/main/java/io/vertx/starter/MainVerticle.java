package io.vertx.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;

public class MainVerticle extends AbstractVerticle {

  AchievementBackend service = new AchievementBackend();

  @Override
  public void start() {

    Router router = Router.router(vertx);
    // Cors filter
    router.route().handler(CorsHandler.create("*"));

    router.delete("/api/reset").handler(rc -> {
      service.reset();
      rc.response().end();
    });

    router.get("/api/achievement").handler(rc -> {
      System.out.println("Get achievements " + service.achievementTypes());
      rc.response().putHeader("content-type", "application/json")
          .end(Json.encode(service.achievementTypes()));
    });

    router.get("/api/achievement/:uuid").handler(rc -> {
      System.out.println("Get achievement for " + rc.pathParam("uuid"));
      String encode = Json.encode(service.achievements(rc.pathParam("uuid")));
      System.out.println(encode);
      rc.response().putHeader("content-type", "application/json")
          .end(encode);
    });

    router.put("/api/achievement/update/:uuid").handler(rc -> {
      String uuid = rc.pathParam("uuid");
      System.out.println("Put for " + uuid + " / " + rc.getBody());
      rc.response().putHeader("content-type", "application/json")
          .end();
    });

    router.get("/api/health").handler(rc -> {
      rc.response().end("Service is running");
    });

    vertx.createHttpServer()
        .requestHandler(router::accept)
        .listen(8080);
  }


}
