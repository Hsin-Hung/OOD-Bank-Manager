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

    public void setStockPositions(List<StockPosition> stockPositions){this.stockPositions = stockPositions;}

    public StockPosition getStockPostition(String symbol){

        for (StockPosition stockPosition: stockPositions){

            if(stockPosition.getSymbol().equals(symbol)){

                return stockPosition;

            }
        }
        return null;

    }

    public boolean hasStock(String symbol){

        for (StockPosition stockPosition: stockPositions){

            if(stockPosition.getSymbol().equals(symbol)) return true;

        }
        return false;

    }


    public boolean buyStocks(String symbol, int shares, DBManager db){

        Stock stock = StockMarket.getStock(symbol);

        BigDecimal totalCost = stock.getMarketVal().multiply(new BigDecimal(shares));

        //check if enough balance to buy stocks
        if(getBalance().compareTo(totalCost)==-1)return false;

        for (StockPosition sp : stockPositions){

                if(sp.isOfStock(stock)){

                    //im lazy
                    StockPosition stockPosition = sp.buyStock(stock, shares);
                    return db.updateStockPosition(getUSER_ID(), symbol, stockPosition.getShares(), stockPosition.getAvgCost());

                }

        }

        if(db.addStockPosition(getUSER_ID(), symbol, shares, stock.getMarketVal())){
            stockPositions.add(new StockPosition(getUSER_ID(), symbol, shares, stock.getMarketVal()));
            return true;
        }

        return false;
    }

    public boolean sellStocks(String symbol, int shares, DBManager db){

        Stock stock = StockMarket.getStock(symbol);

        BigDecimal totalPrice = stock.getMarketVal().multiply(new BigDecimal(shares));
        StockPosition remove = null, isSold = null;


        for(StockPosition sp : stockPositions){

            if(sp.isOfStock(stock)){

                isSold = sp.sellStock(stock, shares);

                if(sp.getShares()==0){
                    remove = sp;
                    break;
                }

            }

        }

        if(isSold != null){
            db.updateStockPosition(getUSER_ID(), symbol, isSold.getShares(), isSold.getAvgCost());
        }

        if(remove != null){

            if(db.deleteStockPosition(getUSER_ID(),symbol))stockPositions.remove(remove);

        }

        return isSold != null;
    }


}
