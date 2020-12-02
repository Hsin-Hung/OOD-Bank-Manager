import java.util.*;


public class Bank {

    String name;
    BankDataBase database;

    public Bank() {


    }

    //create and return the customer object
    public Customer createCustomer(String username, String password){

        return null;


    }
    //authenticate username and password
    public Customer userAuth(String username, String password) {

            return null;

    }

    //create a checking account
    public void createCheckingAccount(Customer customer) {


    }

    //create a savings account
    public void createSavingsAccount(Customer customer) {



    }
    //create a checking account
    public void createSecuritiesAccount(Customer customer){


    }


    public String getAccountType(BankAccount ba) {

            return ba.getType();

    }

    public void updateInterests(BankAccount ba){




    }

    //deposit amount to a bank account
    private void deposit(BankAccount ba, int amount) {



    }

    //withdraw an amount from bank account
    private boolean withdraw(BankAccount ba, int amount) {

        //check if the bank account has enough balance
        return false;
    }


    private void requestLoan(int amount) {


    }

    public boolean transferMoney(BankAccount fromBank, BankAccount toBank, int amount) {

            return true;


    }



}
