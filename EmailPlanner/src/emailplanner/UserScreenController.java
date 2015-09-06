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
import java.sql.Statement;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.EmailPlannerModel;
import mylibrary.MyEmail;
import mylibrary.MyMethod;

/**
 * FXML Controller class
 *
 * @author Amaan
 */
public class UserScreenController extends TimerTask implements Initializable {
    @FXML
    private Button compose;
    @FXML
    private TableView<MyEmail> table;
    private TableViewSelectionModel<MyEmail> tsm;
    @FXML
    private Button remove;
    @FXML
    private Button start;
    @FXML
    private Button stop;
    
    Statement stm = null;
    ResultSet rs = null;
    @FXML
    private MenuItem signOut;
    @FXML
    private MenuItem close;
    @FXML
    private MenuItem changeEmailPassword;
    @FXML
    private MenuItem about;

    /** 
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {  
        
         Platform.runLater(new Runnable() {

             @Override
             public void run() {
                 
                EmailPlannerModel.setRunningStage((Stage) compose.getScene().getWindow());
                  
             }
         });
    }    

    @FXML
    private void composeButtonPressed(ActionEvent event) throws IOException {
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("NewMail.fxml"));
        loader.load();
   
        Parent root = loader.getRoot();
        
        Stage newMailScreenStage = new Stage();
        newMailScreenStage.setScene(new Scene(root));
        newMailScreenStage.initModality(Modality.APPLICATION_MODAL);
        newMailScreenStage.initOwner(compose.getScene().getWindow());
        newMailScreenStage.setTitle("New Message");
        newMailScreenStage.setMaxHeight(535);
        newMailScreenStage.setMaxWidth(715);
        newMailScreenStage.setMinHeight(535);
        newMailScreenStage.setMinWidth(715);
        newMailScreenStage.show();
        

        
    }

    @FXML
    private void removeBtnPressed(ActionEvent event) {
        

            if (tsm.isEmpty()) {
                MyMethod.showWarningAlert(null,"Please select rows to delete.");
                return;
            }
            
            tsm.getSelectedItem().remove();
            tsm.clearSelection();
            table.refresh();

    }

    @FXML
    private void startButnPressed(ActionEvent event) {
        
        if (tsm.isEmpty()) {
            MyMethod.showWarningAlert(null,"Please select rows to Start.");
            return;
        }
        
        
        tsm.getSelectedItem().start();
        tsm.clearSelection();
        table.refresh();
    }

    @FXML
    private void stopBtnPressed(ActionEvent event) {
        

        if (tsm.isEmpty()) {
            MyMethod.showWarningAlert(null,"Please select rows to stop.");
            return;
        }
        
        tsm.getSelectedItem().stop();
        tsm.clearSelection();
        table.refresh();

    }

    @Override
    public void run() {
        
        this.cancel();
        
        Platform.runLater(new Runnable() {

            @Override
            public void run() { 
                setEmailsTable();
            }
        });
        
       
    }
    
    
    public void setEmailsTable() {
        
        tsm = table.getSelectionModel();
        tsm.setSelectionMode(SelectionMode.SINGLE);
        table.setItems(EmailPlannerModel.getEmails());
        table.getColumns().addAll(EmailPlannerModel.getRecipientColumn(), EmailPlannerModel.getSubjectColumn(),
        EmailPlannerModel.getMessageColumn(), EmailPlannerModel.getAttachmentsColumn(), EmailPlannerModel.getDateColumn(), EmailPlannerModel.getStatusColumn());
    }

    @FXML
    private void signOutButtonPressed(ActionEvent event) {
        
        try {
            
            String query = "update EmailID set IsLogin = '0'";
            Integer changes = (MyMethod.makeConnectionToDataBase()).executeUpdate(query);
            
            
            if (changes == 1) {
                
                EmailPlannerModel.closeAllScheduler();
                
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("MainScreen.fxml"));
                loader.load();

                Parent root = loader.getRoot();
                Stage stage = (Stage) compose.getScene().getWindow();
                stage.setScene(new Scene(root));
                
                MainScreenController mainScreenController = loader.getController();
                
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        mainScreenController.setEmail(EmailPlannerModel.email);
                        mainScreenController.setPassword(EmailPlannerModel.password);
                    }
                });
                
            }
         
            
            
        } catch (IOException ex) {
            Logger.getLogger(UserScreenController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(UserScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    @FXML
    private void closeButtonPressed(ActionEvent event) {
        
        System.exit(0);
    }

    @FXML
    private void changeEmailPassword(ActionEvent event) {
        
        try {
            
            EmailPlannerModel.closeAllScheduler();
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("MainScreen.fxml"));
            loader.load();
            
            Parent root = loader.getRoot();
            Stage stage = (Stage) compose.getScene().getWindow();
            stage.setScene(new Scene(root));
            
        } catch (IOException ex) {
            Logger.getLogger(UserScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @FXML
    private void aboutButtonPressed(ActionEvent event) {
    }
    
    
}
