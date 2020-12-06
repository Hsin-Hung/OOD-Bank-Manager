import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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
        String str;
        try (Scanner sc = new Scanner(new File(System.getProperty("user.dir") + "/src/data/currencies.csv"))) {
            sc.useDelimiter("\\n");
            str = sc.next();
            while (sc.hasNext())
            {
                String[] split = sc.next().split(",");
                currencyList.add(split[0]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        currencyList = currencyList.stream().distinct().collect(Collectors.toList());
    }
}
