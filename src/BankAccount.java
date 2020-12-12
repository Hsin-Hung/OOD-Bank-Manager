import java.math.BigDecimal;

/**
 * BankAccount.java - Abstract class and can represent any type of BankAccount
 */
public abstract class BankAccount {
    private final int ACCOUNT_ID;//id that uniquely identifies this bank account
    private final int USER_ID;// the user who owns it
    private final String CURRENCY;
    private final AccountType type;
    private BigDecimal balance = new BigDecimal(0);

    public BankAccount(int account_id, int user_id, String currency, BigDecimal balance, AccountType type) {
        this.ACCOUNT_ID = account_id;
        this.USER_ID = user_id;
        this.CURRENCY = currency;
        this.type = type;
        this.balance = balance;
    }

    public int getUSER_ID() {
        return USER_ID;
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
        return res == 0 || res == 1;
    }

    public boolean withdraw(BigDecimal amount) {
        if (hasEnoughBalance(amount)) {
            balance = balance.subtract(amount);
            return true;
        }

        return false;
    }

    public void deposit(BigDecimal amount) {
        balance = balance.add(amount);
    }
}
