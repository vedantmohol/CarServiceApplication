package admin;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
// import java.util.HashMap;

public class Admin {
    public static void updateServiceInDatabase(String carType, String serviceCode, int servicePrice) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Admin", "root", "@Krsna18")) {
            String updateQuery = "INSERT INTO admin_access (Car_type, Service_Code, Service_Price) VALUES (?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE Service_Price = VALUES(Service_Price)";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, carType);
            preparedStatement.setString(2, serviceCode);
            preparedStatement.setInt(3, servicePrice);
            preparedStatement.executeUpdate();
            System.out.println("Service updated successfully.");
        } catch (SQLException e) {
            throw new RuntimeException("Error updating service in the database: " + e.getMessage(), e);
        }
    }
}

