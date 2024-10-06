package ae.cyberspeed.game.scratch;

import ae.cyberspeed.game.scratch.config.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameServiceTest {

    private GameService gameService;
    private Config mockConfig;
    private Probabilities mockProbabilities;
    private Map<String, Symbol> symbols;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockConfig = mock(Config.class);
        mockProbabilities = mock(Probabilities.class);

        // Mock symbols and their probabilities
        symbols = new HashMap<>();
        symbols.put("A", new Symbol(5.0, "standard", 0, null));
        symbols.put("B", new Symbol(3.0, "standard", 0, null));
        symbols.put("C", new Symbol(2.5, "standard", 0, null));
        symbols.put("D", new Symbol(5.0, "standard", 0, null));
        symbols.put("E", new Symbol(1.2, "standard", 0, null));
        symbols.put("F", new Symbol(1.5, "standard", 0, null));
        symbols.put("10x", new Symbol(10.0, "bonus", 0, "multiply_reward"));
        symbols.put("5x", new Symbol(5.0, "bonus", 0, "multiply_reward"));
        symbols.put("+1000", new Symbol(0.0, "bonus", 1000, "extra_bonus"));
        symbols.put("+500", new Symbol(0.0, "bonus", 500, "extra_bonus"));
        symbols.put("MISS", new Symbol(0.0, "bonus", 0, "miss"));

        when(mockConfig.getRows()).thenReturn(3);
        when(mockConfig.getColumns()).thenReturn(3);
        when(mockConfig.getSymbols()).thenReturn(symbols);
        when(mockConfig.getProbabilities()).thenReturn(mockProbabilities);

        // Mock standard symbol probabilities
        Map<String, Integer> standardProbabilities = new HashMap<>();
        standardProbabilities.put("A", 1);
        standardProbabilities.put("B", 2);
        standardProbabilities.put("C", 3);
        standardProbabilities.put("D", 4);
        standardProbabilities.put("E", 5);
        standardProbabilities.put("F", 6);

        ProbabilityGroup probabilityGroup = mock(ProbabilityGroup.class);
        when(probabilityGroup.getColumn()).thenReturn(0);
        when(probabilityGroup.getRow()).thenReturn(0);
        when(probabilityGroup.getSymbols()).thenReturn(standardProbabilities);

        when(mockProbabilities.getStandardSymbols()).thenReturn(Arrays.asList(probabilityGroup));

        // Mock bonus symbol probabilities
        Map<String, Map<String, Integer>> bonusProbabilities = new HashMap<>();

        Map<String, Integer> bonusSymbolProbabilities = new HashMap<>();

        bonusSymbolProbabilities.put("10x", 1);
        bonusSymbolProbabilities.put("5x", 2);
        bonusSymbolProbabilities.put("+1000", 3);
        bonusSymbolProbabilities.put("+500", 4);
        bonusSymbolProbabilities.put("MISS", 5);

        bonusProbabilities.put("symbols", bonusSymbolProbabilities);
        when(mockProbabilities.getBonusSymbols()).thenReturn(bonusProbabilities);

        gameService = new GameService(mockConfig);
    }

    @Test
    public void testPlayGameWithSameSymbol3Times() {
        String[][] matrix = {
                {"A", "A", "A"},
                {"B", "B", "C"},
                {"A", "B", "C"}
        };

        double betAmount = 100.0;

        // Mock Win Combinations
        Map<String, WinCombination> winCombinations = new HashMap<>();
        winCombinations.put("same_symbol_3_times", new WinCombination(1.0, "same_symbols", 3, null));
        when(mockConfig.getWinCombinations()).thenReturn(winCombinations);

        GameResult result = gameService.play(matrix, betAmount);
        assertEquals(true, result.getAppliedWinningCombinations().containsKey("B"));
        assertEquals("same_symbol_3_times", result.getAppliedWinningCombinations().get("B").stream().findFirst().get());
    }

    @Test
    public void testPlayGameWithSameSymbol4Times() {
        String[][] matrix = {
                {"A", "B", "A"},
                {"B", "C", "C"},
                {"A", "A", "C"}
        };

        double betAmount = 100.0;

        // Mock Win Combinations
        Map<String, WinCombination> winCombinations = new HashMap<>();
        winCombinations.put("same_symbol_4_times", new WinCombination(1.5, "same_symbols", 4, null));
        when(mockConfig.getWinCombinations()).thenReturn(winCombinations);

        GameResult result = gameService.play(matrix, betAmount);
        assertEquals(true, result.getAppliedWinningCombinations().containsKey("A"));
        assertEquals("same_symbol_4_times", result.getAppliedWinningCombinations().get("A").stream().findFirst().get());
    }

    @Test
    public void testPlayGameWithSameSymbolsHorizontally() {
        String[][] matrix = {
                {"A", "A", "A"},
                {"B", "B", "C"},
                {"A", "B", "C"}
        };

        double betAmount = 100.0;

        // Mock Win Combinations
        Map<String, WinCombination> winCombinations = new HashMap<>();
        winCombinations.put("same_symbols_horizontally", new WinCombination(2.0, "linear_symbols", 3, Arrays.asList(
                Arrays.asList("0:0", "0:1", "0:2")
        )));
        when(mockConfig.getWinCombinations()).thenReturn(winCombinations);

        GameResult result = gameService.play(matrix, betAmount);
        assertEquals(true, result.getAppliedWinningCombinations().containsKey("A"));
        assertEquals("same_symbols_horizontally", result.getAppliedWinningCombinations().get("A").stream().findFirst().get());
    }

    @Test
    public void testPlayGameWithSameSymbolsVertically() {
        String[][] matrix = {
                {"A", "B", "C"},
                {"A", "B", "C"},
                {"A", "B", "C"}
        };

        double betAmount = 100.0;

        // Mock Win Combinations
        Map<String, WinCombination> winCombinations = new HashMap<>();
        winCombinations.put("same_symbols_vertically", new WinCombination(2.0, "linear_symbols", 3, Arrays.asList(
                Arrays.asList("0:0", "1:0", "2:0")
        )));
        when(mockConfig.getWinCombinations()).thenReturn(winCombinations);

        GameResult result = gameService.play(matrix, betAmount);
        assertEquals(true, result.getAppliedWinningCombinations().containsKey("A"));
        assertEquals("same_symbols_vertically", result.getAppliedWinningCombinations().get("A").stream().findFirst().get());
    }

    @Test
    public void testPlayGameWithSameSymbolsDiagonallyLeftToRight() {
        String[][] matrix = {
                {"A", "B", "C"},
                {"B", "A", "C"},
                {"C", "B", "A"}
        };

        double betAmount = 100.0;

        // Mock Win Combinations
        Map<String, WinCombination> winCombinations = new HashMap<>();
        winCombinations.put("same_symbols_diagonally_left_to_right", new WinCombination(5.0, "linear_symbols", 3, Arrays.asList(
                Arrays.asList("0:0", "1:1", "2:2")
        )));
        when(mockConfig.getWinCombinations()).thenReturn(winCombinations);

        GameResult result = gameService.play(matrix, betAmount);
        assertEquals(true, result.getAppliedWinningCombinations().containsKey("A"));
        assertEquals("same_symbols_diagonally_left_to_right", result.getAppliedWinningCombinations().get("A").stream().findFirst().get());
    }

    @Test
    public void testPlayGameWithSameSymbolsDiagonallyRightToLeft() {
        String[][] matrix = {
                {"C", "B", "A"},
                {"B", "A", "C"},
                {"A", "B", "C"}
        };

        double betAmount = 100.0;

        // Mock Win Combinations
        Map<String, WinCombination> winCombinations = new HashMap<>();
        winCombinations.put("same_symbols_diagonally_right_to_left", new WinCombination(5.0, "linear_symbols", 3, Arrays.asList(
                Arrays.asList("0:2", "1:1", "2:0")
        )));
        when(mockConfig.getWinCombinations()).thenReturn(winCombinations);

        GameResult result = gameService.play(matrix, betAmount);
        assertEquals(true, result.getAppliedWinningCombinations().containsKey("A"));
        assertEquals("same_symbols_diagonally_right_to_left", result.getAppliedWinningCombinations().get("A").stream().findFirst().get());
    }

    @Test
    public void testPlayGameWithSameSymbol5Times() {
        String[][] matrix = {
                {"A", "A", "A"},
                {"A", "B", "C"},
                {"A", "C", "C"}
        };

        double betAmount = 100.0;

        // Mock Win Combinations
        Map<String, WinCombination> winCombinations = new HashMap<>();
        winCombinations.put("same_symbol_5_times", new WinCombination(2.0, "same_symbols", 5, null));
        when(mockConfig.getWinCombinations()).thenReturn(winCombinations);

        GameResult result = gameService.play(matrix, betAmount);
        assertEquals(true, result.getAppliedWinningCombinations().containsKey("A"));
        assertEquals("same_symbol_5_times", result.getAppliedWinningCombinations().get("A").stream().findFirst().get());
    }

    @Test
    public void testPlayGameWithSameSymbol6Times() {
        String[][] matrix = {
                {"A", "A", "B"},
                {"A", "A", "C"},
                {"A", "A", "C"}
        };

        double betAmount = 100.0;

        // Mock Win Combinations
        Map<String, WinCombination> winCombinations = new HashMap<>();
        winCombinations.put("same_symbol_6_times", new WinCombination(3.0, "same_symbols", 6, null));
        when(mockConfig.getWinCombinations()).thenReturn(winCombinations);

        GameResult result = gameService.play(matrix, betAmount);
        assertEquals(true, result.getAppliedWinningCombinations().containsKey("A"));
        assertEquals("same_symbol_6_times", result.getAppliedWinningCombinations().get("A").stream().findFirst().get());
    }

}

