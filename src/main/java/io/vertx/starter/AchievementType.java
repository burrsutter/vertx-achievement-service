package io.vertx.starter;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class AchievementType {

  public static AchievementType from(JsonObject json) {
    return Json.decodeValue(json.encode(), AchievementType.class);
  }

  /**
   * The achievement type
   */
  private String achievementType;
  /**
   * The description of the achievement
   */
  private String description;

  public String getAchievementType() {
    return achievementType;
  }

  public void setAchievementType(String achievementType) {
    this.achievementType = achievementType;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
