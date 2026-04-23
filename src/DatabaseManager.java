import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static String url = "jdbc:mysql://localhost:3306/qa_test_db";
    private static String username = "root";
    private static String password = "mypassword123";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
