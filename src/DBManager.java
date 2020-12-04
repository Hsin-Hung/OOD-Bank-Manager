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
            addDefaultManager();
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
                    "AMOUNT REAL NOT NULL, " +
                    "USERID INTEGER NOT NULL, " +
                    "ACCOUNTID INTEGER NOT NULL, " +
                    "TARGETACCOUNTID INTEGER, " +
                    "TARGETUSERID INTEGER, " +
                    "DATE TEXT NOT NULL, " +
                    "PRIMARY KEY(ID AUTOINCREMENT))";
            stmt.execute(sql);
            sql = "CREATE TABLE IF NOT EXISTS STOCKS(" +
                    "ID INTEGER, " +
                    "NAME TEXT NOT NULL, " +
                    "BUYAMOUNT REAL NOT NULL, " +
                    "SELLAMOUNT REAL NOT NULL, " +
                    "PRIMARY KEY(ID AUTOINCREMENT))";
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

    public BankAccount addAccount(int userid, AccountType type, BigDecimal amount, String currency) {
        String sql = "INSERT INTO ACCOUNTS(USERID,TYPE,AMOUNT,CURRENCY) VALUES (?,?,?,?)";
        BankAccount account = null;
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userid);
            stmt.setString(2, type.toString());
            stmt.setBigDecimal(3, amount);
            stmt.setString(4, currency);
            stmt.execute();

            sql = "SELECT ID FROM ACCOUNTS WHERE USERID = ? AND TYPE = ? AND CURRENCY = ? ";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userid);
            stmt.setString(2, type.toString());
            stmt.setString(4, currency);

            ResultSet rs = stmt.executeQuery();
            switch(type.toString()) {
                case "CHECKING":
                    account = new CheckingAccount(rs.getInt(1),userid,currency,amount);
                case "SAVINGS":
                    account = new SavingsAccount(rs.getInt(1),userid,currency,amount);
                case "SECURITIES":
                    account = new SecuritiesAccount(rs.getInt(1),userid,currency,amount);

            }

            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return account;
    }

    public boolean addTransaction(String type, BigDecimal amount, String currency, int userid, int accountId) {
        String sql = "INSERT INTO TRANSACTIONS(DATE,TYPE,AMOUNT,CURRENCY,USERID,ACCOUNTID) VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            String strDate = dateFormat.format(date);
            stmt.setString(1, strDate);
            stmt.setString(2, type);
            stmt.setBigDecimal(3, amount);
            stmt.setString(4, currency);
            stmt.setInt(5, userid);
            stmt.setInt(3, accountId);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean deleteAccount(int accountId) {
        String sql = "DELETE FROM ACCOUNTS WHERE ID = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean updateAmount(int accountId, BigDecimal amount) {
        String sql = "UPDATE ACCOUNTS SET AMOUNT = ?, DATE = ? WHERE ID = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setBigDecimal(1, amount);
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            String strDate = dateFormat.format(date);
            stmt.setString(2, strDate);
            stmt.setInt(3, accountId);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean addLoan(int userid, String type, BigDecimal amount, String currency, String collateral) {
        String sql = "INSERT INTO LOANS(USERID,AMOUNT,CURRENCY,COLLATERAL,DATE) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userid);
            stmt.setBigDecimal(2, amount);
            stmt.setString(3, currency);
            stmt.setString(4, collateral);
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            String strDate = dateFormat.format(date);
            stmt.setString(5, strDate);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean updateLoanAmount(int id, BigDecimal amount) {
        String sql = "UPDATE LOANS SET AMOUNT = ?, DATE = ? WHERE ID = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setBigDecimal(1, amount);
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            String strDate = dateFormat.format(date);
            stmt.setString(2, strDate);
            stmt.setInt(3, id);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean deleteLoan(int id) {
        String sql = "DELETE FROM ACCOUNTS WHERE ID = ?";
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
                p = new Customer(id,name,user,password,loans,accounts,transactions);
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

    public List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT ID, USER FROM USERS WHERE ROLE = ?";

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
                case "SAVINGS":
                    account = new SavingsAccount(
                            rs2.getInt(1),
                            rs2.getInt(2),
                            rs2.getString(4),
                            new BigDecimal(rs2.getString(5)));
                case "SECURITIES":
                    account = new SecuritiesAccount(
                            rs2.getInt(1),
                            rs2.getInt(2),
                            rs2.getString(4),
                            new BigDecimal(rs2.getString(5)));
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
            String sql = "SELECT ID, DATE, TYPE ,AMOUNT, USERID, ACCOUNTID, TARGETACCOUNTID, TARGETUSERID FROM TRANSACTIONS WHERE ID = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            String sDate = rs.getString(2);
            SimpleDateFormat formatter=new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
            Date date = formatter.parse(sDate);
            t = new Transaction(rs.getInt(1),
                    date,
                    rs.getString(3),
                    new BigDecimal(rs.getInt(4)),
                    rs.getInt(5),
                    rs.getInt(6),
                    rs.getInt(7),
                    rs.getInt(8));

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


}
