import java.util.*;

public class Customer extends Person{


	int balance;// the balance this customer holds/withdraws
	ArrayList<Loan> loans;
	ArrayList<BankAccount> bankAccounts;

	
	public Customer(String username, String user_id, String password) {
		super(username, user_id, password);
		this.role = "customer";
	}

	@Override
	public String getRole() {
		return role;
	}

	//use the given ATM
	public void useATM(ATM atm) {

		atm.useATM();


		
	}
	
	public int getBalance() {
		
		return balance;
		
	}



}
