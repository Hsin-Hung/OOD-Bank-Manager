import java.math.BigDecimal;

/**
 * this class represents the savings account, which is a type of bank account
 */
public class SavingsAccount extends BankAccount {
    private boolean SecurityBackingAccount = false;

    public SavingsAccount(int account_id, int user_id, String currency, BigDecimal balance) {
        super(account_id, user_id, currency, balance, AccountType.SAVINGS);
    }

    public void setAsSecurityBackingAccount() {
        SecurityBackingAccount = true;
    }

    @Override
    public boolean hasEnoughBalance(BigDecimal amount) {
        if (SecurityBackingAccount) {
            amount = amount.add(Constants.minimalSecurityAmount);
        }

        return super.hasEnoughBalance(amount);
    }

    public boolean canClose() {
        return !isSecurityBackingAccount();
    }

    public boolean isSecurityBackingAccount() {
        return SecurityBackingAccount;
    }
}
