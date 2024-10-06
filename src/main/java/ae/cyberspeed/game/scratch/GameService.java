package ae.cyberspeed.game.scratch;

import ae.cyberspeed.game.scratch.config.Config;
import ae.cyberspeed.game.scratch.config.ProbabilityGroup;
import ae.cyberspeed.game.scratch.config.Symbol;
import ae.cyberspeed.game.scratch.config.WinCombination;

import java.util.*;

/**
 * @author Success.Otto
 * @since 0.0.1
 */
public class GameService {

    private final Config config;
    private final Random random = new Random();
    private Map<String, Set<String>> appliedWinningCombinations = new HashMap<>();
    private String appliedBonusSymbol = null;

    public GameService(Config config) {
        this.config = config;
    }

    public GameResult play(String[][] matrix, double betAmount) {

        this.appliedWinningCombinations = new HashMap<>();
        double totalReward = calculateReward(matrix, betAmount);
        if (!this.appliedWinningCombinations.isEmpty()) {
            getRandomBonusSymbol(config.getProbabilities().getBonusSymbols());
            totalReward = applyBonus(totalReward);
        }
        int randomRow = random.nextInt(config.getRows());
        int randomCol = random.nextInt(config.getColumns());
        matrix[randomRow][randomCol] = appliedBonusSymbol;
        return new GameResult(matrix, totalReward, appliedWinningCombinations, appliedBonusSymbol);
    }

    /**
     * Generate Matrix based on the probabilities in the config
     * @return
     */
    public String[][] generateMatrix() {
        String[][] matrix = new String[config.getRows()][config.getColumns()];
        for (ProbabilityGroup probabilityGroup: config.getProbabilities().getStandardSymbols()) {
            int column = probabilityGroup.getColumn();
            int row = probabilityGroup.getRow();
            matrix[row][column] = getRandomSymbol(probabilityGroup.getSymbols());
        }
        return matrix;
    }

    private String getRandomSymbol(Map<String, Integer> symbolProbabilities) {
        int totalWeight = symbolProbabilities.values().stream().mapToInt(Integer::intValue).sum();
        int randomValue = random.nextInt(totalWeight);
        int cumulativeWeight = 0;

        for (Map.Entry<String, Integer> entry: symbolProbabilities.entrySet()) {
            cumulativeWeight += entry.getValue();
            if (randomValue < cumulativeWeight) {
                return entry.getKey();
            }
        }
        return null;
    }

    private String getRandomBonusSymbol(Map<String, Map<String, Integer>> bonusSymbolProbabilities) {
        int totalWeight = bonusSymbolProbabilities.get("symbols").values().stream().mapToInt(Integer::intValue).sum();
        int randomValue = random.nextInt(totalWeight);
        int cumulativeWeight = 0;

        for (Map.Entry<String, Integer> entry: bonusSymbolProbabilities.get("symbols").entrySet()) {
            cumulativeWeight += entry.getValue();
            if (randomValue < cumulativeWeight) {
                appliedBonusSymbol = entry.getKey();
                return entry.getKey();
            }
        }
        appliedBonusSymbol = null;
        return "MISS";
    }

    public double calculateReward(String[][] matrix, double betAmount) {
        Map<String, Integer> symbolCounts = countSymbols(matrix);
        double totalReward = 0;


        for (Map.Entry<String, Integer> symbolCount: symbolCounts.entrySet()) {
            Set<String> winningCombinations = new HashSet<>();
            String symbol = symbolCount.getKey();
            int count = symbolCount.getValue();
            boolean hasWinningCombination = false;
            double sameSymbolReward = 1;
            double verticalSymbolReward = 1;

            for (Map.Entry<String, WinCombination> winCombinationEntry: config.getWinCombinations().entrySet()) {
                String key = winCombinationEntry.getKey();
                WinCombination winCombination = winCombinationEntry.getValue();

                if ("same_symbols".equals(winCombination.getWhen()) && winCombination.getCount() == count) {
                    hasWinningCombination = true;
                    sameSymbolReward *= winCombination.getRewardMultiplier();
                    winningCombinations.add(key);
                }


                if ("linear_symbols".equals(winCombination.getWhen()) && winCombination.getCoveredAreas() != null
                        && !winCombination.getCoveredAreas().isEmpty()) {
                    boolean hasVerticalSymbolWinningCombination = false;
                    for (List<String> coveredArea : winCombination.getCoveredAreas()) {
                        if (isCoveredAreaMatching(matrix, coveredArea)) {
                            String[] coordinates = coveredArea.get(0).split(":");
                            int coveredRow = Integer.parseInt(coordinates[0]);
                            int coveredColumn = Integer.parseInt(coordinates[1]);
                            String winningSymbol = matrix[coveredRow][coveredColumn];
                            if (symbol.equals(winningSymbol)) {
                                hasWinningCombination = true;
                                hasVerticalSymbolWinningCombination = true;
                            }
                        }
                    }
                    if (hasVerticalSymbolWinningCombination) {
                        verticalSymbolReward *= winCombination.getRewardMultiplier();
                        winningCombinations.add(key);
                    }
                }
            }
            if (hasWinningCombination) {
                double symbolReward = config.getSymbols().get(symbol).getRewardMultiplier();
                totalReward += betAmount * symbolReward * sameSymbolReward * verticalSymbolReward;
                appliedWinningCombinations.put(symbol, winningCombinations);
            }
        }
        return totalReward;
    }

    private Map<String, Integer> countSymbols(String[][] matrix) {
        Map<String, Integer> symbolCounts = new HashMap<>();
        for (String[] row: matrix) {
            for (String symbol: row) {
                symbolCounts.put(symbol, symbolCounts.getOrDefault(symbol, 0) + 1);
            }
        }
        return symbolCounts;
    }

    private boolean isCoveredAreaMatching(String[][] matrix, List<String> coveredArea) {
        String firstSymbol = null;
        for (String position: coveredArea) {
            String[] coordinates = position.split(":");
            int row = Integer.parseInt(coordinates[0]);
            int column = Integer.parseInt(coordinates[1]);

            if (firstSymbol == null) {
                firstSymbol = matrix[row][column];
            } else if (!firstSymbol.equals(matrix[row][column])) {
                return false;
            }
        }
        return true;
    }

    public double applyBonus(double reward) {
        Symbol bonusSymbol = config.getSymbols().get(appliedBonusSymbol);
        if (bonusSymbol != null && "bonus".equals(bonusSymbol.getType())) {
            switch (bonusSymbol.getImpact()) {
                case "multiply_reward":
                    reward *= bonusSymbol.getRewardMultiplier();
                    break;
                case "extra_bonus":
                    reward += bonusSymbol.getExtra();
                    break;
                case "miss":
                    reward = 0;
                    break;
            }
        }
        return reward;
    }
}
