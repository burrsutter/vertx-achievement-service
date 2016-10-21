package io.vertx.starter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class AchievementBackend {

  private static final List<AchievementType> ACHIEVEMENT_TYPES;

  private static final Map<String, Map<String, Achievement>> USER_ACHIEVEMENTS = new HashMap<>();

  /**
   * Return the list of achievement types
   * @return the list of achievement types
   */
  public List<AchievementType> achievementTypes() {
    return ACHIEVEMENT_TYPES;
  }

  /**
   * Reset all the achievements
   */
  public void reset() {
    USER_ACHIEVEMENTS.clear();
  }

  /**
   * Get the achievements for a specific user
   * @param uuid The user's uuis
   * @return The current list of achievements
   */
  public List<Achievement> achievements(final String uuid) {
    Map<String, Achievement> current = USER_ACHIEVEMENTS.get(uuid);
    if (current == null) {
      current = addNewAchievements(uuid);
    }
    return new ArrayList<>(current.values());
  }

  /**
   * Update an achievement
   * @param uuid The user uuid
   * @param achievementType The achievement type
   * @return The current value of the achievement or null if it doesn't exist
   */
  public Achievement updateAchievement(String uuid, String achievementType) {
    System.out.println("Update achievements: " + uuid + " / " + achievementType);
    Map<String, Achievement> current = USER_ACHIEVEMENTS.get(uuid);
    if (current == null) {
      current = addNewAchievements(uuid);
    }
    final Achievement achievement = current.get(achievementType);
    if (achievement != null) {
      achievement.setAchieved(true);
    }
    return achievement;
  }

  private Map<String, Achievement> addNewAchievements(String uuid) {
    ConcurrentHashMap<String, Achievement> newAchievements = new ConcurrentHashMap<>();
    for(AchievementType type: ACHIEVEMENT_TYPES) {
      final String achievementType = type.getAchievementType();
      final Achievement achievement = new Achievement();
      achievement.setAchievementType(achievementType);
      newAchievements.put(achievementType, achievement);
    }
    Map<String, Achievement> previous = USER_ACHIEVEMENTS.putIfAbsent(uuid, newAchievements);
    return (previous == null ? newAchievements : previous);
  }

  private static void addAchievementType(final List<AchievementType> achievementTypes, final String achievementType, final String description) {
    final AchievementType type = new AchievementType();
    type.setAchievementType(achievementType);
    type.setDescription(description);

    achievementTypes.add(type);
  }

  static {
    final List<AchievementType> achievementTypes = new ArrayList<>();

    addAchievementType(achievementTypes, "TEN_CONSEQ", "10 consecutive points");
    addAchievementType(achievementTypes, "FIFTY_CONSEQ", "50 consecutive points");
    addAchievementType(achievementTypes, "100_POINTS", "100 points");
    addAchievementType(achievementTypes, "500_POINTS", "500 points");
    addAchievementType(achievementTypes, "TOP_SCORER", "Top scorer");

    ACHIEVEMENT_TYPES = achievementTypes;
  }
}
