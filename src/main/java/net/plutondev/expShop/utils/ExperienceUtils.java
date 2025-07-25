package net.plutondev.expShop.utils;

import org.bukkit.entity.Player;
import net.plutondev.expShop.enums.ExpType;

public class ExperienceUtils {

    public void deductExperience(Player player, int expCost, ExpType expType) {
        if (expType == ExpType.LEVEL) {
            // Calculate current total experience (before deduction)
            int currentLevel = player.getLevel();
            float currentFraction = player.getExp();
            getTotalExperienceForLevel(currentLevel);
            getExpToNextLevel(currentLevel);

            // Calculate new total experience after deducting levels
            int targetLevel = Math.max(0, currentLevel - expCost);
            int targetTotalExp = getTotalExperienceForLevel(targetLevel);

            if (targetLevel > 0 && currentLevel > 0 && currentFraction > 0) {
                targetTotalExp += Math.round(getExpToNextLevel(targetLevel) * currentFraction);
            }

            // Reset and apply the new experience total
            player.setTotalExperience(0);
            player.setLevel(0);
            player.setExp(0);
            player.giveExp(targetTotalExp);
        } else if (expType == ExpType.POINTS) {
            int currentTotalExp = player.getTotalExperience();
            int targetTotalExp = Math.max(0, currentTotalExp - expCost);

            player.setTotalExperience(0);
            player.setLevel(0);
            player.setExp(0);
            player.giveExp(targetTotalExp);
        }
    }

    public boolean hasEnoughExp(Player player, int expCost, ExpType expType) {
        if (expType == ExpType.LEVEL) {
            return player.getLevel() >= expCost;
        } else if (expType == ExpType.POINTS) {
            return player.getTotalExperience() >= expCost;
        }
        return true;
    }

    public int getTotalExperienceForLevel(int level) {
        if (level <= 0) return 0;

        int experience = 0;
        if (level <= 16) {
            experience = (level * level + 6 * level);
        } else if (level <= 31) {
            experience = (int) (2.5 * level * level - 40.5 * level + 360);
        } else {
            experience = (int) (4.5 * level * level - 162.5 * level + 2220);
        }
        return experience;
    }

    public int getExpToNextLevel(int level) {
        if (level <= 15) return 2 * level + 7;
        if (level <= 30) return 5 * level - 38;
        return 9 * level - 158;
    }

    public void updateLevelAndExp(Player player, int totalExp) {
        int level = 0;
        int expToNextLevel;

        while (true) {
            expToNextLevel = getExpToNextLevel(level);
            if (totalExp < expToNextLevel) break;
            totalExp -= expToNextLevel;
            level++;
        }

        player.setLevel(level);
        player.setExp((float) totalExp / expToNextLevel);
    }
}