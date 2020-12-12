import java.math.BigDecimal;

/**
 * this class represents a checking account, which is a type of Bank account
 */
public class CheckingAccount extends BankAccount {
    public CheckingAccount(int account_id, int user_id, String currency, BigDecimal balance) {
        super(account_id, user_id, currency, balance, AccountType.CHECKING);
    }
}
