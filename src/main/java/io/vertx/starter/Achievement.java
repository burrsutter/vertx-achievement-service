package io.vertx.starter;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class Achievement {

  public static Achievement from(JsonObject json) {
    return Json.decodeValue(json.encode(), Achievement.class);
  }

  /**
   * The achievement code
   */
  private String achievementType;
  /**
   * Has the achievement been reached?
   */
  private boolean achieved;

  public String getAchievementType() {
    return achievementType;
  }

  public void setAchievementType(String achievementType) {
    this.achievementType = achievementType;
  }

  public boolean isAchieved() {
    return achieved;
  }

  public synchronized void setAchieved(boolean achieved) {
    this.achieved = achieved;
  }
}
