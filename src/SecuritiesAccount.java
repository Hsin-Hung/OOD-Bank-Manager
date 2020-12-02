
public class SecuritiesAccount extends BankAccount{

    public SecuritiesAccount(int user_id, int account_id, String currency){

        super(user_id, account_id, currency);
        type = "securities";
    }

    @Override
    public String getType() {
        return type;
    }
}
