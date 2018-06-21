/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockcharts.earthquake.servlet;
import java.sql.*;
import java.util.*;
import com.stockcharts.commons.net.*;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author murch23
 */
public class Main {
    private static final String EARTHQUAKES_URL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.geojson";
    
    
    
    public static void main(String[] args) throws IOException{
        
       List<Earthquake> quakes = EarthquakeDAO.getEarthquakesFromFeed();
       
       for (Earthquake quake : quakes) {
           System.out.println(quake.toString());
       }
        
    }
    
    private static Earthquake getEarthquakeFromJSONObject(JSONObject jo) {
        
        JSONArray coordinates = jo.getJSONObject("geometry").getJSONArray("coordinates");
        JSONObject properties = jo.getJSONObject("properties");
        
        
        Earthquake quake = new Earthquake()
                .withId(jo.getString("id"))
                .withLatitude(coordinates.getFloat(0))
                .withLongitude(coordinates.getFloat(1))
                .withMagnitude(properties.getFloat("mag"))
                .withPlace(properties.getString("place"))
                .withTime(properties.getLong("time"));
        
        return quake;
    }
    
    private static List<Earthquake> getEarthquakeFromJSONArray(JSONArray ja) {
        List<Earthquake> quakes = new ArrayList<>();
        
        for (int i = 0; i < ja.length(); i++) {
            
        }
        
        return quakes;
    }
}
