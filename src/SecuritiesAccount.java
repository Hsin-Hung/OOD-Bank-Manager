import java.math.BigDecimal;

public class SecuritiesAccount extends BankAccount {
    public SecuritiesAccount(int account_id,  int user_id, String currency, BigDecimal balance) {
        super(account_id,user_id,  currency, balance, AccountType.SECURITIES);
    }


}
