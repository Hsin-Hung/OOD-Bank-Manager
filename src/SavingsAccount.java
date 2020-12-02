
public class SavingsAccount extends BankAccount{

    public SavingsAccount(int user_id, int account_id, String currency){

        super(user_id, account_id, currency);
        type = "savings";

    }

    @Override
    public String getType() {
        return type;
    }

}
