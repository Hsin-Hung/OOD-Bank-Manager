import java.math.BigDecimal;
import java.sql.*;


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
            createTables();


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
                    "PRIMARY KEY(ID AUTOINCREMENT))";
            stmt.execute(sql);
            sql = "CREATE TABLE IF NOT EXISTS STOCKS(" +
                    "ID INTEGER, " +
                    "NAME TEXT NOT NULL, " +
                    "BUYAMOUNT REAL NOT NULL, " +
                    "SELLAMOUNT REAL NOT NULL, " +
                    "PRIMARY KEY(ID AUTOINCREMENT))";
            stmt.execute(sql);
            System.out.println("STOCKS CREATED");
            sql = "CREATE TABLE IF NOT EXISTS ACCOUNTS (" +
                    "ID INTEGER NOT NULL UNIQUE," +
                    "USERID INTEGER NOT NULL," +
                    "TYPE TEXT NOT NULL," +
                    "AMOUNT REAL NOT NULL," +
                    "CURRENCY TEXT NOT NULL," +
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
        String sql = "UPDATE ACCOUNTS SET AMOUNT = ? WHERE ID = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setBigDecimal(1, amount);
            stmt.setInt(2, accountId);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}
