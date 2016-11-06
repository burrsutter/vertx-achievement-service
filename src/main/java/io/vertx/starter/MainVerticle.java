package io.vertx.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.BodyHandler;
import java.util.logging.Logger;
import java.util.logging.Level;

public class MainVerticle extends AbstractVerticle {
  private final static Logger LOGGER = Logger.getLogger(MainVerticle.class.getName());
  
  AchievementBackend service = new AchievementBackend();

  @Override
  public void start() {
    LOGGER.setLevel(Level.INFO);
    Router router = Router.router(vertx);
    // Cors filter
    router.route().handler(CorsHandler.create("*"));
    router.route().handler(BodyHandler.create());

    router.delete("/api/reset").handler(rc -> {
      LOGGER.info("reset");
      service.reset();
      rc.response().end(); 
      // this should respond with a 204, at least that is what is expected by GameVerticle.groovy
    });

    router.get("/api/achievement").handler(rc -> {
      LOGGER.info("Get achievements " + service.achievementTypes());
      rc.response().putHeader("content-type", "application/json")
          .end(Json.encode(service.achievementTypes()));
    });

    router.get("/api/achievement/:uuid").handler(rc -> {
      String uuid = rc.pathParam("uuid");
      LOGGER.info("Get achievements for: " + uuid);
      String encoded = Json.encode(service.achievements(rc.pathParam("uuid")));
      // System.out.println(encode);
      rc.response().putHeader("content-type", "application/json")
          .end(encoded);
    });
    
    // maybe switch to post
    router.put("/api/achievement/update/:uuid").handler(rc -> {
      String uuid = rc.pathParam("uuid");
      LOGGER.info("PUT for " + uuid + " / " + rc.getBody());
      String body = rc.getBodyAsString();
      if (body != null) {
        try {          
          JsonArray achievementArray = rc.getBodyAsJsonArray();
          int size = achievementArray.size();
          LOGGER.info("number of achievements sent: " + size);
          for (int i = 0; i < size; i++) {
            JsonObject achievement = achievementArray.getJsonObject(i);
            String achievementType = achievement.getString("type");
            LOGGER.info("achievementType: " + achievementType);
            service.updateAchievement(uuid,achievementType);
          } // for
        } catch (Exception e) {
          LOGGER.severe("Exception: " + e);          
        } // catch
      } // if (body != null)
      rc.response().putHeader("content-type", "application/json")
          .end();
    });

    router.get("/api/health").handler(rc -> {
      rc.response().end("Service is running");
    });

    vertx.createHttpServer()
        .requestHandler(router::accept)
        .listen(9090);
  }


}
