import java.math.BigDecimal;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
            String file = this.getClass().getResource("").getPath();
            file = file.substring(1,file.length()-4) + "src/db/";
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
    public void createTables() {

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
                    "DATE TEXT NOT NULL, " +
                    "TYPE TEXT NOT NULL, " +
                    "AMOUNT REAL NOT NULL, " +
                    "USERID INTEGER NOT NULL, " +
                    "ACCOUNTID INTEGER NOT NULL, " +
                    "TARGETACCOUNTID INTEGER, " +
                    "TARGETUSERID INTEGER, " +
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
        }

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

    public void addUser(String name, String user, String pass, String role) {
        String sql = "INSERT INTO USERS(NAME,USERNAME,PASSWORD,ROLE) VALUES (?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, user);
            stmt.setString(3, pass);
            stmt.setString(4,role);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addAccount(int userid, String type, BigDecimal amount, String currency) {
        String sql = "INSERT INTO ACCOUNTS(USERID,TYPE,AMOUNT,CURRENCY) VALUES (?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userid);
            stmt.setString(2, type);
            stmt.setBigDecimal(3, amount);
            stmt.setString(4,currency);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addTransaction(String type, BigDecimal amount, String currency, int userid, int accountId) {
        String sql = "INSERT INTO TRANSACTIONS(DATE,TYPE,AMOUNT,CURRENCY,USERID,ACCOUNTID) VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, String.valueOf(java.time.LocalDate.now()));
            stmt.setString(2, type);
            stmt.setBigDecimal(3, amount);
            stmt.setString(4,currency);
            stmt.setInt(5, userid);
            stmt.setInt(3, accountId);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteAccount(int accountId) {
        String sql = "DELETE FROM ACCOUNTS WHERE ID = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateAmount(int accountId, BigDecimal amount) {
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
        }
    }

    public void addLoan(int userid, String type, BigDecimal amount, String currency,String collateral) {
        String sql = "INSERT INTO LOANS(USERID,AMOUNT,CURRENCY,COLLATERAL,DATE) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userid);
            stmt.setBigDecimal(2, amount);
            stmt.setString(3,currency);
            stmt.setString(4,collateral);
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            String strDate = dateFormat.format(date);
            stmt.setString(5, strDate);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateLoanAmount(int id, BigDecimal amount) {
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
        }
    }


    public void deleteLoan(int id ) {
        String sql = "DELETE FROM ACCOUNTS WHERE ID = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public Person getPerson(String username) {
        //TODO: INCOMPLETE
        String sql = "SELECT ROLES FROM USERS WHERE USERNAME = ?";
        Person p;
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            String role = rs.getString(1);
            if(role.equals("CUSTOMER")) {
                p = new Customer();
            } else if (role.equals("MANAGER")) {
               p = new BankManager();
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return p;
    }

}
