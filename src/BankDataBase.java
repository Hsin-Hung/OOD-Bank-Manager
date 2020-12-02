import java.math.BigDecimal;

//TODO - do the database querying here
public class BankDataBase {

    public BankDataBase() {


    }
    public boolean addNewCustomer(Customer customer){


        //TODO - add the new customer to the databse
        return true;
    }

    public boolean addNewBankAccount(BankAccount ba){

        //TODO - add the new bank account to the database

        return true;
    }

    public boolean updateInterests(){

        //TODO - update the interests for all customers and bank accounts in the database
        return true;
    }

    public boolean updateCustomer(Customer customer){

        //TODO - basically update this customer with its new info to the database
        return true;
    }

    public boolean updateBankAccount(BankAccount ba){

        //TODO - update the given bank account in the database
        return true;
    }

    public Customer getCustomer(String username, String password){

        //TODO - get the customer of the username and password, null if none
        return null;
    }

}
