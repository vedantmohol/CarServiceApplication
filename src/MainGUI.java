import admin.CallAdmin;
import service.Service;
import user.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainGUI extends JFrame {
    private JButton adminButton;
    private JButton userButton;
    private JButton registerButton;
    private JLabel backgroundLabel;
    private List<User> userList;
    private String currentUserName;
    public MainGUI() {
        setTitle("Car Service System");
        setSize(1600, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            Image backgroundImage = ImageIO.read(new File("E:\\DOCUMENTS_2\\OOP_Project\\Garage_System\\images\\bg.png"));
            backgroundLabel = new JLabel(new ImageIcon(backgroundImage));
        } catch (IOException e) {
            e.printStackTrace();
            backgroundLabel = new JLabel();
        }

        userList = new ArrayList<>();

        adminButton = new JButton("Admin");
        userButton = new JButton("User");
        registerButton = new JButton("Register");

        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean loginSuccessful = false;
                do {
                    String adminName = JOptionPane.showInputDialog("Enter Admin Name:");
                    String passcode = JOptionPane.showInputDialog("Enter Passcode:");
                    CallAdmin admin = new CallAdmin();
                    loginSuccessful = admin.login(adminName, passcode);
                    if (!loginSuccessful) {
                        int option = JOptionPane.showOptionDialog(null,
                                "Incorrect Passcode! Try Again?",
                                "Error",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.ERROR_MESSAGE,
                                null,
                                new String[]{"Try Again", "Close"},
                                "Try Again");
                        if (option == JOptionPane.NO_OPTION) {
                            return; // Close the application if "Close" is clicked
                        }
                    }
                } while (!loginSuccessful);
//                 dispose();
            }
        });

        userButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginFrame();
//                dispose(); // Dispose of the main frame
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog("Enter Your Name:");
                // Check if name is empty or cancelled
                if (name == null || name.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please Enter Your Name!", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Return to the main screen
                }

                String email = JOptionPane.showInputDialog("Enter Email ID:");
                // Check if email is empty or cancelled
                if (email == null || email.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please Enter Your Email ID!", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Return to the main screen
                }

                String password = JOptionPane.showInputDialog("Enter Password:");
                // Check if password is empty or cancelled
                if (password == null || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please Enter Your Password!", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Return to the main screen
                }

                // Call handleRegistration method from DatabaseManager
                DatabaseManager.handleRegistration(name, email, password);
                handleRegistration();
            }
        });

        backgroundLabel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 100));
        backgroundLabel.add(adminButton);
        backgroundLabel.add(userButton);
        backgroundLabel.add(registerButton);
        add(backgroundLabel);

        adminButton.setBackground(Color.BLACK);
        adminButton.setForeground(Color.red);
        adminButton.setFocusable(false);
        adminButton.setPreferredSize(new Dimension(200,50));
        adminButton.setFont(new Font("Times New Roman", Font.BOLD, 25));

        userButton.setBackground(Color.BLACK);
        userButton.setForeground(Color.red);
        userButton.setFocusable(false);
        userButton.setPreferredSize(new Dimension(200,50));
        userButton.setFont(new Font("Times New Roman", Font.BOLD, 25));

        registerButton.setBackground(Color.BLACK);
        registerButton.setForeground(Color.red);
        registerButton.setFocusable(false);
        registerButton.setPreferredSize(new Dimension(200,50));
        registerButton.setFont(new Font("Times New Roman", Font.BOLD, 25));

        setVisible(true);
    }
    private void handleRegistration() {
        String name;
        //this.currentUserName = name; // Set the current user's name
        // Show registration success message
        JOptionPane.showMessageDialog(null, "Registration Successful!");
    }

    private boolean isValidEmail(String email) {
        // Simple email format validation
        return email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainGUI();
            }
        });
    }
}

class LoginFrame extends JFrame {
    static JTextField nameField;
    JPasswordField passwordField;
    private static String currentUserName;

