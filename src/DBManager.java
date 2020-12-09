import java.math.BigDecimal;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;
import java.util.Date;

/*
 * DBManager.java - class that handles all database operations
 */
public class DBManager {
    private Connection conn = null;

    public DBManager() {
        try {
            // db parameters
            String file = System.getProperty("user.dir") + "/src/db/";
            String url = "jdbc:sqlite://" + file + "atm.db";
            System.out.println("URL=" + url);
            // create a connection to the database

            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
//            dropTables();
//            System.out.println("Tables dropped");
            createTables();
            System.out.println("Tables created");
         //   addDefaultManager();
            System.out.println("Add default manager");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void dropTables() {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String sql = "DROP TABLE USERS";
            stmt.execute(sql);
            sql = "DROP TABLE ACCOUNTS";
            stmt.execute(sql);
            sql = "DROP TABLE TRANSACTIONS";
            stmt.execute(sql);
            sql = "DROP TABLE LOANS";
            stmt.execute(sql);
            sql = "DROP TABLE STOCKS";
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean createTables() {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS USERS (" +
                    "ID INTEGER NOT NULL UNIQUE," +
                    "NAME TEXT NOT NULL," +
                    "USERNAME TEXT NOT NULL UNIQUE," +
                    "PASSWORD TEXT NOT NULL," +
                    "ROLE TEXT NOT NULL," +
                    "PRIMARY KEY(ID AUTOINCREMENT)" +
                    ")";
            stmt.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS TRANSACTIONS(" +
                    "ID INTEGER NOT NULL UNIQUE, " +
                    "TYPE TEXT NOT NULL, " +
                    "USERID INTEGER NOT NULL, " +
                    "ACCOUNTID INTEGER, " +
                    "AMOUNT REAL, " +
                    "CURRENCY TEXT, " +
                    "TARGETACCOUNTID INTEGER, " +
                    "TARGETUSERID INTEGER, " +
                    "COLLATERAL TEXT, " +
                    "DATE TEXT NOT NULL, " +
                    "PRIMARY KEY(ID AUTOINCREMENT))";
            stmt.execute(sql);
            sql = "CREATE TABLE IF NOT EXISTS STOCKS(" +
                    "SYMBOL TEXT NOT NULL UNIQUE, " +
                    "USERID INTEGER NOT NULL, " +
                    "SHARES INTEGER NOT NULL, " +
                    "AVGCOST REAL NOT NULL, " +
                    "PRIMARY KEY(SYMBOL))";
            stmt.execute(sql);
            sql = "CREATE TABLE IF NOT EXISTS ACCOUNTS (" +
                    "ID INTEGER NOT NULL UNIQUE," +
                    "USERID INTEGER NOT NULL," +
                    "TYPE TEXT NOT NULL," +
                    "AMOUNT REAL NOT NULL," +
                    "CURRENCY TEXT NOT NULL," +
                    "PRIMARY KEY(ID AUTOINCREMENT)" +
                    ")";
            stmt.execute(sql);
            sql = "CREATE TABLE IF NOT EXISTS LOANS (" +
                    "ID INTEGER NOT NULL UNIQUE," +
                    "USERID INTEGER NOT NULL," +
                    "AMOUNT REAL NOT NULL," +
                    "CURRENCY TEXT NOT NULL," +
                    "COLLATERAL TEXT NOT NULL," +
                    "PRIMARY KEY(ID AUTOINCREMENT)" +
                    ")";
            stmt.execute(sql);
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;

        }
        return true;
    }

    public void addDefaultManager() {
        String sql = "INSERT INTO USERS(NAME,USERNAME,PASSWORD,ROLE) VALUES (\"MANAGER\",\"admin\",\"admin\",\"MANAGER\");";
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.execute(sql);
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean addUser(String name, String user, String pass, Role role) {
        String sql = "INSERT INTO USERS(NAME,USERNAME,PASSWORD,ROLE) VALUES (?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, user);
            stmt.setString(3, pass);
            stmt.setString(4, role.toString());
            stmt.execute();
            stmt.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public BankAccount addAccount(Customer c, AccountType type, BigDecimal amount, String currency) {
        String sql = "INSERT INTO ACCOUNTS(USERID,TYPE,AMOUNT,CURRENCY) VALUES (?,?,?,?)";
        BankAccount account = null;
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, c.getUid());
            stmt.setString(2, type.toString());
            stmt.setBigDecimal(3, amount);
            stmt.setString(4, currency);
            stmt.execute();

            sql = "SELECT ID FROM ACCOUNTS WHERE USERID = ? AND TYPE = ? AND CURRENCY = ? ";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, c.getUid());
            stmt.setString(2, type.toString());
            stmt.setString(3, currency);

            ResultSet rs = stmt.executeQuery();

            switch(type.toString()) {
                case "CHECKING":
                    account = new CheckingAccount(rs.getInt(1),c.getUid(),currency,amount);
                    break;
                case "SAVINGS":
                    account = new SavingsAccount(rs.getInt(1),c.getUid(),currency,amount);
                    break;
                case "SECURITIES":
                    account = new SecuritiesAccount(rs.getInt(1),c.getUid(),currency,amount);
                    break;

            }

            stmt.close();


        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return account;
    }

    public BankMainAccount addBankMainAccount( BigDecimal amount, String currency) {
        String sql = "INSERT INTO ACCOUNTS(USERID,TYPE,AMOUNT,CURRENCY) VALUES (?,?,?,?)";
        BankMainAccount account = null;
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, 1 );
            stmt.setString(2, AccountType.BANK.toString());
            stmt.setBigDecimal(3, amount);
            stmt.setString(4, currency);
            stmt.execute();

            sql = "SELECT ID FROM ACCOUNTS WHERE USERID = ? AND TYPE = ? AND CURRENCY = ? ";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, 1);
            stmt.setString(2, AccountType.BANK.toString());
            stmt.setString(3, currency);

            ResultSet rs = stmt.executeQuery();
            account = new BankMainAccount(rs.getInt(1),1,currency,amount);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        return account;
    }

    public boolean updateBankMainAccount( BigDecimal amount, String currency) {
        String sql = "UPDATE ACCOUNTS SET AMOUNT = ? WHERE TYPE = ? AND CURRENCY = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setBigDecimal(1, amount);
            stmt.setString(2, AccountType.BANK.toString());
            stmt.setString(3, currency);
            stmt.execute();
            stmt.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }


    public Transaction addTransaction(TransactionType type,int userid, int accountId, BigDecimal amount, String currency, int targetUserId, int targetAccountId, String collateral) {
        String sql = "INSERT INTO TRANSACTIONS(DATE,TYPE,AMOUNT,CURRENCY,USERID,ACCOUNTID,TARGETUSERID,TARGETACCOUNTID,COLLATERAL) VALUES (?,?,?,?,?,?,?,?,?)";
        Transaction t = null;
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
            String strDate = dateFormat.format(date);
            stmt.setString(1, strDate);
            stmt.setString(2, type.toString());
            stmt.setBigDecimal(3, amount);
            stmt.setString(4, currency);
            stmt.setInt(5, userid);
            stmt.setInt(6, accountId);
            stmt.setInt(7, targetUserId);
            stmt.setInt(8, targetAccountId);
            stmt.setString(9, collateral);
            stmt.execute();


            sql = "SELECT MAX(ID) FROM TRANSACTIONS";
            stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            
            t = new Transaction(rs.getInt(1),date,type,amount,currency,userid,accountId,targetUserId, targetAccountId, collateral);

            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return t;
    }

    public boolean deleteAccount(Customer c, int accountId) {
        String sql = "DELETE FROM ACCOUNTS WHERE ID = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId);
            stmt.execute();
            stmt.close();
            Transaction t  = addTransaction(TransactionType.CLOSE,c.getUid(), accountId, null,null,-1,-1, null);
            c.addTransaction(t);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean updateAmount(int accountId, BigDecimal amount) {
        String sql = "UPDATE ACCOUNTS SET AMOUNT = ? WHERE ID = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setBigDecimal(1, amount);
            stmt.setInt(2, accountId);
            stmt.execute();
            stmt.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public Loan addLoan(Customer c, BigDecimal amount, String currency, String collateral) {
        String sql = "INSERT INTO LOANS(USERID,AMOUNT,CURRENCY,COLLATERAL) VALUES (?,?,?,?)";
        Loan loan = null;

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, c.getUid());
            stmt.setBigDecimal(2, amount);
            stmt.setString(3, currency);
            stmt.setString(4, collateral);
            stmt.execute();

            sql = "SELECT MAX(ID) FROM LOANS";
            stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            stmt = conn.prepareStatement(sql);
            loan = new Loan(rs.getInt(1), c.getUid(), currency, amount, collateral);


        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return loan;
    }

    public boolean updateLoanAmount(int id, BigDecimal amount) {
        String sql = "UPDATE LOANS SET AMOUNT = ? WHERE ID = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setBigDecimal(1, amount);
            stmt.setInt(2, id);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean removeLoan(int id) {
        String sql = "DELETE FROM  LOANS WHERE ID = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean deleteLoan(Customer c, int id) {
        String sql = "DELETE FROM ACCOUNTS WHERE ID = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.execute();
            stmt.close();
            Transaction t  = addTransaction(TransactionType.CLOSELOAN,c.getUid(), id, null,null,  -1,-1,null);
            c.addTransaction(t);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public Person getPerson(String username) {
        String sql = "SELECT ID, NAME, USERNAME, PASSWORD, ROLE FROM USERS WHERE USERNAME = ?";
        Person p = null;
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            int id = rs.getInt(1);
            String name = rs.getString(2);
            String user = rs.getString(3);
            String password = rs.getString(4);
            String role = rs.getString(5);

            if (role.equals("CUSTOMER")) {

                List<BankAccount> accounts = getAllUserAccounts(id);
                List<Loan> loans = getAllUserLoans(id);
                List<Transaction> transactions = getAllUserTransaction(id);
                List<StockPosition> stockPositions = getAllStockPosition(id);
                p = new Customer(id,name,user,password,loans,accounts,transactions, stockPositions);
            } else if (role.equals(Role.MANAGER.toString())) {
                p = new BankManager(id,name,user,password);
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }

        return p;
    }

    public Person getPersonFromAccount(int accountId) {
        String sql = "SELECT NAME FROM USERS, ACCOUNTS WHERE USERS.ID = ACCOUNTS.USERID AND ACCOUNTS.ID = ?";
        Person p = null;
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            p = getPerson(rs.getString(1));

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return p;
        }
        return p;

    }

    public Person getPersonFromLoan(int loanId) {
        String sql = "SELECT NAME FROM USERS, LOANS WHERE USERS.ID = LOANS.USERID AND ACCOUNTS.ID = ?";
        Person p = null;
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, loanId);
            ResultSet rs = stmt.executeQuery();
            p = getPerson(rs.getString(1));

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return p;
        }
        return p;

    }
    public List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT ID, USERNAME FROM USERS WHERE ROLE = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, Role.CUSTOMER.toString());
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Person p = getPerson(rs.getString(2));
                list.add((Customer) p);
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }

