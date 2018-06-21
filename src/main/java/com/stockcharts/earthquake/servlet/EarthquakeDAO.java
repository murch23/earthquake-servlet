/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockcharts.earthquake.servlet;

import com.stockcharts.commons.net.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author murch23
 */
public class EarthquakeDAO {
    
    private final static Logger logger = Logger.getLogger(EarthquakeDAO.class.getName());
    
    /*public static List<Earthquake> getEarthquakeList() throws SQLException {
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
    }*/
    
    public static List<Earthquake> getEarthquakesFromFeed() throws IOException {
        List<Earthquake> earthquakes = new ArrayList<>();
        
        RestResponse response = new RestRequest(EarthquakesServlet.EARTHQUAKES_URL).doGet();
        
        JSONObject jo = new JSONObject(response.getBody());
        
        JSONArray ja = jo.getJSONArray("features");
        
        for (int i = 0; i < ja.length(); i++) {
            JSONObject earthquake = ja.getJSONObject(i);
            
            earthquakes.add(getEarthquakeFromJSONObject(earthquake));
        }
        
        return earthquakes;
    }
    
    private static Earthquake getEarthquakeFromJSONObject(JSONObject jo) {
        
        JSONArray coordinates = jo.getJSONObject("geometry").getJSONArray("coordinates");
        JSONObject properties = jo.getJSONObject("properties");
        
        
        Earthquake quake = new Earthquake()
                .withId(jo.getString("id"))
                .withLatitude(coordinates.getFloat(1))
                .withLongitude(coordinates.getFloat(0))
                .withMagnitude(properties.getFloat("mag"))
                .withPlace(properties.getString("place"))
                .withTime(properties.getLong("time"));
        
        return quake;
    }
    
    /*private static Earthquake getEarthquakeFromJSONObject(JSONObject jo) {
        
        JSONObject earthquake = 
        
        
        Earthquake quake = new Earthquake()
                .withId(jo.getString("id"))
                .withLatitude(ja.getFloat(0))
                .withLongitude(ja.getFloat(1))
                .withMagnitude(earthquake.getFloat("mag"))
                .withPlace(earthquake.getString("place"))
                .withTime(earthquake.getLong("time"));
        
        return quake;
    }*/
}
