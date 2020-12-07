import java.lang.management.BufferPoolMXBean;
import java.math.BigDecimal;
import java.util.*;

//stock portfolio can hold multiple stocks of the same stock id
public class StockPosition {

    private int uid; // the customer who owns this stock position
    private String symbol;
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

    public Stock getStock(){
        return StockMarket.getStock(symbol);

    }

    public BigDecimal getMarketValue(){ return getStock().getMarketVal();}

    public boolean isOfStock(Stock s){

        return symbol.equals(s.getSymbol());

    }

    public BigDecimal getTotalCost(){

        return avgCost.multiply(new BigDecimal(shares));

    }

    public BigDecimal getTotalReturn(){

        BigDecimal sellPrice = getMarketValue().multiply(new BigDecimal(shares));

        return sellPrice.subtract(getTotalCost());


    }

    //buy stocks of this company
    public void buyStock(Stock newStock, int shares){

        if(isOfStock(newStock)){

            this.shares+=shares;

            BigDecimal buyCost = newStock.getMarketVal().multiply(new BigDecimal(shares));

            avgCost = getTotalCost().add(buyCost).divide(new BigDecimal(this.shares));

        }

    }

    //sell stocks of this company
    public boolean sellStock(Stock sellStock, int shares){

        if(isOfStock(sellStock) && (this.shares>=shares)){

            this.shares-=shares;
            return true;

        }
        return false;
    }
}
