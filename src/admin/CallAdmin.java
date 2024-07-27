package admin;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import static admin.Admin.updateServiceInDatabase;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
// import java.util.Scanner;
import java.sql.*;

public class CallAdmin {
    Admin admin = new Admin();
    HashMap<String, String> loginCredentials = new HashMap<>();

    {
        loginCredentials.put("abhishek", "123");
        loginCredentials.put("ankit", "1234");
        loginCredentials.put("vedant", "12345");
        loginCredentials.put("moin", "123456");
    }

    public boolean login(String adminName, String adminPassCode) {
        if (loginCredentials.containsKey(adminName)) {
            if (loginCredentials.get(adminName).equals(adminPassCode)) {
                enterInputs(); // Call enterInputs after successful login
                return true;
            } else {
                System.out.println("Please enter valid password " + adminName);
            }
        }
        return false;
    }

    public void enterInputs() {
        JFrame adminInputFrame = new JFrame("Admin Input");
        adminInputFrame.setSize(1000, 500);
        adminInputFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        adminInputFrame.setLayout(new BorderLayout()); // Use BorderLayout for the main frame

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));

        JLabel carTypeLabel = new JLabel("Car Type:",SwingConstants.CENTER);
        JTextField carTypeField = new JTextField();
        carTypeLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        JLabel serviceCodeLabel = new JLabel("Service Code:",SwingConstants.CENTER);
        JTextField serviceCodeField = new JTextField();
        serviceCodeLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        JLabel servicePriceLabel = new JLabel("Service Price:",SwingConstants.CENTER);
        JTextField servicePriceField = new JTextField();
        servicePriceLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));

        inputPanel.add(carTypeLabel);
        inputPanel.add(carTypeField);
        inputPanel.add(serviceCodeLabel);
        inputPanel.add(serviceCodeField);
        inputPanel.add(servicePriceLabel);
        inputPanel.add(servicePriceField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addButton = new JButton("Add Service");
        addButton.setFocusable(false);
        addButton.setForeground(Color.red);
        addButton.setBackground(Color.black);
        addButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
        addButton.setPreferredSize(new Dimension(150, 40));
        buttonPanel.add(addButton);

        JButton checkPendingServicesButton = new JButton("Check Pending Services");
        checkPendingServicesButton.setFocusable(false);
        checkPendingServicesButton.setForeground(Color.red);
        checkPendingServicesButton.setBackground(Color.black);
        checkPendingServicesButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
        checkPendingServicesButton.setPreferredSize(new Dimension(250, 40));
        checkPendingServicesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminInputFrame.dispose();
                 CallAdmin.ServiceInfoFrame();
            }
        });

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(checkPendingServicesButton);

        adminInputFrame.add(inputPanel, BorderLayout.CENTER); // Add input panel to the center
        adminInputFrame.add(buttonPanel, BorderLayout.SOUTH); // Add button panel to the bottom
        adminInputFrame.add(topPanel, BorderLayout.NORTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get input values from text fields
                String carType = carTypeField.getText();
                String serviceCode = serviceCodeField.getText();
                int servicePrice = Integer.parseInt(servicePriceField.getText());

                updateServiceInDatabase(carType, serviceCode, servicePrice);

                carTypeField.setText("");
                serviceCodeField.setText("");
                servicePriceField.setText("");
            }
        });
        adminInputFrame.setLocationRelativeTo(null);
        adminInputFrame.setVisible(true);
    }
    private static JTable serviceTable;
    private static DefaultTableModel tableModel;

    public static void ServiceInfoFrame() {
        JFrame service = new JFrame();
        service.setTitle("Service Info");
        service.setSize(600, 400);
        service.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        service.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Customer ID");
        tableModel.addColumn("Customer Name");
        tableModel.addColumn("Service Code");
        tableModel.addColumn("Car Type");
        tableModel.addColumn("Service Price");

        fetchDataFromDatabase();

        serviceTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(serviceTable);

        panel.add(scrollPane, BorderLayout.CENTER);

        JButton deleteButton = new JButton("Delete Services");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if a row is selected
                int selectedRow = serviceTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a row to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int customerId = (int) serviceTable.getValueAt(selectedRow, 0);

                tableModel.removeRow(selectedRow);

                deleteRowFromDatabase(customerId);
            }
        });
        panel.add(deleteButton, BorderLayout.NORTH);

        JButton closeButton = new JButton("Close");
        closeButton.setForeground(Color.red);
        closeButton.setBackground(Color.black);
        closeButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
        closeButton.setPreferredSize(new Dimension(150, 40));
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                service.dispose();
            }
        });
        panel.add(closeButton, BorderLayout.SOUTH);
        service.add(panel);
        service.setVisible(true);
    }

    private static void deleteRowFromDatabase(int customerId) {
        String url = "jdbc:mysql://localhost:3306/service";
        String username = "root";
        String dbPassword = "@Krsna18";
        try (Connection connection = DriverManager.getConnection(url, username, dbPassword);
             Statement statement = connection.createStatement()) {
            String query = "DELETE FROM service_info WHERE customer_id = " + customerId;
            statement.executeUpdate(query);
            System.out.println("Row deleted successfully.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void fetchDataFromDatabase() {
        String url = "jdbc:mysql://localhost:3306/service";
        String username = "root";
        String dbPassword = "@Krsna18";
        try (Connection connection = DriverManager.getConnection(url, username, dbPassword);
             Statement statement = connection.createStatement()) {
            String query = "SELECT * FROM service_info";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int customerId = resultSet.getInt("customer_id");
                String customerName = resultSet.getString("customer_name");
                String serviceCode = resultSet.getString("service_code");
                String carType = resultSet.getString("car_type");
                int servicePrice = resultSet.getInt("service_price");
                tableModel.addRow(new Object[]{customerId, customerName, serviceCode, carType, servicePrice});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CallAdmin();
            }
        });
    }
}
