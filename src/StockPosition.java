import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * this class represents a stock position a user holds
 */
public class StockPosition {

    private final int uid; // the customer who owns this stock position
    private final String symbol;
    private int shares; // number of shares
    private BigDecimal avgCost;

    public StockPosition(int uid, String symbol, int shares, BigDecimal avgCost) {
        this.uid = uid;
        this.symbol = symbol;
        this.shares = shares;
        this.avgCost = avgCost;
    }

    public int getUid() {
        return uid;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getShares() {
        return shares;
    }

    public BigDecimal getAvgCost() {
        return avgCost;
    }

    public Stock getStock() {
        return StockMarket.getStock(symbol);
    }

    public BigDecimal getMarketValue() {
        return getStock().getMarketVal();
    }

    public boolean isOfStock(Stock s) {
        return symbol.equals(s.getSymbol());
    }

    /**
     * Get the total money spent on buying current shares.
     *
     * @return Total cost
     */
    public BigDecimal getTotalCost() {
        return avgCost.multiply(new BigDecimal(shares));
    }

    /**
     * Get the amount of money you'd get if you sold everything
     *
     * @return Total Return
     */
    public BigDecimal getTotalReturn() {
        BigDecimal sellPrice = getMarketValue().multiply(new BigDecimal(shares));
        return sellPrice.subtract(getTotalCost());
    }

    /**
     * Buy stocks of this company
     *
     * @param newStock stock to be purchased
     * @param shares   amount of shares to buy
     * @return This Object IF newStock matches the stock position
     */
    public StockPosition buyStock(Stock newStock, int shares) {
        if (isOfStock(newStock)) {
            this.shares += shares;
            BigDecimal buyCost = newStock.getMarketVal().multiply(new BigDecimal(shares)).stripTrailingZeros();
            
            avgCost = getTotalCost().add(buyCost).divide(new BigDecimal(Integer.toString(this.shares)),2, RoundingMode.CEILING);
            return this;
        }
        return null;
    }

    /**
     * Buy stocks of this company
     *
     * @param sellStock stock to be sold
     * @param shares    amount of shares to sell
     * @return This Object IF sellStock matches the stock position
     */
    public StockPosition sellStock(Stock sellStock, int shares) {
        if (isOfStock(sellStock) && (this.shares >= shares)) {
            this.shares -= shares;
            return this;
        }
        return null;
    }
}
