import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {

    private Connection conn = null;

    public DBManager() {
        try {
            // db parameters
            String url = "jdbc:sqlite:D:/sqlite/db/randreas.db";
            // create a connection to the database
            if(conn == null) {
                conn = DriverManager.getConnection(url);
                System.out.println("Connection to SQLite has been established.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createTables() {

        Statement stmt;
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE \"USERS\" (\n" +
                    "\t\"ID\"\tINTEGER NOT NULL UNIQUE,\n" +
                    "\t\"NAME\"\tTEXT NOT NULL,\n" +
                    "\t\"USERNAME\"\tTEXT NOT NULL UNIQUE,\n" +
                    "\t\"PASSWORD\"\tTEXT NOT NULL,\n" +
                    "\t\"ROLE\"\tTEXT NOT NULL,\n" +
                    "\tPRIMARY KEY(\"ID\" AUTOINCREMENT)\n" +
                    ")";
            stmt.execute(sql);
            sql = "CREATE TABLE IF NOT EXISTS TRANSACTIONS();";
            stmt.execute(sql);
            sql = "CREATE TABLE IF NOT EXISTS STOCKS();";
            stmt.execute(sql);
            sql = "CREATE TABLE IF NOT EXISTS ACCOUNTS();";
            stmt.execute(sql);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

}
