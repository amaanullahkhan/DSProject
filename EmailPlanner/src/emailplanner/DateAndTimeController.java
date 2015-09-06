/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emailplanner;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Int;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.Timer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import model.EmailPlannerModel;
import mylibrary.MyEmail;
import mylibrary.MyMethod;

/**
 * FXML Controller class
 *
 * @author Amaan
 */
public class DateAndTimeController implements Initializable {
    
    @FXML
    private DatePicker date;
    @FXML
    private ComboBox<Integer> hour;
    @FXML
    private ComboBox<Integer> minutes;
    @FXML
    private Button done;
  
 
    
    Statement statement = null;
    NewMailController newMailController = null;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        populateComboBoxes();
        
        // Get NEW MAIL Controller Object
//        Stage dateTimeStage = (Stage) date.getScene().getWindow();
//        Stage newMailControllerStage = (Stage) dateTimeStage.getOwner();
//        NewMailController newMailController = (NewMailController) newMailControllerStage.getScene().getRoot().getUserData();
        
        
        
    }    
    
    public void init(NewMailController newMailController) {
        this.newMailController = newMailController;
    }
    
    @FXML
    private void doneBtnPressed(ActionEvent event) throws SQLException {

        
        if (date.getValue() == null || hour.getValue() == null || minutes.getValue() == null) {
            
            
            MyMethod.showWarningAlert(null, "Plz Select Valid Date Time");
            
        } else {
            
            
            String dateTime = date.getValue() + " " + hour.getValue() + ":" + minutes.getValue() + ":00";
            
            String query = "insert into Mails values ('" + newMailController.getRecipients()+ "','" + newMailController.getSubjectText() + "','" + newMailController.getEditorText() + "','" + newMailController.getAttachmentsPath() + "','" + dateTime + "','1');";

            statement = MyMethod.makeConnectionToDataBase();
            Integer index = statement.executeUpdate(query);

            newMailController.clearAllFieldsAndAttachments();
            Stage dateAndTimeSatge = (Stage) done.getScene().getWindow();
            dateAndTimeSatge.close();

            EmailPlannerModel.getEmails();
            
            
        }
        
      
        
        
    }  
    
    private void populateComboBoxes() {
        
        hour.getItems().addAll(FXCollections.<Integer>observableArrayList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,
                                                                            19,20,21,22,23));
        minutes.getItems().addAll(FXCollections.<Integer>observableArrayList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,
                                                                                15,16,17,18,19,20,21,22,23,24,25,26,
                                                                                27,28,29,30,31,32,33,34,35,36,37,38,39,
                                                                                40,41,42,43,44,45,46,47,48,49,50,51,52,
                                                                                53,54,55,56,57,58,59));
      
        
        
    }

    
    
}
