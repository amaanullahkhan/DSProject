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
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.EmailPlannerModel;
import mylibrary.MyMethod;

/**
 * FXML Controller class
 *
 * @author Amaan
 */
public class LoadingScreenController extends TimerTask implements Initializable {
    @FXML
    private Label title;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private Label progressDetail;

    
    Statement stm = null;
    ResultSet rs = null;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
               
                EmailPlannerModel.setRunningStage((Stage) progressIndicator.getScene().getWindow());

            }
        });
        
    }    
    
    public void init() {
        
         try {
            
            stm = MyMethod.makeConnectionToDataBase();
            rs = stm.executeQuery("Select * from EmailID");
            
            while (rs.next()) {
                
                if (rs.getString(3).equals("1")) {

                    progressDetail.setText("...  Login User Found  ...");
                    
                    EmailPlannerModel.email = rs.getString(1);
                    EmailPlannerModel.password = rs.getString(2);

                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {

                        @Override
                        public void run() {
                            
                            timer.cancel();
                            Platform.runLater(new Runnable() {

                                @Override
                                public void run() {
                                    
                                   initUserScreen();
                                    
                                }
                            });
                            
                        }
                    }, 2000);
   
                }
                
                else {
                    
                     progressDetail.setText("...  No Login User Found  ...");

                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {

                        @Override
                        public void run() {
                            
                            timer.cancel();
                            Platform.runLater(new Runnable() {

                                @Override
                                public void run() {
                                    
                                    initMainScreen();
                                    
                                }
                            });
                            
                        }
                    }, 2000);
                    
                }
                
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(LoadingScreenController.class.getName()).log(Level.SEVERE, null, ex);
        } 
     
    } 

    @Override
    public void run() {
        
        this.cancel();
        
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                init();
            }
        });
        
    }
    
    public void initUserScreen() {
        
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("UserScreen.fxml"));
            loader.load();
            Scene scene = new Scene(loader.getRoot());
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene(scene);

            stage.setTitle("Email Planner");
            stage.setMaxHeight(635);
            stage.setMaxWidth(815);
            stage.setMinHeight(635);
            stage.setMinWidth(815);
            stage.show();
            
            Timer timer = new Timer();
            timer.schedule(loader.getController(), 0);

            ((Stage) progressIndicator.getScene().getWindow()).close();

        } catch (IOException ex) {
            Logger.getLogger(LoadingScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void initMainScreen() {
        
        try {
            
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("MainScreen.fxml"));
            loader.load();
            Scene scene = new Scene(loader.getRoot());
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene(scene);

            stage.setTitle("Email Planner");
            stage.setMaxHeight(635);
            stage.setMaxWidth(815);
            stage.setMinHeight(635);
            stage.setMinWidth(815);
            stage.show();
            
            Timer timer = new Timer();
            timer.schedule(loader.getController(), 0);

            ((Stage) progressIndicator.getScene().getWindow()).close();

        } catch (IOException ex) {
            Logger.getLogger(LoadingScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }       
        
    }
    
}
