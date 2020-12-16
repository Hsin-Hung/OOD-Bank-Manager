CS611 Final Project - ATM

# Teammates
1.	Name: Richard Andreas
Email: ra7296@bu.edu
BUID: U78371851

2.	Name: Jiun Yan Chen
Email: ericjyc@bu.edu
BUID: U12635715

3.	Name: Hsin Hung Wu
Email: henrywu@bu.edu
BUID: U29935516


# Execution instructions
1.	Unzip the ATM.zip file and enter the directory.
2.	Compile the java program.
javac -cp "lib/*" src/*.java
3.	Execute the program
Windows: ```java -cp "lib/*;src" App```
Mac/Linux: ```java -cp "lib/*:src" App```
4.	Login to the GUI:
a.	As Manager, username “admin”, password “admin”
b.	As Customer, signup and create your own user and pass. 
Username is unique.


# Full documentation
https://ericchen1248.github.io/611-OOD-Bank-Manager/

# Things to note
1.	Initial account only created for manager admin/admin. Other users have to be created.
2.	When opening/closing/depositing into accounts, the user only has to input how much money he wants added. The charge will be a separate transaction.
3.	Internet connection is required to update stocks. An offline method to update stocks is to update the stocks.csv manually.
4.	Bank profits are only added for charging fees, during open and close accounts, checking account transaction and when loan is being paid off.
5.	Bank profits are subtracted during saving interest increase and when loan is being requested from the bank.
6. 	All integer spinners are automatically set to integers or 0, if the value is invalid.

 
# Files

## Main Files 
1.	App.java
a.	Main class that runs entire application
2.	DBManager.java 
a.	Class that handles all database operations.

## Jar Files
3.	Forms_rt.jar
4.	Sqlite-jdbc-3.32.3.2.jar

## Data Files
5.	Currencies.csv
6.	Stocks.csv
7.	Prices.csv


## Screen/GUI
8.	AccountsObject.java
9.	BankProfitObject.java
10.	BankProfitsScreen.form
11.	BaseScreen.java
12.	CustomerInfoScreen.java
13.	CustomerScreen.java
14.	DailyReportScreen.java
15.	ElementObject.java
16.	ElementsScreen.java
17.	IUIElement.java
18.	LoanObject.java
19.	LoanPayoffDialog.java
20.	LoginScreen.java
21.	ManagerScreen.java
22.	NoArgFunction.java
23.	NewAccountDialog.java
24.	NewLoanScreen.java
25.	NewSecuritiesAccountDialog.java
26.	SingleArgMethod.java
27.	SignUpScreen.java
28.	StockActionDialog.java
29.	StockObject.java
30.	TransactionObject.java
31.	ViewStockScreen.java
32.	WithdrawDialog.java
 
## Objects
33.	AccountType.java
a.	Enum that handles all types of accounts (Checking, Savings, Securities, Bank)
34.	ATM.java
a.	Class that serves as a facade or layer between the Bank and the users. The users get to manage their account and perform all sorts of tasks through the ATM without interacting with the Bank directly. 
35.	Bank.java
a.	Class that serves as the bank, which is the backend that deals with the logics and executes instructions that are sent from the ATM.
36.	BankAccount.java
a.	Abstract class that builds into various bank accounts.
37.	BankMainAccount.java
a.	Class that extends BankAccount to track the bank balances/profits/losses.
38.	BankManager.java
a.	Class that extends Person and creates a bank manager object. 
39.	CheckingAccount.java
a.	Class that extends BankAccount to handle a customer’s checking account
40.	Constants.java
a.	Class that handles all hard coded constants
41.	Currency.java
a.	Class that captures all currency needs.
42.	Customer.java
a.	Class that extends Person to handle all customer’s actions with his accounts.
43.	Helper.java
a.	A class that holds static helper functions callable from anywhere.
44.	Loan.java
a.	Class that builds into a loan and handles all loan activities.
45.	Person.java
a.	Abstract class that defines a person
46.	Role.java
a.	Enum class that has person roles (CUSTOMER,MANAGER)
47.	SavingsAccount.java
a.	Class that extends BankAccount to handle a customer’s savings account
48.	SecuritiesAccount.java
a.	Class that extends BankAccount to handle a customer’s securities account
49.	Stock.java
a.	Class that represents the details of any type of stock.
50.	StockPosition.java
a.	Class that represents the amount/details of a particular stock that is owned by a customer.
51.	StockMarket.java
a.	A singleton class that initializes and contains all the stocks in the stock market.

52.	Transaction.java
a.	Class that represents and contains all the details of a particular transaction. 
53.	TransactionType.java
a.	Enum that has all types of bank transactions
