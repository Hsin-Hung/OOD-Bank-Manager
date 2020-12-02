public class CheckingAccount extends BankAccount {
    public CheckingAccount(int user_id, int account_id, String currency) {
        super(user_id, account_id, currency);
        type = "checking";
    }

    @Override
    public String getType() {
        return type;
    }
}
