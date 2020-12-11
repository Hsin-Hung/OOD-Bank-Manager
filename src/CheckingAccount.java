import java.math.BigDecimal;

public class CheckingAccount extends BankAccount {
    public CheckingAccount(int account_id, int user_id, String currency, BigDecimal balance) {
        super(account_id, user_id, currency, balance, AccountType.CHECKING);
    }
}
