package algorithms.clustering.services;

import algorithms.clustering.patterns.cluster.DoubleArray;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class MyDBServices {
    /**
     * Auther: Abeer Mousa @09-01-2016
     * get All Students who took the same Courses 
     * and the same learning objects as the current student
     */
    public MyDBServices() {
        super();
    }
    
    public DoubleArray getMatchHappyStudents(Connection con,String StudentId, double mark){
        Statement stmt = null;
            String query = "select a.student_id, a.course_id, a.learning_outcomes_id, a.mark \n" + 
            "    from student_Learning_outcomes a,student_Courses b where  a.student_id = b.student_id /n"+
                             "and a.course_id = b.course_id and b.mark >="+mark+"";
            try {
                stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                List<DoubleArray> result ;
                 
                while (rs.next()) {
                   // DoubleArray 
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
            return null;
    }
    
    
    
    public List<DoubleArray> getMarksForTest(Connection con,String StudentId, double mark){
        Statement stmt = null;
        List<DoubleArray> result = new ArrayList<DoubleArray>();
            String query = "SELECT a.c1, a.c2, a.c3, a.c4, a.c5\n" + 
            "  FROM test a";
            try {
                stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                
                 
                while (rs.next()) {
                    
                    double [] data = new double [4];
                    
                        String studentID = rs.getString("c5");
                        data[0] =  rs.getDouble("c1"); 
                        data[1] =  rs.getDouble("c2");
                        data[2] =  rs.getDouble("c3");
                        data[3] =  rs.getDouble("c4");
                   
                    DoubleArray record = new DoubleArray(data);
                    System.out.println("data[0] ="+data[0]);
                    System.out.println("data[3] ="+data[3]);
                    result.add(record);
                    
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
            return result;
    }
    
    
    public void writeClustersToDB(Connection con,String clusterID ,String StudentId, String reffStudent){
        Statement stmt = null;
        String sql = "insert into test_output values ('"+ clusterID+"','"+StudentId+"','"+reffStudent+"')";

        try {
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
        }
       
        finally {
                if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            
            }
        }
          
    }
    
    public static void main(String[] arg){
        Database db = new Database();
        MyDBServices myDB = new MyDBServices();
        myDB.getMarksForTest(db.con,"1", 10);
    }

}