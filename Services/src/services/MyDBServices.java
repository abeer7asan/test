package services;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;

public class MyDBServices {
    /**
     * Auther: Abeer Mousa @09-01-2016
     * get All Students who took the same Courses 
     * and the same learning objects as the current student
     */
    public MyDBServices() {
        super();
    }
    
    public void getMatchHappyStudents(Connection con,String StudentId, double mark){
        Statement stmt = null;
            String query = "select * from student_Learning_outcomes a\n" + 
            "where a.student_id = "+StudentId;
            try {
                stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    String coffeeName = rs.getString("COF_NAME");
                    int supplierID = rs.getInt("SUP_ID");
                    float price = rs.getFloat("PRICE");
                    int sales = rs.getInt("SALES");
                    int total = rs.getInt("TOTAL");
                    System.out.println(coffeeName + "\t" + supplierID +
                                       "\t" + price + "\t" + sales +
                                       "\t" + total);
                }
         
            } catch (SQLException e) {
        } finally {
                if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
            }
        }
    }

