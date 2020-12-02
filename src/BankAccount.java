import java.math.BigDecimal;
import java.util.*;

public abstract class BankAccount {
    private final int USER_ID;// the user who owns it
    private final int ACCOUNT_ID;//id that uniquely identifies this bank account
    private final String CURRENCY;
    protected String type;
    private Date date;

    BigDecimal balance = new BigDecimal(0);
    ArrayList<Transaction> transactions;

    public BankAccount(int user_id, int account_id, String currency) {
        this.USER_ID = user_id;
        this.ACCOUNT_ID = account_id;
        this.CURRENCY = currency;
    }

    public abstract String getType();

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public int getAccountID() {
        return ACCOUNT_ID;
    }

    public String getCurrency() {
        return CURRENCY;
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    public ArrayList<Transaction> getTransaction() {
        return transactions;
    }

    public boolean hasEnoughBalance(BigDecimal amount) {

        int res = balance.compareTo(amount);

        if (res == 0 || res == 1) return true;

        return false;

    }

    public boolean withdraw(BigDecimal amount) {

        if (hasEnoughBalance(amount)) {
            balance.subtract(amount);
            return true;
        }

        return false;

    }

    public void deposit(BigDecimal amount) {
        balance.add(amount);
    }
}
