package ae.cyberspeed.game.scratch;

import ae.cyberspeed.game.scratch.config.Config;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author Success.Otto
 * @since 0.0.1
 */
public class Main {
    public static void main(String[] args) throws Exception {

        String configFilePath = "";
        double betAmount = 100;

        // Iterate through command-line arguments
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--config":  // If argument is --config, the next value should be the file path
                    if (i + 1 < args.length) {
                        configFilePath = args[++i];
                    } else {
                        System.err.println("Error: --config option requires a file path.");
                        System.exit(1);
                    }
                    break;
                case "--betting-amount":  // If argument is --betting-amount, the next value should be the amount
                    if (i + 1 < args.length) {
                        try {
                            betAmount = Double.parseDouble(args[++i]);
                        } catch (NumberFormatException e) {
                            System.err.println("Error: --betting-amount requires a valid number.");
                            System.exit(1);
                        }
                    } else {
                        System.err.println("Error: --betting-amount option requires a value.");
                        System.exit(1);
                    }
                    break;
                default:
                    System.err.println("Unknown option: " + args[i]);
                    System.exit(1);
            }
        }

        // Check if required arguments are provided
        if (configFilePath == null || betAmount == 0) {
            System.err.println("Usage: java -jar scratch.game-0.0.1.jar --config <config-file> --betting-amount <amount>");
            System.exit(1);
        }

        InputStream configFileStream = Main.class.getClassLoader().getResourceAsStream("config.json");
        File configFile;
        if (!configFilePath.isEmpty()) {
            configFile = new File(configFilePath);
            if (!configFile.exists() || !configFile.isFile()) {
                System.err.println("Config file not found or is not a valid file: " + configFilePath);
                System.exit(1);
            } else {
                configFileStream = new FileInputStream(configFile);
            }
        }
        if (configFileStream == null) {
            System.err.println("Config file not found in resources: " + configFilePath);
            System.exit(1);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Config config = objectMapper.readValue(configFileStream, Config.class);

        GameService gameService = new GameService(config);
        String[][] matrix = gameService.generateMatrix();
        GameResult result = gameService.play(matrix, betAmount);

        String jsonResult = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
        System.out.println("Generated Matrix:");
        for (String[] row: matrix) {
            for (String symbol: row) {
                System.out.print(symbol + " ");
            }
            System.out.println();
        }
        System.out.println("json: ");
        System.out.println(jsonResult);
    }
}
