package ae.cyberspeed.game.scratch.config;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Success.Otto
 * @since 0.0.1
 */
public class Symbol {
    @JsonProperty("reward_multiplier")
    private double rewardMultiplier; // When impact is 'multiply_reward'
    private String type; // Can be 'standard' or 'bonus'
    private String impact; // Bonus action like 'multiply_reward', 'extra_bonus' or 'miss'
    private int extra; // Extra bonus when impact is 'extra_bonus'

    public Symbol() {}

    public Symbol(double rewardMultiplier, String type, Integer extra, String impact) {
        this.rewardMultiplier = rewardMultiplier;
        this.type = type;
        this.extra = extra;
        this.impact = impact;
    }

    public double getRewardMultiplier() {
        return rewardMultiplier;
    }

    public void setRewardMultiplier(double rewardMultiplier) {
        this.rewardMultiplier = rewardMultiplier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImpact() {
        return impact;
    }

    public void setImpact(String impact) {
        this.impact = impact;
    }

    public int getExtra() {
        return extra;
    }

    public void setExtra(int extra) {
        this.extra = extra;
    }

}
