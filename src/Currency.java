import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * this class represents currency and it contains different types of currencies
 */
public class Currency {
    private static List<String> currencyList;

    public static List<String> getCurrencyList() {
        if (currencyList == null) {
            init();
        }
        return currencyList;
    }

    private static void init() {
        currencyList = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(System.getProperty("user.dir") + "/src/data/currencies.csv"), StandardCharsets.UTF_8);
            boolean skipFirst = true;
            for (String line : lines) {
                if (skipFirst) {
                    skipFirst = false;
                    continue;
                }
                String[] split = line.split(",");
                currencyList.add(split[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        currencyList = currencyList.stream().distinct().collect(Collectors.toList());
    }
}
