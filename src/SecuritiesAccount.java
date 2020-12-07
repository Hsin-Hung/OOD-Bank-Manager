import java.math.BigDecimal;
import java.util.*;

public class SecuritiesAccount extends BankAccount {

    private List<StockPosition> stockPositions = new ArrayList<>();

    public SecuritiesAccount(int account_id,  int user_id, String currency, BigDecimal balance) {
        super(account_id,user_id,  currency, balance, AccountType.SECURITIES);
    }

    public List<StockPosition> getStockPositions(){

        return stockPositions;

    }

    public boolean hasStock(String symbol){

        for (StockPosition stockPosition: stockPositions){

            if(stockPosition.getSymbol().equals(symbol)) return true;

        }
        return false;

    }

    public boolean buyStocks(String symbol, int shares){

        Stock stock = StockMarket.getStock(symbol);

        BigDecimal totalCost = stock.getMarketVal().multiply(new BigDecimal(shares));

        //check if enough balance to buy stocks
        if( getBalance().compareTo(totalCost)==-1)return false;

        withdraw(totalCost);

        for (StockPosition sp : stockPositions){

                if(sp.isOfStock(stock)){
                    sp.buyStock(stock, shares);
                    return true;
                }

        }

        stockPositions.add(new StockPosition(getUSER_ID(), symbol, shares, stock.getMarketVal()));
        return true;
    }

    public boolean sellStocks(String symbol, int shares){

        Stock stock = StockMarket.getStock(symbol);

        BigDecimal totalPrice = stock.getMarketVal().multiply(new BigDecimal(shares));
        Boolean isSold = false;

        for(StockPosition sp : stockPositions){

            if(sp.isOfStock(stock)){

                isSold = sp.sellStock(stock, shares);

            }

        }

        if(isSold)deposit(totalPrice);

        return isSold;
    }


}
