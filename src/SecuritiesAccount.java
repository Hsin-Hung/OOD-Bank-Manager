import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SecuritiesAccount extends BankAccount {
    private List<StockPosition> stockPositions = new ArrayList<>();

    public SecuritiesAccount(int account_id, int user_id, String currency, BigDecimal balance) {
        super(account_id, user_id, currency, balance, AccountType.SECURITIES);
    }

    /**
     * @return list of stock positions owned
     */
    public List<StockPosition> getStockPositions() {
        return stockPositions;
    }

    /**
     * Used to initialize stock position from the customer
     *
     * @param stockPositions List of stock position to be stored
     */
    public void setStockPositions(List<StockPosition> stockPositions) {
        this.stockPositions = stockPositions;
    }

    /**
     * Get the corresponding stock position for this stock
     *
     * @param symbol Symbol of the stock to be looked up
     * @return The StockPosition of the symbol if owned, otherwise null
     */
    public StockPosition getStockPosition(String symbol) {
        for (StockPosition stockPosition : stockPositions) {
            if (stockPosition.getSymbol().equals(symbol)) {
                return stockPosition;
            }
        }
        return null;
    }

    /**
     * Check if the securities account owns shares of this stock
     *
     * @param symbol Stock symbol of the stock to be checked
     * @return true if stock is owned
     */
    public boolean hasStock(String symbol) {
        for (StockPosition stockPosition : stockPositions) {
            if (stockPosition.getSymbol().equals(symbol)) return true;
        }
        return false;
    }

    /**
     * Buy stocks, log it to the database, and take the money away from the security account.
     *
     * @param symbol Symbol of stock to buy
     * @param shares Amount of shares to buy
     * @param db     DBManager reference
     * @return true if the purchase was successful
     */
    public boolean buyStocks(String symbol, int shares, DBManager db) {
        Stock stock = StockMarket.getStock(symbol);
        BigDecimal totalCost = stock.getMarketVal().multiply(new BigDecimal(shares));

        //check if enough balance to buy stocks
        if (getBalance().compareTo(totalCost) == -1) return false;

        for (StockPosition sp : stockPositions) {
            if (sp.isOfStock(stock)) {
                StockPosition stockPosition = sp.buyStock(stock, shares);
                return db.updateStockPosition(getUSER_ID(), symbol, stockPosition.getShares(), stockPosition.getAvgCost());
            }
        }

        if (db.addStockPosition(getUSER_ID(), symbol, shares, stock.getMarketVal())) {
            stockPositions.add(new StockPosition(getUSER_ID(), symbol, shares, stock.getMarketVal()));
            return true;
        }

        return false;
    }

    /**
     * Sells stocks, log it to the database, and add the money to the security account.
     *
     * @param symbol Symbol of stock to sell
     * @param shares Amount of shares to sell
     * @param db     DBManager reference
     * @return true if the sell was successful
     */
    public boolean sellStocks(String symbol, int shares, DBManager db) {
        Stock stock = StockMarket.getStock(symbol);

        StockPosition remove = null, isSold = null;
        for (StockPosition sp : stockPositions) {
            if (sp.isOfStock(stock)) {
                isSold = sp.sellStock(stock, shares);
                if (sp.getShares() == 0) {
                    remove = sp;
                    break;
                }
            }
        }

        if (isSold != null) {
            db.updateStockPosition(getUSER_ID(), symbol, isSold.getShares(), isSold.getAvgCost());
        }

        if (remove != null) {
            if (db.deleteStockPosition(getUSER_ID(), symbol)) stockPositions.remove(remove);
        }

        return isSold != null;
    }
}
