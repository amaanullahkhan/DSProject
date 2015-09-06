/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mylibrary;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;


/**
 *
 * @author Amaan
 */
public class MyMethod {
    
     public static Statement makeConnectionToDataBase() {
         
         Statement stm = null ;
         
       try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionURL = "jdbc:sqlserver://Amaan-PC:50750;databaseName=EmailPlanner;user=sa;password=Kidsnextdoor1;";
            Connection con = DriverManager.getConnection(connectionURL);

            stm = con.createStatement();
          
            System.out.println("connected");
            
            
            
            } catch (SQLException | ClassNotFoundException ex) {
                
                System.out.println("Not Connected To DataBase");
                
        }
       return stm;
    }
     
     
     
    public static Boolean isDuplicate(ResultSet rs, String str) {
         
         try {
             while(rs.next()){
                 
                 if(rs.getString(1).equals(str)) {
                     
                    return true;
                     
                 }
             }
         } catch (SQLException ex) {
             Logger.getLogger(MyMethod.class.getName()).log(Level.SEVERE, null, ex);
         }
         
         return false;
     }
      
    public static void showInformationAlert(String title, String Content){
         
       Alert alert = new Alert(Alert.AlertType.INFORMATION);
       alert.setTitle(title);
       alert.setContentText(Content);
       alert.showAndWait();

     }
     
    public static void showWarningAlert(String title, String Content){
         
       Alert alert = new Alert(Alert.AlertType.WARNING);
       alert.setTitle(title);
       alert.setContentText(Content);
       alert.showAndWait();

     }
      
    public static void showErrorAlert(String title, String Content){
         
       Alert alert = new Alert(Alert.AlertType.ERROR);
       alert.setTitle(title);
       alert.setContentText(Content);
       alert.showAndWait();

     }
    
}
