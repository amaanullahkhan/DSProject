/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emailplanner;

import java.io.File;
import java.io.IOException;
import static java.lang.ProcessBuilder.Redirect.to;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.xml.bind.JAXBException;
import javax.xml.bind.PropertyException;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.Validator;
import mylibrary.MyEmail;
import mylibrary.MyMethod;
/**
 * FXML Controller class
 *
 * @author Amaan
 */
public class NewMailController implements Initializable {
    @FXML
    private TextField subject;
    
    @FXML
    private TextField to;
    private String recipients = null;
    
    @FXML
    private HTMLEditor editor;
    @FXML
    private Button sendNow;
    
    @FXML
    private Button addAttachement;
    List<File> attachments = null ;
    String attachmentsPath = null;
    FileChooser fileChooser;
    
    @FXML
    private Button sendLater;
    @FXML
    private TextField cc;
    @FXML
    private TextField bcc;
 
    // Getter Setter for Subject Text 
    public void setSubjectText(String s) { this.subject.setText(s); }
    public String getSubjectText() { return this.subject.getText(); }
 
    // Getter Setter for To Text
    public void setToText(String t) { this.to.setText(t); }
    public String getToText() { return this.to.getText(); }
    
    public void setRecipients(String r) { this.recipients = r; }
    public String getRecipients() { setRecipients(); return this.recipients; }

    // Getter Setter for Editor Text
    public void setEditorText(String e) { this.editor.setHtmlText("<html><head></head><body contenteditable=\"true\">"+e+"</body></html>"); }
    public String getEditorText() { return editor.getHtmlText().substring(58, editor.getHtmlText().indexOf("</body>")); }
    
    
    public void setAttachmentsPath(String s) { this.attachmentsPath = s; }
    public String getAttachmentsPath() { setAttachmetsPath(); return this.attachmentsPath; }
    
    
    public void setAttachments( List<File> a) { this.attachments = a; }
    public List<File> getAttachments() { return this.attachments; }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        addAttachement.setTooltip(new Tooltip("Add Attachment"));
    }    

    @FXML
    private void sendNowButtonPressed(ActionEvent event) throws MessagingException, AddressException, IOException {
        
        if (to.getText().equals("")) {
            
             MyMethod.showWarningAlert(null, "Enter At least 1 Recipient");
       
        } 
        else if (subject.getText().equals("")) {
            
               MyMethod.showWarningAlert(null, "Enter Subject");
            
        } else {
            
            // Create Mail and Send
            MyEmail mail = new MyEmail(this.getRecipients(), this.subject.getText(), this.getEditorText(), this.getAttachmentsPath());
            try {

                mail.sendNow();
                clearAllFieldsAndAttachments();

            } catch (MessagingException ex) {

                MyMethod.showErrorAlert("Email Not Sent", "Possible causes may be:\n=> No Internet\n=>Attachments Files Not Supported\n=>Port 25 is not set on your PC=>OR You have provided wrong emailID and Password");
            } catch (IOException ex) {

                MyMethod.showErrorAlert("No Attachments Find", "Attachments at your given path no longer exist");
            }
         
        }
       
    }
 
    @FXML
    private void sendLaterButtonPressed(ActionEvent event) throws IOException {
   
        if (to.getText().equals("")) {
             MyMethod.showWarningAlert(null, "Enter At least 1 Recipient");
            
        } 
        else if (subject.getText().equals("")) {
            
 
            MyMethod.showWarningAlert(null, "Enter Subject");
            
        } else {
            
             initDateTimeStage();
        }
        
        
    }

    @FXML
    private void addAttachementButtonPressed(ActionEvent event) {
        
        
        fileChooser = new FileChooser();
        
        fileChooser.setTitle("Select Attachement");
        fileChooser.setInitialDirectory(new File("C:\\"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.*"));
        
        
        attachments = fileChooser.showOpenMultipleDialog(subject.getScene().getWindow());
        
        
    }

    // Function to get all files' path in variable "AttachmentPath"
    private void setAttachmetsPath() {
        
        attachmentsPath = null;
        
        if (attachments != null && attachments.size() > 0) {
            
            for (int i=0; i<attachments.size(); i++) {
                
                if (i == 0) {
                    
                    attachmentsPath = attachments.get(i).getAbsolutePath();
                }
                else {
                    attachmentsPath = attachmentsPath + ",";
                    attachmentsPath = attachmentsPath + attachments.get(i).getAbsolutePath();
                }
            } 
            
        }
        
    }
    
    
    private void setRecipients() {
        
        recipients = null;
        
        recipients = to.getText();
        
        if (!cc.getText().equals("")) {
            
            recipients = recipients+","+cc.getText();
        }
        
        if (!bcc.getText().equals("")) {
            
            recipients = recipients+","+bcc.getText();
        }
        
    }
    
    
    private void initDateTimeStage() throws IOException {
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("DateAndTime.fxml"));
        loader.load();
        
        DateAndTimeController dateAndTimeController = (DateAndTimeController) loader.getController();
        dateAndTimeController.init(this);
        
        Parent root = loader.getRoot();
        
        Stage dateTimeStage = new Stage();
        dateTimeStage.setScene(new Scene(root));
        dateTimeStage.initModality(Modality.APPLICATION_MODAL);
        dateTimeStage.initOwner(sendLater.getScene().getWindow());
        dateTimeStage.setTitle("Schedule Email");
        dateTimeStage.setMaxHeight(179);
        dateTimeStage.setMaxWidth(458);
        dateTimeStage.setMinHeight(179);
        dateTimeStage.setMinWidth(458);
        dateTimeStage.show();
    } 

    public void clearAllFieldsAndAttachments() {
        
        this.setSubjectText("");
        this.setToText("");
        this.setEditorText("");
        this.setAttachmentsPath("");
        this.setAttachments(null);
        this.cc.setText("");
        this.bcc.setText("");
    }
    
    public Boolean isValidRecipientsEmail() { 
        
        String tempTo = getToText().toLowerCase();
        Integer index = tempTo.indexOf(".com");
        
        return false;
    }

   
}
