/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emailplanner;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.EmailPlannerModel;
import mylibrary.MyMethod;

/**
 *
 * @author Amaan
 */
public class MainScreenController extends TimerTask implements Initializable {
    
    @FXML private Button submit;
    @FXML private Label title;
    @FXML private PasswordField password;
    @FXML private TextField email;
    
    
    public String getEmail() { return this.email.getText(); }
    public void setEmail(String s) { this.email.setText(s); }
    
    public String getPassword() { return this.password.getText(); }
    public void setPassword(String s) { this.password.setText(s); }
    
    
    @FXML private void submitButtonPressed(ActionEvent event) throws IOException {

        try {
            
            String query = "update EmailID set Email = '"+email.getText()+"', Password = '"+password.getText()+"', IsLogin = '1'";
            Integer rs = (MyMethod.makeConnectionToDataBase()).executeUpdate(query);
            
            if (rs == 1) {
                
                EmailPlannerModel.email = email.getText();
                EmailPlannerModel.password = password.getText();
                
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("UserScreen.fxml"));
                loader.load();
                Parent root = loader.getRoot();
                Stage userScreenStage = (Stage) submit.getScene().getWindow();
                userScreenStage.setScene(new Scene(root)); 
                
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        UserScreenController userScreenController = loader.getController();
                        userScreenController.setEmailsTable();
                    }
                });
           
            } 
            
            
        } catch (SQLException ex) {
            
            MyMethod.showErrorAlert("DataBase Error", "Error While Signing");
        }
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
         Platform.runLater(new Runnable() {

            @Override
            public void run() {
                
                EmailPlannerModel.setRunningStage((Stage) submit.getScene().getWindow());
            }
        });
        
        
        
        
    }    

    @Override
    public void run() {
     
        this.cancel();
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
             
                fillEmailAndPassField();
            }
        });
       
    }
    
    public void fillEmailAndPassField() {
        
        try {
            
            String query = "select * from EmailID";
            ResultSet rs = (MyMethod.makeConnectionToDataBase()).executeQuery(query);
            
            while (rs.next()) {
                
                email.setText(rs.getString(1));
                EmailPlannerModel.email = email.getText();
                
                password.setText(rs.getString(2));
                EmailPlannerModel.password = password.getText();
            }
            
        } catch (SQLException ex) {
            MyMethod.showErrorAlert("DataBase Error", "Error while retreiving EmailID and Password");
        }
    }
    
}
