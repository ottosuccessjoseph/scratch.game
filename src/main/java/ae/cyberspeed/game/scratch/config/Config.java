package ae.cyberspeed.game.scratch.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * @author Success.Otto
 * @since 0.0.1
 */
public class Config {

    private int columns = 3; // Defines the number of columns in the matrix
    private int rows = 3; // Defines the number of rows in the matrix
    private Map<String, Symbol> symbols; // Defines standard and bonus symbols
    private Probabilities probabilities; // Defines probabilities of symbols
    @JsonProperty("win_combinations")
    private Map<String, WinCombination> winCombinations; // Defines the win pattern

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public Map<String, Symbol> getSymbols() {
        return symbols;
    }

    public void setSymbols(Map<String, Symbol> symbols) {
        this.symbols = symbols;
    }

    public Probabilities getProbabilities() {
        return probabilities;
    }

    public void setProbabilities(Probabilities probabilities) {
        this.probabilities = probabilities;
    }

    public Map<String, WinCombination> getWinCombinations() {
        return winCombinations;
    }

    public void setWinCombinations(Map<String, WinCombination> winCombinations) {
        this.winCombinations = winCombinations;
    }
}
