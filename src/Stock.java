import java.math.BigDecimal;

public class Stock {

    private int sid;
    private String symbol;
    private String name;
    private BigDecimal marketVal; // current market value

    public Stock(int sid, String symbol, String name, BigDecimal marketVal) {
        this.sid = sid;
        this.symbol = symbol;
        this.name = name;
        this.marketVal = marketVal;
    }

    public int getSid() {
        return sid;
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

    public void setMarketVal(BigDecimal marketVal) {
        this.marketVal = marketVal;
    }
}


