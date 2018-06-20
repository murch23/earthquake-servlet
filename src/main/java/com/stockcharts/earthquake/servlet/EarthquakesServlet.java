/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockcharts.earthquake.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
//import org.json.JSONObject;

public class EarthquakesServlet extends HttpServlet {

    private static final String DB_DRIVER_CLASS = "org.mariadb.jdbc.Driver";
    public static final String DATABASE_URL = "jdbc:mariadb:aurora://scc-intern-db.couiu6erjuou.us-east-1.rds.amazonaws.com:3306/InternDB?user=intern&password=stockcharts2018&trustServerCertificate=true&connectTimeout=5000";
    
    private final Logger logger = Logger.getLogger(EarthquakesServlet.class.getName());
    
    @Override
    public void init(ServletConfig config)throws ServletException {
        super.init(config);
        
        logger.warn("==================================================");
        logger.warn("           sample-servlet : init()");
        logger.warn("==================================================");
        
        logger.warn("Loading the driver class...");
        
        try {
            
            //loads the driver class
            Class.forName(DB_DRIVER_CLASS);
            
        } catch (ClassNotFoundException e) {
            
            logger.fatal("Driver class not found. " + e);
            throw new UnavailableException("Driver not found");
            
        }
        
        logger.warn("...Driver class loaded");
        
        logger.warn("==================================================");
        logger.warn("       sample-servlet : init() - COMPLETE");
        logger.warn("==================================================");
    }
    
    @Override
    public void destroy() {
        logger.warn("<<<<<<<<<<<<<<<<<<<< ######## >>>>>>>>>>>>>>>>>>>>");
        logger.warn("           sample-servlet - destroy()");
        logger.warn("<<<<<<<<<<<<<<<<<<<< ######## >>>>>>>>>>>>>>>>>>>>");
        
        
        
        logger.warn("<<<<<<<<<<<<<<<<<<<< ######## >>>>>>>>>>>>>>>>>>>>");
        logger.warn("       sample-servlet - destroy() - COMPLETE");
        logger.warn("<<<<<<<<<<<<<<<<<<<< ######## >>>>>>>>>>>>>>>>>>>>");
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Earthquake> quakes = new ArrayList<>();
        
        try{
            quakes = EarthquakeDAO.getEarthquakeList();
        } catch (SQLException e) {
            logger.fatal("Error trying to add earthquakes to list from database in DoGet()", e);
        }
        
        String sort = request.getParameter("sort");
        
        if(sort == null){
            sort = "";
        }
        
        logger.debug("Recieved the sort parameter");
        
        switch (sort) {
            case "magnitude":
                Collections.sort(quakes, Earthquake.MAGNITUDE);
                break;
            case "latitude":
                Collections.sort(quakes, Earthquake.LATITUDE);
                break;
            case "longitude":
                Collections.sort(quakes, Earthquake.LONGITUDE);
                break;
            case "time":
                Collections.sort(quakes, Earthquake.TIME);
            default:
                //do nothing
                break;
        }
        
        JSONArray ja = new JSONArray(quakes);
        
        try (PrintWriter out = response.getWriter()) {
            
            response.setHeader("Connection", "close");
            response.setContentType("application/json");
            
            out.print(ja.toString());
            out.flush();
            
        } catch (IOException e) {
            logger.error("Error writing response to client. " , e);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }
}
