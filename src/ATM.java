
// all the logics are done in the bank, ATM is just a fascade for the bank
public class ATM {

	Bank bank;// the bank that connects this ATM
	Customer loggedInCustomer; // the customer who logged in to this ATM

	public ATM(Bank bank) {

		this.bank = bank;

	}

	//create a new customer
	private void signUp(){

		//enter username and password
		// loggedInCustomer = bank.createCustomer(username,password)

	}

	//prompt for username and password and set 'loggedInCustomer' as the logged in customer
	private boolean logIn() {

		//input username and password
		//loggedInCustomer = bank.userAuth(username, password)


		return true;
	}

	private void logOut(){

		loggedInCustomer = null;

	}

	private void createCheckingAccount() {
		//bank.createCheckingAccount(Customer customer)

	}

	private void createSavingsAccount() {
		//bank.createSavingsAccount(Customer customer)

	}

	private void createSecuritiesAccount(){

		//bank.createSecuritiesAccount(Customer customer)

	}

	private void deposit() {

		//bank.deposit(BankAccount ba, int amount)

	}

	private void withdraw() {

		//bank.withdraw(BankAccount ba, int amount)

	}

	private void requestLoan() {

		//bank.requestLoan(int amount)
	}

	private void transferMoney(){

		//bank.transferMoney(BankAccount fromBank, BankAccount toBank, int amount)


	}

	private void viewTransactions() {


	}

	private void viewBalance() {




	}



	//use ATM, starting point of ATM
	public void useATM() {

		if(logIn()){


			//manage the accounts



		}


	}
	

}
