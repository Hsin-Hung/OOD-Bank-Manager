import java.math.BigDecimal;
import java.util.Date;

public class Transaction {
    protected int tid;
    protected Date date;
    protected TransactionType type;
    protected BigDecimal amount;
    protected String currency;
    protected int uid;
    protected int account_id;
    protected int target_uid = -1;
    protected int target_account_id = -1;
    protected String collateral = null;

    public Transaction(int tid, Date date, TransactionType type, BigDecimal amount, String currency, int uid, int account_id,
                       int target_uid, int target_account_id, String collateral) {
        this.tid = tid;
        this.date = date;
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.uid = uid;
        this.account_id = account_id;
        this.target_uid = target_uid;
        this.target_account_id = target_account_id;
        this.collateral = collateral;
    }

    public Transaction(int tid, Date date, TransactionType type, BigDecimal amount, String currency, int uid, int account_id) {
        this(tid, date, type, amount, currency, uid, account_id, -1, -1, null);
    }

    public int getTid() {
        return tid;
    }

    public Date getDate() {
        return date;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public int getUid() {
        return uid;
    }

    public int getAccount_id() {
        return account_id;
    }

    public int getTarget_uid() {
        return target_uid;
    }

    public int getTarget_account_id() {
        return target_account_id;
    }

    public String getCollateral() {
        return this.collateral;
    }
}
