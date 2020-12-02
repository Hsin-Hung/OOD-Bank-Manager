import java.math.BigDecimal;
import java.util.*;
import java.math.*;

public abstract class BankAccount {

    String type;
    final int USER_ID;// the user who owns it
    final int ACCOUNT_ID;//id that uniquely identifies this bank account
    final String CURRENCY;
    Date date;

    BigDecimal balance = new BigDecimal(0);
    ArrayList<Transaction> transactions;

    public BankAccount(int user_id, int account_id, String currency) {
        this.USER_ID = user_id;
        this.ACCOUNT_ID = account_id;
        this.CURRENCY = currency;

    }

    public abstract String getType();

    public void setBalance(BigDecimal balance){

        this.balance = balance;

    }

    public void setDate(Date date){

        this.date = date;

    }

    public Date getDate(){
        return date;
    }

    public BigDecimal getBalance() {

        return balance;

    }

    public int getAccountID() {

        return ACCOUNT_ID;
    }

    public void addTransaction(Transaction t) {

        transactions.add(t);
    }

    public ArrayList<Transaction> getTransaction(){
        return transactions;

    }

    public boolean hasEnoughBalance(int amount) {

            return true;

    }

    public int withdraw(int amount) {

//        if(hasEnoughBalance(amount)) {
//
//
//
//        }

        return 0;

    }

    public void deposit(int amount) {




    }



}
