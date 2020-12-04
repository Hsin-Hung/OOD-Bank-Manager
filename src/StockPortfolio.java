import java.math.BigDecimal;
import java.util.*;

//stock portfolio can hold multiple stocks of the same stock id
public class StockPortfolio {

    private int spid; // stock portfolio id
    private int uid;
    private int sid;
    private int shares; // number of shares
    private BigDecimal avgCost;


    public StockPortfolio(int spid, int uid, int sid, int shares, BigDecimal avgCost) {
        this.spid = spid;
        this.uid = uid;
        this.sid = sid;
        this.shares = shares;
        this.avgCost = avgCost;
    }

    public int getSpid() {
        return spid;
    }

    public int getUid() {
        return uid;
    }

    public int getSid() {
        return sid;
    }

    public int getShares() {
        return shares;
    }

    public BigDecimal getAvgCost() {
        return avgCost;
    }

    public void addStock(Stock stock){

        //TODO -

    }
}
