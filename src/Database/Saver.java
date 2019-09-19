/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import Data.IMDbPage;

/**
 *
 * @author larik
 */
public final class Saver {
    private final String tableName = "pages";
    private Connection c;
    private String hostName;
    private String dbName;
    private String user;
    private String pass;
    
    public Saver(){
        c = null;
    }
    
    public Saver(String hostName, String dbName, String user, String pass){
        this.hostName = hostName;
        this.dbName = dbName;
        this.user = user;
        this.pass = pass;
        try {
            c = DriverManager.getConnection("jdbc:mysql://" + hostName + ":3306/" + dbName + "?useSSL=false", user, pass);
        } catch (SQLException ex) {
            System.out.println("Error connecting to the database.");
        }
    }
    
    public boolean displayTable(){
        try {
            Statement statement = c.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
            while(resultSet.next()){
                System.out.println(resultSet.getString(1));
            }
        } catch (SQLException ex) {
            System.out.println("Could not create statement");
            return false;
        }
        return true;
    }
    
    public boolean savePage(IMDbPage page){
        if(pageExists(page)){
            return false;
        }
        try (PreparedStatement preparedStatement = 
                    c.prepareStatement("INSERT INTO " + tableName + " VALUES(?, ?, ?, ?, ?)");
        ){
            preparedStatement.setString(1, page.getName());
            preparedStatement.setString(2, page.getUrl());
            preparedStatement.setString(3, page.getDirector() == null ? null : page.getDirector().getName());
            preparedStatement.setString(4, page.getType());
            preparedStatement.setDouble(5, page.getRatingValue());
            preparedStatement.execute();
            preparedStatement.close();
            //System.out.println(page);
        } catch (SQLException ex) {
            System.out.println("Could not create statement");
            return false;
        }
        return true;
    }
    
    public boolean pageExists(IMDbPage page){
        try (
            Statement statement = c.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName
                    + " where title = \"" + page.getName() + "\"");
        ){
            if(resultSet.next())
                return true;
        } catch (SQLException ex) {
            System.out.println("Couldn't get proper contact with database");
        }
        return false;
    }
    
    public boolean pageExists(String url){
        try {
            Statement statement = c.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName
                    + " where url = \"" + url + "\"");
            if(resultSet.next())
                return true;
        } catch (SQLException ex) {
            System.out.println("Couldn't get proper contact with database");
        }
        return false;
    }
    
    public boolean close(){
        if(c == null)
            return false;
        try {
            c.close();
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }
    
}
