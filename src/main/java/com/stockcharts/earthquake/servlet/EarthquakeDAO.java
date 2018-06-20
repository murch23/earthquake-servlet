/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockcharts.earthquake.servlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author murch23
 */
public class EarthquakeDAO {
    
    private final static Logger logger = Logger.getLogger(EarthquakesServlet.class.getName());
    
    public static List<Earthquake> getEarthquakeList() throws SQLException {
        logger.debug("getEarthquakeList() called");
        
        List<Earthquake> earthquakeList = new ArrayList<>();
        
        String query = "SELECT * FROM InternDB.Earthquakes";
        try (Connection conn = DriverManager.getConnection(EarthquakesServlet.DATABASE_URL);
                PreparedStatement stmt = conn.prepareStatement(query)) {
            
            logger.debug("Established connection to Database");
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                earthquakeList.add(new Earthquake()
                        .withId(rs.getString("id"))
                        .withLatitude(rs.getFloat("latitude"))
                        .withLongitude(rs.getFloat("longitude"))
                        .withMagnitude(rs.getFloat("magnitude"))
                        .withPlace(rs.getString("place"))
                        .withTime(rs.getLong("time")));
                
                logger.debug("Added new Earthquake to the list");
                
            }
            
        }
        
        return earthquakeList;
    }
}
