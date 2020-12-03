import java.math.BigDecimal;
import java.util.*;

public abstract class BankAccount {
    private int ACCOUNT_ID;//id that uniquely identifies this bank account
    private int USER_ID;// the user who owns it
    private String CURRENCY;
    private AccountType type;
    BigDecimal balance = new BigDecimal(0);

    public BankAccount(int account_id,int user_id,  String currency, BigDecimal balance, AccountType type ) {
        this.ACCOUNT_ID = account_id;
        this.USER_ID = user_id;
        this.CURRENCY = currency;
        this.type = type;
        this.balance = balance;
    }

    public AccountType getType() {
        return type;
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
