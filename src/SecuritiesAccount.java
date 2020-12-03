import java.math.BigDecimal;

public class SecuritiesAccount extends BankAccount {
    public SecuritiesAccount(int user_id,  int account_id, String currency, BigDecimal balance) {
        super(account_id,user_id,  currency, balance, AccountType.SECURITIES);
    }


}