    LoginFrame() {
        setTitle("Login");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2));

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel()); // placeholder
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String password = new String(passwordField.getPassword());
                boolean authenticated = authenticate(name, password);
                if (authenticated) {
                    dispose();// Close login frame
                    serviceFrame(currentUserName);
                } else {
                    JOptionPane.showMessageDialog(null, "Please check your password again!", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(panel);
        setVisible(true);
    }

    private boolean authenticate(String name, String password) {
        // Perform database authentication here
        String url = "jdbc:mysql://localhost:3306/REGISTER";
        String username = "root";
        String dbPassword = "@Krsna18";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(url, username, dbPassword);
            String query = "SELECT * FROM Register_Info WHERE Customer_Name = ? AND Customer_Password = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            this.currentUserName = name;
            return resultSet.next(); // If there is a result, authentication is successful
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void openUserFrame(String userName) {
        final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://localhost:3306/ADMIN";

        // Define database credentials
        final String USER = "root";
        final String PASS = "@Krsna18";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            // Register JDBC driver
            Class.forName(JDBC_DRIVER);

            // Establish a connection to the database
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // Fetch distinct car types from the database
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT DISTINCT car_type FROM admin_access");

            // Create a list to store car types
            List<String> carTypeList = new ArrayList<>();
            while (rs.next()) {
                // Add each distinct car type to the list
                carTypeList.add(rs.getString("car_type"));
            }

            JFrame userFrame = new JFrame("User Input");
            userFrame.setSize(600, 400);
            userFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            userFrame.setLocationRelativeTo(null);

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());

            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new GridLayout(3, 2, 10, 10)); // 3 rows, 2 columns, with gaps of 10px
            inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

            JLabel carLabel = new JLabel("Select car:");
            carLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
            JComboBox<String> carComboBox = new JComboBox<>();
            for (String carType : carTypeList) {
                carComboBox.addItem(carType);
            }
            carComboBox.setPreferredSize(new Dimension(150, 30)); // Reduce the size of car combobox

            JLabel serviceLabel = new JLabel("Enter service codes:");
            serviceLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
            JTextField serviceTextField = new JTextField();
            serviceTextField.setFont(new Font("Times New Roman", Font.PLAIN, 16));

            JButton submitButton = new JButton("Submit");
            submitButton.setFocusable(false);
            submitButton.setForeground(Color.white);
            submitButton.setBackground(Color.red);
            submitButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
            submitButton.setPreferredSize(new Dimension(150, 40));
            submitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String selectedServices = serviceTextField.getText().trim();

                    if (selectedServices.isEmpty()) {
                        // Show an alert if no service code is entered
                        JOptionPane.showMessageDialog(null, "Please Enter the Service Code");
                        return; // Stop further execution
                    }

                    String selectedCar = (String) carComboBox.getSelectedItem();
                    String userSelectedCar = selectedCar.toLowerCase();

                    Service carService = new Service();
                    try {
                        carService.loadData(); // Load service data from the database
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error loading service data",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    int amount = 0;
                    String[] userServices = selectedServices.split("\\s+");
                    for (String userService : userServices) {
                        Integer price = carService.carNameWithService.get(userSelectedCar).get(userService.toUpperCase());
                        if (price != null) {
                            amount += price;
                        } else {
                            JOptionPane.showMessageDialog(null, "Wrong Service Code. Please Enter Again",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            return; // Stop further execution
                        }
                    }
                    storeServiceData(userName, selectedServices, selectedCar, amount);

                    JButton amountButton = new JButton("Total Amount: " + amount);
                    amountButton.setFocusable(false);
                    amountButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
                    amountButton.setPreferredSize(new Dimension(300, 50));
                    amountButton.setBackground(Color.BLACK);
                    amountButton.setForeground(Color.WHITE);

                    JPanel totalPanel = new JPanel();
                    totalPanel.setLayout(new BorderLayout());
                    totalPanel.add(amountButton, BorderLayout.NORTH);

                    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
                    JButton okButton = new JButton("OK");
                    okButton.setFocusable(false);
                    okButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
                    okButton.setPreferredSize(new Dimension(100, 40));
                    okButton.setBackground(Color.BLACK);
                    okButton.setForeground(Color.WHITE);
                    int finalAmount = amount;
                    okButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (finalAmount > 2000) {
                                JOptionPane.showMessageDialog(null, "Congratulations you have earned a 10% discount on your Service!",
                                        "Info", JOptionPane.PLAIN_MESSAGE);
                                userFrame.dispose();
                            } else {
                                String message;
                                if (finalAmount <= 1000) {
                                    message = "Your car will be serviced in 2-3 working days.";
                                } else if (finalAmount <= 2000) {
                                    message = "Your car will be serviced in 4-5 working days.";
                                } else {
                                    message = "Your car will be serviced in 8-9 working days.";
                                }
                                JOptionPane.showMessageDialog(null, message, "Info", JOptionPane.PLAIN_MESSAGE);
                                userFrame.dispose();
                            }

                        }
                    });
                    buttonPanel.add(okButton);
                    totalPanel.add(buttonPanel, BorderLayout.CENTER);

                    mainPanel.removeAll();
                    mainPanel.add(totalPanel, BorderLayout.CENTER);
                    mainPanel.revalidate();

                    userFrame.revalidate();
                }
            });

            inputPanel.add(carLabel);
            inputPanel.add(carComboBox);
            inputPanel.add(serviceLabel);
            inputPanel.add(serviceTextField);

            mainPanel.add(inputPanel, BorderLayout.CENTER);
            mainPanel.add(submitButton, BorderLayout.SOUTH);

            userFrame.add(mainPanel);
            userFrame.setVisible(true);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // Close the database connection and resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void storeServiceData(String customerName, String serviceCode, String carType, int servicePrice) {
        String url = "jdbc:mysql://localhost:3306/service";
        String username = "root";
        String dbPassword = "@Krsna18";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection(url, username, dbPassword);
            String query = "INSERT INTO service_info (customer_name, service_code, car_type, service_price) VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, customerName);
            preparedStatement.setString(2, serviceCode);
            preparedStatement.setString(3, carType);
            preparedStatement.setInt(4, servicePrice);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void serviceFrame(String userName) {
        final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://localhost:3306/ADMIN";
        //LoginFrame.openUserFrame(LoginFrame.currentUserName);
        // Define database credentials
        final String USER = "root";
        final String PASS = "@Krsna18";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            // Register JDBC driver
            Class.forName(JDBC_DRIVER);

            // Establish a connection to the database
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // Fetch data from the database
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM admin_access");

            // Create the initial frame
            JFrame initialFrame = new JFrame("Services");
            initialFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            initialFrame.setSize(800, 600);
            initialFrame.setLayout(new BorderLayout());
            initialFrame.setLocationRelativeTo(null);

            JPanel initialPanel = new JPanel(new GridLayout(0, 2, 10, 10)); // Dynamic rows

            // Create a map to store JTextAreas for each car type
            Map<String, JTextArea> carTypeTextAreaMap = new HashMap<>();

            // Dynamically populate panels with fetched data
            while (rs.next()) {
                String carType = rs.getString("car_type");
                String serviceCode = rs.getString("service_code");
                JTextArea textArea = carTypeTextAreaMap.get(carType);

                // If JTextArea for the car type doesn't exist, create a new one
                if (textArea == null) {
                    JPanel panel = new JPanel(new BorderLayout());
                    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                    JLabel carTypeLabel = new JLabel(carType, SwingConstants.CENTER);
                    carTypeLabel.setFont(new Font("Arial", Font.BOLD, 16));
                    panel.add(carTypeLabel, BorderLayout.NORTH);

                    textArea = new JTextArea();
                    textArea.setEditable(false);
                    textArea.setFont(new Font("Arial", Font.PLAIN, 20));

                    JScrollPane scrollPane = new JScrollPane(textArea);
                    panel.add(scrollPane, BorderLayout.CENTER);

                    initialPanel.add(panel);

                    // Add the JTextArea to the map
                    carTypeTextAreaMap.put(carType, textArea);
                }

                // Append service code below the car type
                int servicePrice = rs.getInt("service_price");
                textArea.append(" " + serviceCode + " : $" + servicePrice + "\n");
            }

            // Add the initial panel to the frame
            initialFrame.add(initialPanel, BorderLayout.CENTER);

            // Create and add a proceed button
            JButton proceedButton = new JButton("Proceed");
            proceedButton.setBackground(Color.BLACK);
            proceedButton.setForeground(Color.red);
            proceedButton.setFocusable(false);
            proceedButton.setPreferredSize(new Dimension(1200, 50));
            proceedButton.setFont(new Font("Times New Roman", Font.BOLD, 25));
            proceedButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Close the initial frame
                    initialFrame.dispose();

                    // Proceed to the user frame
                    LoginFrame.openUserFrame(LoginFrame.currentUserName);
                }
            });
            initialFrame.add(proceedButton, BorderLayout.SOUTH);

            // Display the frame
            initialFrame.setVisible(true);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}