/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockcharts.earthquake.servlet;
import java.sql.*;
import java.util.*;

/**
 *
 * @author murch23
 */
public class Main {
    private static final String DB_DRIVER_CLASS = "org.mariadb.jdbc.Driver";
    public static final String DATABASE_URL = "jdbc:mariadb:aurora://scc-intern-db.couiu6erjuou.us-east-1.rds.amazonaws.com:3306/InternDB?user=intern&password=stockcharts2018&trustServerCertificate=true&connectTimeout=5000";
    
    
    public static void main(String[] args){
        try {
            //loads the driver class
            Class.forName(DB_DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            System.out.println("Driver class not found. " + e);
            return;
        }
        
        List<Earthquake> earthquakes = getEarthquakeList();
        
        for (Earthquake quake : earthquakes) {
            System.out.println(quake);
        }
    }
    
    private static List<Earthquake> getEarthquakeList(){
        List<Earthquake> earthquakeList = new ArrayList<>();
        
        String query = "SELECT * FROM InternDB.Earthquakes";
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
                PreparedStatement stmt = conn.prepareStatement(query)) {
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                earthquakeList.add(new Earthquake()
                        .withId(rs.getString("id"))
                        .withLatitude(rs.getFloat("latitude"))
                        .withLongitude(rs.getFloat("longitude"))
                        .withMagnitude(rs.getFloat("magnitude"))
                        .withPlace(rs.getString("place"))
                        .withTime(rs.getLong("time")));
            }
            
        } catch (SQLException e) {
            System.out.println("Error querying database. " + e);
        }
        
        return earthquakeList;
    }
}
