import java.sql.*;

public class DatabaseManager {
    // Define database connection parameters
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/REGISTER?useSSL=false";

    // Define database credentials
    static final String USER = "root";
    static final String PASS = "@Krsna18";

    // Define SQL queries
    static final String INSERT_QUERY = "INSERT INTO REGISTER_INFO (Customer_Name, Customer_Email_ID, Customer_Password) VALUES (?, ?, ?)";

    // Method to handle registration
    public static void handleRegistration(String name, String email, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Register JDBC driver
            Class.forName(JDBC_DRIVER);

            // Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // Create a prepared statement
            stmt = conn.prepareStatement(INSERT_QUERY);

            // Set parameters
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);

            // Execute the query
            stmt.executeUpdate();
            System.out.println("Registration successful!");

        } catch (SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            // Finally block used to close resources
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            } // Nothing we can do
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            } // End finally try
        } // End try
    }
}
