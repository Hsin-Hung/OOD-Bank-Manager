import java.sql.*;

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

    public void addAccount(String userid, String type, double amount, )

}
