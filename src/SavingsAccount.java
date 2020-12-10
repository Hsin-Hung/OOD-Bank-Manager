import java.math.BigDecimal;

/**
 * 
 */
public class SavingsAccount extends BankAccount {
    public SavingsAccount( int account_id,int user_id,  String currency, BigDecimal balance) {
        super (  account_id,user_id, currency,balance, AccountType.SAVINGS);
    }
}
