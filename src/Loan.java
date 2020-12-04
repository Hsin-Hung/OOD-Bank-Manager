import java.math.BigDecimal;
import java.util.Date;

public class Loan {
    private int lid;
    private int uid;
    private String currency;
    private BigDecimal amount;
    private String collateral;


    public Loan(int lid, int uid, String currency, BigDecimal amount, String collateral) {
        this.lid = lid;
        this.uid = uid;
        this.currency = currency;
        this.amount = amount;
        this.collateral = collateral;

    }

    public int getLid() {
        return lid;
    }

    public void setLid(int lid) {
        this.lid = lid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCollateral() {
        return collateral;
    }

    public void setCollateral(String collateral) {
        this.collateral = collateral;
    }
}
