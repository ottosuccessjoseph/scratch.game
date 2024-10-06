package ae.cyberspeed.game.scratch.config;

import java.util.Map;

/**
 * @author Success.Otto
 * @since 0.0.1
 */
public class ProbabilityGroup {
    private int column; // Column the probability should be applied
    private int row; // Row the probability should be applied
    private Map<String, Integer> symbols; // Map of symbols and probability that should be applied based on the column x row

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public Map<String, Integer> getSymbols() {
        return symbols;
    }

    public void setSymbols(Map<String, Integer> symbols) {
        this.symbols = symbols;
    }
}
