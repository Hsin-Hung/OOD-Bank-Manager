import java.math.BigDecimal;

public class Stock {

    private final String symbol;// this uniquely identifies the stock
    private final String name;
    private final BigDecimal marketVal; // current market value

    public Stock(String symbol, String name, BigDecimal marketVal) {
        this.symbol = symbol;
        this.name = name;
        this.marketVal = marketVal;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getMarketVal() {
        return marketVal;
    }
}