        return list;
    }

    public List<BankAccount> getAllAccounts() {
        List<BankAccount> accounts = new ArrayList<>();
        try {
            String sql = "SELECT ID FROM ACCOUNTS";
            PreparedStatement stmt2 = conn.prepareStatement(sql);
            ResultSet rs2 = stmt2.executeQuery();

            while (rs2.next()) {
                BankAccount a = getAccount(rs2.getInt(1));
                accounts.add(a);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        return accounts;
    }


    public List<BankMainAccount> getAllBankMainAccounts() {
        List<BankMainAccount> accounts = new ArrayList<>();
        try {
            String sql = "SELECT ID FROM ACCOUNTS WHERE USERID = 1";
            PreparedStatement stmt2 = conn.prepareStatement(sql);
            ResultSet rs2 = stmt2.executeQuery();

            while (rs2.next()) {
                BankMainAccount a = (BankMainAccount) getAccount(rs2.getInt(1));
                accounts.add(a);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        return accounts;
    }
    public BankAccount getAccount(int id) {
        BankAccount account = null;
        try {
            String sql = "SELECT ID, USERID, TYPE, CURRENCY ,AMOUNT FROM ACCOUNTS WHERE ID = ?";
            PreparedStatement stmt2 = conn.prepareStatement(sql);
            stmt2.setInt(1, id);
            ResultSet rs2 = stmt2.executeQuery();


            String accType = rs2.getString(3);
            switch (accType) {
                case "CHECKING":
                    account = new CheckingAccount(
                            rs2.getInt(1),
                            rs2.getInt(2),
                            rs2.getString(4),
                            new BigDecimal(rs2.getString(5)));
                    break;
                case "SAVINGS":
                    account = new SavingsAccount(
                            rs2.getInt(1),
                            rs2.getInt(2),
                            rs2.getString(4),
                            new BigDecimal(rs2.getString(5)));
                    break;
                case "SECURITIES":
                    account = new SecuritiesAccount(
                            rs2.getInt(1),
                            rs2.getInt(2),
                            rs2.getString(4),
                            new BigDecimal(rs2.getString(5)));
                    break;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        return account;
    }




    public List<BankAccount> getAllUserAccounts(int id) {
        List<BankAccount> accounts = new ArrayList<>();
        try {
            String sql = "SELECT ID FROM ACCOUNTS WHERE USERID = ?";
            PreparedStatement stmt2 = conn.prepareStatement(sql);
            stmt2.setInt(1, id);
            ResultSet rs2 = stmt2.executeQuery();

            while (rs2.next()) {
                BankAccount a = getAccount(rs2.getInt(1));
                accounts.add(a);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        return accounts;
    }

    public List<Loan> getAllLoans() {
        List<Loan> loans = new ArrayList<>();

        try {
            String sql = "SELECT ID FROM LOANS";

            PreparedStatement stmt2 = conn.prepareStatement(sql);

            ResultSet rs2 = stmt2.executeQuery();
            while (rs2.next()) {
                Loan l = getLoan(rs2.getInt(1));
                loans.add(l);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        return loans;
    }

    public Loan getLoan(int id) {
        Loan loan = null;
        try {
            String sql = "SELECT ID, USERID, CURRENCY ,AMOUNT, COLLATERAL FROM LOANS WHERE ID = ?";

            PreparedStatement stmt2 = conn.prepareStatement(sql);
            stmt2.setInt(1, id);
            ResultSet rs2 = stmt2.executeQuery();

            loan = new Loan(
                rs2.getInt(1),
                rs2.getInt(2),
                rs2.getString(3),
                new BigDecimal(rs2.getString(4)),
                rs2.getString(5));

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        return loan;
    }

    public List<Loan> getAllUserLoans(int id) {
        List<Loan> loans = new ArrayList<>();

        try {
            String sql = "SELECT ID FROM LOANS WHERE USERID = ?";

            PreparedStatement stmt2 = conn.prepareStatement(sql);
            stmt2.setInt(1, id);
            ResultSet rs2 = stmt2.executeQuery();
            while (rs2.next()) {
                Loan l = getLoan(rs2.getInt(1));
                loans.add(l);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        return loans;
    }
    public List<Transaction> getAllTransaction() {
        List<Transaction> list = new ArrayList<>();

        try {
            String sql = "SELECT ID FROM TRANSACTIONS";

            PreparedStatement stmt2 = conn.prepareStatement(sql);
            ResultSet rs2 = stmt2.executeQuery();
            while (rs2.next()) {
                Transaction l = getTransaction(rs2.getInt(1));
                list.add(l);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        return list;
    }

    public Transaction getTransaction(int id) {
        Transaction t = null;
        try {
            String sql = "SELECT ID, DATE, TYPE ,AMOUNT, CURRENCY, USERID, ACCOUNTID, TARGETACCOUNTID, TARGETUSERID, COLLATERAL FROM TRANSACTIONS WHERE ID = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            String sDate = rs.getString(2);
            Date date = Constants.DATE_FORMAT.parse(sDate);
            t = new Transaction(rs.getInt(1),
                    date,
                    TransactionType.valueOf(rs.getString(3)),
                    new BigDecimal(rs.getInt(4)),
                    rs.getString(5),
                    rs.getInt(6),
                    rs.getInt(7),
                    rs.getInt(8),
                    rs.getInt(9),
                    rs.getString(10));

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        return t;
    }

    public List<Transaction> getAllUserTransaction(int id) {
        List<Transaction> list = new ArrayList<>();

        try {
            String sql = "SELECT ID FROM TRANSACTIONS WHERE USERID = ?";

            PreparedStatement stmt2 = conn.prepareStatement(sql);
            stmt2.setInt(1, id);
            ResultSet rs2 = stmt2.executeQuery();
            while (rs2.next()) {
                Transaction l = getTransaction(rs2.getInt(1));
                list.add(l);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        return list;
    }

    public boolean isDistinctUsername(String username) {
        String sql = "SELECT COUNT(*) FROM USERS WHERE USERNAME = ?";
        boolean res = false;
        try {
            PreparedStatement stmt  = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            int count = rs.getInt(1);
            if(count >= 1) {
                res = false;
            } else {
                res = true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return res;
    }

    public boolean isDistinctAccount(int userid, String currency, AccountType type) {
        String sql = "SELECT COUNT(*) FROM ACCOUNTS WHERE USERID = ? AND CURRENCY = ? AND TYPE = ?";
        boolean res = false;
        try {
            PreparedStatement stmt  = conn.prepareStatement(sql);
            stmt.setInt(1,userid);
            stmt.setString(2,currency);
            stmt.setString(3, type.toString());
            ResultSet rs = stmt.executeQuery();
            int count = rs.getInt(1);
            if(count >= 1) {
                res = false;
            } else {
                res = true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return res;
    }


    public Person isValidUserAuth(String user, String pass) {
        String sql = "SELECT ID, USERNAME FROM USERS WHERE USERNAME = ? AND PASSWORD = ?";
        Person res = null;
        try {
            PreparedStatement stmt  = conn.prepareStatement(sql);
            stmt.setString(1, user);
            stmt.setString(2, pass);
            ResultSet rs = stmt.executeQuery();
            int id = rs.getInt(1);
            if(id == 0) {
                return null;
            }
            res = getPerson(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        return res;
    }

    public List<SavingsAccount> getHighSavingAccounts() {
        String sql = "SELECT ID, USERID, TYPE, AMOUNT, CURRENCY FROM ACCOUNTS WHERE AMOUNT >= ? AND TYPE = ?";
        List<SavingsAccount> list = new ArrayList<>();
        try {
            PreparedStatement stmt  = conn.prepareStatement(sql);
            stmt.setBigDecimal(1, new BigDecimal("5000"));
            stmt.setString(2, AccountType.SAVINGS.toString());
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                SavingsAccount acc = new SavingsAccount(rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getBigDecimal(4));
                list.add(acc);
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        return list;

    }

    public boolean transferMoney(int fromId, int toId, BigDecimal fromAmount, BigDecimal toAmount) {
        String sql = "UPDATE ACCOUNTS SET AMOUNT = " +
                "CASE ID " +
                "WHEN ? THEN ? " +
                "WHEN ? THEN ? " +
                "END ";

        try {
            PreparedStatement stmt  = conn.prepareStatement(sql);
            stmt.setInt(1,fromId);
            stmt.setBigDecimal(2,fromAmount);
            stmt.setInt(3,fromId);
            stmt.setBigDecimal(4,toAmount);
            stmt.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;

    }

    public boolean addStockPosition(int userid, String symbol, int shares, BigDecimal avgCost){

        String sql = "INSERT INTO STOCKS(SYMBOL,USERID,SHARES,AVGCOST) VALUES (?,?,?,?)";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, symbol);
            stmt.setInt(2, userid);
            stmt.setInt(3, shares);
            stmt.setBigDecimal(4, avgCost);
            stmt.execute();
            return true;


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public boolean deleteStockPosition(int userid, String symbol){
        String sql = "DELETE FROM STOCKS WHERE USERID = ? AND SYMBOL = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userid);
            stmt.setString(2, symbol);
            stmt.execute();
            stmt.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;




    }

    public StockPosition getStockPosition(String symbol, int userid){

        StockPosition sp = null;

        try {
            String sql = "SELECT SYMBOL, USERID, SHARES, AVGCOST FROM STOCKS WHERE SYMBOL = ? AND USERID = ?";

            PreparedStatement stmt2 = conn.prepareStatement(sql);
            stmt2.setString(1, symbol);
            stmt2.setInt(2, userid);
            ResultSet rs2 = stmt2.executeQuery();

            sp = new StockPosition(userid, rs2.getString(1), rs2.getInt(3), rs2.getBigDecimal(4));

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        return sp;


    }

    public List<StockPosition> getAllStockPosition(int userid){

        List<StockPosition> list = new ArrayList<>();

        try {
            String sql = "SELECT SYMBOL FROM STOCKS WHERE USERID = ?";

            PreparedStatement stmt2 = conn.prepareStatement(sql);
            stmt2.setInt(1, userid);
            ResultSet rs2 = stmt2.executeQuery();
            while (rs2.next()) {
                StockPosition stockPosition = getStockPosition(rs2.getString(1),userid);
                list.add(stockPosition);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        return list;

    }

    public boolean updateStockPosition(int userid, String symbol, int shares, BigDecimal avgCost){

        String sql = "UPDATE STOCKS SET SHARES = ?, AVGCOST = ? WHERE SYMBOL = ? AND USERID = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, shares);
            stmt.setBigDecimal(2, avgCost);
            stmt.setString(3, symbol);
            stmt.setInt(4, userid);
            stmt.execute();
            stmt.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;




    }


}
