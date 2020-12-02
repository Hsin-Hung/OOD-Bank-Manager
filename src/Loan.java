import java.math.BigDecimal;
import java.util.Date;

public class Loan {
    int lid;
    int uid;
    String currency;
    BigDecimal amount;
    String collateral;
    Date date;

    public Loan(int lid, int uid, String currency, BigDecimal amount, String collateral, Date date) {
        this.lid = lid;
        this.uid = uid;
        this.currency = currency;
        this.amount = amount;
        this.collateral = collateral;
        this.date = date;
    }
}
