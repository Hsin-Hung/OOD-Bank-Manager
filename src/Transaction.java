import java.math.BigDecimal;
import java.util.Date;

public class Transaction {
    int tid;
    Date date;
    String type;
    BigDecimal amount;
    int uid;
    int account_id;
    int target_uid = -1;
    int target_account_id = -1;

    public Transaction(int tid, Date date, String type, BigDecimal amount, int uid, int account_id,
                       int target_uid, int target_account_id) {
        this.tid = tid;
        this.date = date;
        this.type = type;
        this.amount = amount;
        this.uid = uid;
        this.account_id = account_id;
        this.target_uid = target_uid;
        this.target_account_id = target_account_id;
    }

    public Transaction(int tid, Date date, String type, BigDecimal amount, int uid, int account_id) {
        this(tid, date, type, amount, uid, account_id, -1, -1);
    }
}
