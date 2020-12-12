import java.math.BigDecimal;

/**
 * BankMainAccount.java - Main bank account for the bank to show profits and losses.
 */
public class BankMainAccount extends BankAccount {
    public BankMainAccount(int account_id, int user_id, String currency, BigDecimal balance) {
        super(account_id, user_id, currency, balance, AccountType.BANK);
    }
}
