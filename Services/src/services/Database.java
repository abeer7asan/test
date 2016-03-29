package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    protected Connection con;
    protected String dbName;


    /*
     * Default constactur :
     * Connect to the recommender Database
     */
    public Database() {
        super();
        System.out.println("Database open connection returns: "+openConnection());
    }

    /**
     * Written by Abeer Mousa 09-01-2015
     * Open a connection to Oracle database
     * If database type, name or passward is changed, it must be changed from here
     */

    public boolean openConnection() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("No Oracle driver");
            e.printStackTrace();
            return false;
        }
        System.out.println("Oracle database is regestered");
        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "RECOMENDATION", "recomendation");
        } catch (SQLException e) {
            System.out.println("Connection Faild");
            e.printStackTrace();
            return false;
        }
        if (con != null) {
            return true;
        }
        return false;


    }
    
    
    
    public static void main(String[] arg) {
        Database db = new Database();
  
    }
}
