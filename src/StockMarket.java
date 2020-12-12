import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * this class represents the stock market, which contains all types of stocks in the market
 */
public class StockMarket {

    private static List<Stock> stocks;
    private static List<String> rawSymbols;

    static {
        init();
    }

    public static List<Stock> getStocks() {
        return stocks;
    }

    public static Stock getStock(String symbol) {
        for (Stock s : stocks) {
            if (s.getSymbol().equals(symbol)) {
                return s;
            }
        }
        return null;
    }

    /**
     * Queries "Financial Modeling Prep"'s stock api to fetch the latest stock prices.
     *
     * @return true if the update was successful
     */
    public static boolean updatePrices() {
        List<String> prices = new ArrayList<>();
        List<String> symbols = new ArrayList<>();

        try {
            List<String> query = new ArrayList<>();
            for (String stock : rawSymbols) {
                if (query.size() <= 1000) {
                    query.add(stock);
                    continue;
                }
                URL url = new URL("https://financialmodelingprep.com/api/v3/quote/" + String.join(",", query) + "?apikey=f713a83175295761a138c51218623e24");

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
                    for (String line; (line = reader.readLine()) != null; ) {
                        if (line.contains("\"symbol\" :")) {
                            symbols.add(line.split(": ")[1].split(",")[0].replace('"', '\0'));
                        }

                        if (line.contains("\"price\" : ")) {
                            prices.add(line.split(": ")[1].split(",")[0]);
                        }
                    }
                }

                query.clear();
            }

            /*
              "symbol" : "GDOT",
              "name" : "Green Dot Corporation",
              "price" : 55.30000000,
              "changesPercentage" : 0.04000000,
              "change" : 0.02000000,
              "dayLow" : 54.45000000,
              "dayHigh" : 55.59000000,
              "yearHigh" : 64.97000000,
              "yearLow" : 14.20000000,
              "marketCap" : 2957432832.00000000,
              "priceAvg50" : 55.58205800,
              "priceAvg200" : 50.94485500,
              "volume" : 22896,
              "avgVolume" : 493765,
              "exchange" : "NYSE",
              "open" : 55.21000000,
              "previousClose" : 55.28000000,
              "eps" : 0.90200000,
              "pe" : 61.30820000,
              "earningsAnnouncement" : "2020-11-04T16:05:01.000+0000",
              "sharesOutstanding" : 53479798,
              "timestamp" : 1607355144
             */
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        try {
            FileWriter myWriter = new FileWriter(System.getProperty("user.dir") + "/src/data/prices.csv");
            myWriter.write("symbol,price");
            for (int i = 0; i < prices.size(); i++) {
                myWriter.write("\n" + symbols.get(i) + "," + prices.get(i));
            }
            myWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        init();

        return true;
    }

    private static void init() {
        stocks = new ArrayList<>();
        rawSymbols = new ArrayList<>();
        Map<String, BigDecimal> pricingMap = new HashMap<>();

        int count = 20;
        try {
            List<String> prices = Files.readAllLines(Paths.get(System.getProperty("user.dir") + "/src/data/prices.csv"), StandardCharsets.UTF_8);

            boolean skipFirst = true;
            for (String price : prices) {
                if (skipFirst) {
                    skipFirst = false;
                    continue;
                }
                String[] split = price.split(",");
                String symbol = split[0];
                BigDecimal marketVal = new BigDecimal(split[1].replace("\"", ""));
                pricingMap.put(symbol, marketVal);
            }

            List<String> lines = Files.readAllLines(Paths.get(System.getProperty("user.dir") + "/src/data/stocks.csv"), StandardCharsets.UTF_8);
            skipFirst = true;
            for (String line : lines) {
                if (skipFirst) {
                    skipFirst = false;
                    continue;
                }
                String[] split = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                String symbol = split[0].replace("\"", "");
                rawSymbols.add(symbol);
                String name = split[1].replace("\"", "");
                if (!pricingMap.containsKey(symbol)) {
                    continue;
                }
                Stock stock = new Stock(symbol, name, pricingMap.get(symbol));
                stocks.add(stock);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        stocks = stocks.stream().distinct().collect(Collectors.toList());
    }
}
