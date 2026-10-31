import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** JDBC connection utility. Credentials are provided via environment variables. */
public class DBConnection {
    public static Connection getConnection() throws SQLException {
        String url = System.getenv("AIRPORT_DB_URL");
        if (url == null || url.isBlank()) {
            url = "jdbc:mysql://localhost:3306/oop";
        }
        String user = System.getenv("AIRPORT_DB_USER");
        String password = System.getenv("AIRPORT_DB_PASSWORD");
        if (user == null || user.isBlank() || password == null) {
            throw new SQLException("Set AIRPORT_DB_USER and AIRPORT_DB_PASSWORD before running the app.");
        }
        return DriverManager.getConnection(url, user, password);
    }
}
