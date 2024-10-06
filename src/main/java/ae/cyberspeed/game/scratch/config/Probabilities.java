package ae.cyberspeed.game.scratch.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * @author Success.Otto
 * @since 0.0.1
 */
public class Probabilities {
    @JsonProperty("standard_symbols")
    private List<ProbabilityGroup> standardSymbols; // List of standard symbol probability objects
    @JsonProperty("bonus_symbols")
    private Map<String, Map<String, Integer>> bonusSymbols; // Bonus Symbol probabilities

    public List<ProbabilityGroup> getStandardSymbols() {
        return standardSymbols;
    }

    public void setStandardSymbols(List<ProbabilityGroup> standardSymbols) {
        this.standardSymbols = standardSymbols;
    }

    public Map<String, Map<String, Integer>> getBonusSymbols() {
        return bonusSymbols;
    }

    public void setBonusSymbols(Map<String, Map<String, Integer>> bonusSymbols) {
        this.bonusSymbols = bonusSymbols;
    }
}
