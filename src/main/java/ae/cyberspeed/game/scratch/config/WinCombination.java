package ae.cyberspeed.game.scratch.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author Success.Otto
 * @since 0.0.1
 */
public class WinCombination {
    @JsonProperty("reward_multiplier")
    private double rewardMultiplier; // Multiplier for the winning combination
    private String when;  // Win condition
    private int count; // Number of matching symbols needed
    private String group; // Grouping of win combinations
    @JsonProperty("covered_areas")
    private List<List<String>> coveredAreas; // Areas to check

    public WinCombination() {}

    public WinCombination(double rewardMultiplier, String when, int count, List<List<String>> coveredAreas) {
        this.rewardMultiplier = rewardMultiplier;
        this.when = when;
        this.count = count;
        this.coveredAreas = coveredAreas;
    }

    public double getRewardMultiplier() {
        return rewardMultiplier;
    }

    public void setRewardMultiplier(double rewardMultiplier) {
        this.rewardMultiplier = rewardMultiplier;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<List<String>> getCoveredAreas() {
        return coveredAreas;
    }

    public void setCoveredAreas(List<List<String>> coveredAreas) {
        this.coveredAreas = coveredAreas;
    }
}
