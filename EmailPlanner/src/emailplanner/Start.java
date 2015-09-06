/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emailplanner;

import com.sun.javaws.exceptions.ExitException;
import java.awt.MenuItem;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import model.EmailPlannerModel;

/**
 *
 * @author Amaan
 */
public class Start extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        
//        Parent root = FXMLLoader.load(getClass().getResource("LoadingScreen.fxml"));
       
        EmailPlannerModel.setTrayIcon();
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("LoadingScreen.fxml"));
        loader.load();
        
        Scene scene = new Scene(loader.getRoot());
        stage.setScene(scene);
        stage.setTitle("Email Planner");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
        Platform.setImplicitExit(false);
        
        
        LoadingScreenController loadingScreenController = loader.getController();
        
        Timer timer = new Timer();
        timer.schedule(loadingScreenController, 2000);  
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        super.stop(); //To change body of generated methods, choose Tools | Templates.
        
        EmailPlannerModel.closeAllScheduler();
        System.out.println("Project Closed");
        System.exit(0);
    }
    
    
    
}
