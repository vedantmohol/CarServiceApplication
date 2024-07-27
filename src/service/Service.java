package service;
import car.Car;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Service {
    Car car=new Car();
    public HashMap<String,Integer> service=new HashMap<>();
    public HashMap<String,HashMap<String,Integer>> carNameWithService=new HashMap<>();
    {
        HashMap<String,Integer> suvService=new HashMap<>();
        suvService.put("BS01",700);
        suvService.put("GS01",900);
        suvService.put("TS01",600);
        suvService.put("BF01",200);
        suvService.put("GF01",2000);

        HashMap<String,Integer> sedanService=new HashMap<>();
        sedanService.put("BS01",500);
        sedanService.put("GS01",1000);
        sedanService.put("TS01",800);
        sedanService.put("BF01",400);
        sedanService.put("GF01",2500);

        HashMap<String,Integer> xuvService=new HashMap<>();
        xuvService.put("BS01",400);
        xuvService.put("GS01",950);
        xuvService.put("TS01",700);
        xuvService.put("BF01",300);
        xuvService.put("GF01",2200);

        HashMap<String,Integer> electricService=new HashMap<>();
        electricService.put("BS01",400);
        electricService.put("GS01",800);
        electricService.put("TS01",700);
        electricService.put("BF01",500);
        electricService.put("GF01",2300);

        carNameWithService.put(car.getCarList().get(0),suvService);
        carNameWithService.put(car.getCarList().get(1),sedanService);
        carNameWithService.put(car.getCarList().get(2),xuvService);
        carNameWithService.put(car.getCarList().get(4),electricService);
    }

    public void loadData() throws SQLException, ClassNotFoundException {
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

            // Fetch data from the database
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM admin_access");

            // Populate the carNameWithService map
            while (rs.next()) {
                String carType = rs.getString("car_type").toLowerCase();
                String serviceCode = rs.getString("service_code").toUpperCase();
                int servicePrice = rs.getInt("service_price");

                // Check if the car type is already in the map
                if (!carNameWithService.containsKey(carType)) {
                    carNameWithService.put(carType, new HashMap<>());
                }

                // Add service code and price to the map
                carNameWithService.get(carType).put(serviceCode, servicePrice);
            }
        } catch (SQLException | ClassNotFoundException e) {
            // Handle exceptions
            e.printStackTrace();
            throw e; // Rethrow the exception
        } finally {
            // Close the database connection and resources
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    // Method to get the service price for a given car type and service code
    public Integer getServicePrice(String carType, String serviceCode) {
        carType = carType.toLowerCase();
        serviceCode = serviceCode.toUpperCase();
        if (carNameWithService.containsKey(carType)) {
            Map<String, Integer> serviceMap = carNameWithService.get(carType);
            if (serviceMap.containsKey(serviceCode)) {
                return serviceMap.get(serviceCode);
            }
        }
        return null;
    }
    }
