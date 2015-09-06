/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import emailplanner.Start;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.swing.ImageIcon;
import mylibrary.MyEmail;
import mylibrary.MyMethod;

/**
 *
 * @author Amaan
 */
public class EmailPlannerModel {
    
//    public static EmailPlannerModel sharedInstance = new EmailPlannerModel();
    
    public static String email = "";
    public static String password = "";
    
    public static TrayIcon trayIcon ;
    public static Stage runningStage ;
    
    public static ObservableList<MyEmail> emails = FXCollections.observableArrayList(); 
    public static ObservableList<Timer> scheduler = FXCollections.observableArrayList();
 
  
    private EmailPlannerModel() {
        
    }
    
    public static void setRunningStage(Stage stage) {
        
        runningStage = stage;
    }
    
    public static void setTrayIcon() {
        
        
        if (SystemTray.isSupported()) {
            
            if (trayIcon == null) {
                
                try {

                    EmailPlannerModel.trayIcon = new TrayIcon(EmailPlannerModel.createImage("images/MailIcon.png", "tray icon"));
                    SystemTray tray = SystemTray.getSystemTray();
                    PopupMenu popUp = new PopupMenu();

                    MenuItem exitItem = new MenuItem("Exit");
                    popUp.add(exitItem);
                    EmailPlannerModel.trayIcon.setPopupMenu(popUp);
                    EmailPlannerModel.trayIcon.setImageAutoSize(true);
                    EmailPlannerModel.trayIcon.setToolTip("Email Planner");

                    tray.add(EmailPlannerModel.trayIcon);
                    
                    trayIcon.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent ae) {
                           
                            if (runningStage != null) {
                                
                                
                                Platform.runLater(new Runnable() {

                                    @Override
                                    public void run() {
                                     runningStage.show();
                                     runningStage.setIconified(false);
                                   }
                                });
                                
                                
                            }
                        }
                    });
                    
                    exitItem.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent ae) {

                            System.exit(0);

                        }
                    });
                } catch (AWTException ex) {
                    Logger.getLogger(EmailPlannerModel.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
           
        }
        
    }
    
    public static Image createImage(String path, String description) {
        URL imageURL = Start.class.getResource(path);
        
        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            
            System.out.println("Image Found");
            return (new ImageIcon(imageURL, description)).getImage();
            
        }
    }
 
    public static ObservableList<MyEmail> getEmails() {
        
        
        emails.clear();
        
        try {
            Statement stm = MyMethod.makeConnectionToDataBase();
            ResultSet rs = stm.executeQuery("select * from Mails");
            
            
            
            while (rs.next()) {
                emails.add(new MyEmail(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),rs.getString(5),rs.getString(6)));
                
            }
            
            
        } catch (SQLException ex) {
            
            MyMethod.showErrorAlert("Data Base Error", "There was an error while connecting to Data Base");
        }
        
            setEmailScheduling();
            return emails;
    }
    
    public static void setEmailScheduling() {
        
        if (!scheduler.isEmpty()) {
            
           closeAllScheduler();
           scheduler.clear();
            
        }
        
        
        for (int i=0; i < emails.size(); i++) {
            
            if (emails.get(i).getStatus().equals("1")) {
                
                Timer timer = new Timer();
                timer.schedule(emails.get(i), emails.get(i).getDateAsDateObject());
                scheduler.add(timer);
            }
            else {
                Timer timer = new Timer();
                scheduler.add(timer);
                
            }
        }
         
    }
    
    
    public static void closeAllScheduler() { 
        
        for (int i = 0; i < scheduler.size(); i++ ) {
            
            scheduler.get(i).cancel();
            System.out.println("All Scheduler canceled");
        }
        
    } 
    

    /* Returns Recipient TableColumn */
    public static TableColumn<MyEmail, String> getRecipientColumn() {
        TableColumn<MyEmail, String> recipientCol = new TableColumn<>("Recipient");
        recipientCol.setCellValueFactory(new PropertyValueFactory<>("recipient"));
        return recipientCol;
    }
    
    /* Returns Subject TableColumn */
    public static TableColumn<MyEmail, String> getSubjectColumn() {
        TableColumn<MyEmail, String> subjectCol = new TableColumn<>("Subject");
        subjectCol.setCellValueFactory(new PropertyValueFactory<>("subject"));
        return subjectCol;
    }
    
    /* Returns Msg TableColumn */
    public static TableColumn<MyEmail, String> getMessageColumn() {
        TableColumn<MyEmail, String> msgCol = new TableColumn<>("Message");
        msgCol.setCellValueFactory(new PropertyValueFactory<>("msg"));
        return msgCol;
    }
    
    
    public static TableColumn<MyEmail, String> getAttachmentsColumn() {
        TableColumn<MyEmail, String> attachmentsCol = new TableColumn<>("Attachments");
        attachmentsCol.setCellValueFactory(new PropertyValueFactory<>("attachments"));
        return attachmentsCol;
    }
     
    
    public static TableColumn<MyEmail, String> getDateColumn() {
        TableColumn<MyEmail, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        return dateCol;
    }
     
    
    public static TableColumn<MyEmail, String> getStatusColumn() {
        TableColumn<MyEmail, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        return statusCol;
    }
    
}
