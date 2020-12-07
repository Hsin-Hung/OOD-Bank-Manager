import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;
import java.util.stream.Collectors;


public class StockMarket {

    private static List<Stock> stocks;

    public static List<Stock> getStocks() {

        if(stocks == null){
            init();

        }
        return stocks;
    }

    public static Stock getStock(String symbol){

        if(stocks == null){
            init();

        }

        for(Stock s: stocks){

            if(s.getSymbol().equals(symbol)){

                return s;

            }


        }

        return null;



    }

    private static void init() {

        stocks = new ArrayList<>();
        int count = 20;
        try {
            List<String> lines = Files.readAllLines(Paths.get(System.getProperty("user.dir") + "/src/data/stocks.csv"), StandardCharsets.UTF_8);
            boolean skipFirst = true;
            for (String line : lines) {
                if (skipFirst) {
                    skipFirst = false;
                    continue;
                }
                String[] split = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                String symbol = split[0].replace("\"", "");
                String name = split[1].replace("\"", "");
                try {
                    BigDecimal marketVal = new BigDecimal(split[2].replace("\"", ""));
                    stocks.add(new Stock(symbol, name, marketVal));

                }catch (NumberFormatException e){

                    continue;

                }

                if(--count==0)break;


            }
        } catch (IOException e) {
            e.printStackTrace();
        }

      stocks = stocks.stream().distinct().collect(Collectors.toList());

    }



}
