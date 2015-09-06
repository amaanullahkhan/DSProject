/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mylibrary;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import model.EmailPlannerModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author Amaan
 */
public class MyEmail extends TimerTask {
    
    // Properties
    private final StringProperty recipients = new SimpleStringProperty(this, "recipients", null);
    private final StringProperty subject = new SimpleStringProperty(this, "subject", null);
    private final StringProperty message = new SimpleStringProperty(this, "message", null);
    private final StringProperty attachments = new SimpleStringProperty(this, "attachments", null);
    private final StringProperty date = new SimpleStringProperty(this, "date", null);
    private final StringProperty status = new SimpleStringProperty(this, "status", null);

    // Constructor for send now 
    public MyEmail(String r, String s, String m, String a) { 
        this.recipients.set(r);
        this.subject.set(s);
        this.message.set(m);
        this.attachments.set(a);
    }
    
    // Constructor for send later
    public MyEmail(String r, String s, String m, String a, String d, String st) {
       
        this(r,s,m,a);
        this.setDate(d);
        this.setStatus(st);
        
    }
    
    // Getter Setter for Recipient
    public final String getRecipient(){ return this.recipients.get(); }
    public final void setRecipient(String r){ this.recipients.set(r);}
    
    // Getter Setter for Subject
    public final String getSubject() { return this.subject.get(); }
    public final void setSubject(String s) { this.subject.set(s); }
    
    // Getter Setter for Message
    public final String getMsg() { return this.message.get(); }
    public final void setMsg(String m) { this.message.set(m); }
    
    // Getter Setter for Attachments
    public final String getAttachments() { return this.attachments.get(); } 
    public final void setAttachments(String a) { this.attachments.set(a); }
    
    // Getter Setter for date
    public final String getDate() { return this.date.get(); }
    public final void setDate(String d) { this.date.set(d);  }
    public final Date getDateAsDateObject() { 
        
        int index = 0;
        String dateTemp = this.getDate();
        
        index = dateTemp.indexOf("-");
        Integer year = Integer.parseInt(dateTemp.substring(0, index));
        dateTemp = dateTemp.substring(index + 1);
                
        index = dateTemp.indexOf("-");
        Integer month = Integer.parseInt(dateTemp.substring(0, index));
        dateTemp = dateTemp.substring(index + 1);
        
        index = dateTemp.indexOf(" ");
        Integer day = Integer.parseInt(dateTemp.substring(0, index));
        dateTemp = dateTemp.substring(index + 1);
        
        index = dateTemp.indexOf(":");
        Integer hour = Integer.parseInt(dateTemp.substring(0, index));
        dateTemp = dateTemp.substring(index + 1);
        
        index = dateTemp.indexOf(":");
        Integer minute = Integer.parseInt(dateTemp.substring(0, index));
       
        Calendar dateTime = Calendar.getInstance();
        dateTime.set(Calendar.YEAR, year);
        dateTime.set(Calendar.MONTH, month - 1);
        dateTime.set(Calendar.DAY_OF_MONTH, day);
        dateTime.set(Calendar.HOUR, hour);
        dateTime.set(Calendar.MINUTE, minute);
        dateTime.set(Calendar.SECOND, 00);
                
        return dateTime.getTime();
    
    }
    
    // Getter Setter for status
    public final String getStatus() { return this.status.get(); }
    public final void setStatus(String d) { this.status.set(d);  }
    
    @Override
    public void run() {
        
        try {
            
            this.sendNow();       
            this.remove();
            
        } catch (MessagingException ex) {
            
            MyMethod.showErrorAlert("Email Not Sent", "Possible causes may be:\n=> No Internet\n=>Attachments Files Not Supported\n=>Port 25 is not set on your PC=>OR You have provided wrong emailID and Password");
        } catch (IOException ex) {
            
            MyMethod.showErrorAlert("No Attachments Find", "Attachments at your given path no longer exist");
        }
    }
    
// Function to send Mail
    public void sendNow() throws AddressException, MessagingException, IOException {
 
//Step1		
        Properties mailServerProperties;
        Session getMailSession;
        MimeMessage generateMailMessage;

        System.out.println("\n 1st ===> setup Mail Server Properties..");
        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", "587");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");
        System.out.println("Mail Server Properties have been setup successfully..");

//Step2		
        System.out.println("\n\n 2nd ===> get Mail Session..");
        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        generateMailMessage = new MimeMessage(getMailSession);
        
        // Separating All Recipients and adding recipient to this mail
        String recipientsTemp = recipients.get();
        int index = recipientsTemp.indexOf(",");
        
        if (index == -1) {
            
            generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientsTemp));
        } 
        
        else {

            while (!recipientsTemp.equals("")) {

                if (index == -1) {

                    String recipient = recipientsTemp;
                    System.out.println(recipient);
                    generateMailMessage.addRecipient(Message.RecipientType.BCC, new InternetAddress(recipient));

                    break;
                }

                String recipient = recipientsTemp.substring(0, index);
                System.out.println(recipient);
                generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(recipient));
                

                recipientsTemp = recipientsTemp.substring(index + 1);
                index = recipientsTemp.indexOf(",");
            }

        }
  
        generateMailMessage.setSubject(this.subject.get());
        
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(this.message.get());
           
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        // Setting all Attachments of this mail
        if ( attachments.get() != null ) { 
            
            String tempAttachments = attachments.get();
            int commaIndex = tempAttachments.indexOf(",");

            if (commaIndex == -1) { // For Just 1 Attachment
                
                messageBodyPart = new MimeBodyPart();
                messageBodyPart.attachFile(new File(tempAttachments));
                multipart.addBodyPart(messageBodyPart);
                
            }

            else { // For more than 1 attachments
                
                while (!tempAttachments.equals("")) {

                    if (commaIndex == -1) {

                        // Adding attachment
                        String attachmentPath = tempAttachments;
                        messageBodyPart = new MimeBodyPart();
                        messageBodyPart.attachFile(new File(attachmentPath));
                        multipart.addBodyPart(messageBodyPart);
                        
                        break;
                    }

                    String attachmentPath = tempAttachments.substring(0, commaIndex);
                    System.out.println(attachmentPath);
                    
                    // Adding attachment
                    messageBodyPart = new MimeBodyPart();
                    messageBodyPart.attachFile(new File(attachmentPath));
                    multipart.addBodyPart(messageBodyPart);
                    
                    tempAttachments = tempAttachments.substring(commaIndex + 1);
                    commaIndex = tempAttachments.indexOf(",");
                }
                
            }

        }
        
        generateMailMessage.setContent(multipart);
        System.out.println("Mail Session has been created successfully..");

//Step3		
        System.out.println("\n\n 3rd ===> Get Session and Send mail");
        Transport transport = getMailSession.getTransport("smtp");

        // Enter your correct gmail UserID and Password (XXXApp Shah@gmail.com)
        transport.connect("smtp.gmail.com", EmailPlannerModel.email , EmailPlannerModel.password);
        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
        transport.close();
        System.out.println("\n\n 3rd ===> Email Sent successfully");
        
    }
    
// Function to add this mail object in database and in table list and start scheduler
    public void sendLater() {
        
        
        
    }
    
    public void start() { 
        
        if (this.getStatus().equals("1")) {
            
            return;
        }
        
        for (int i = 0; i < EmailPlannerModel.emails.size(); i++) {
            
            if ( EmailPlannerModel.emails.get(i).equals(this) ){
                
                try {
                    Statement stm = MyMethod.makeConnectionToDataBase();
                    Integer rowsAffected = stm.executeUpdate("Update Mails Set Status = '1' where Recipients = '"+this.getRecipient()+"' AND Subject = '"+this.getSubject()+"'AND Message = '"+this.getMsg()+"'AND Attachments = '"+this.getAttachments()+"'AND Date = '"+this.getDate()+"' AND Status = '"+this.getStatus()+"';");
                    System.out.println(rowsAffected);
      
                    EmailPlannerModel.getEmails();
                    
                } catch (SQLException ex) {
                    Logger.getLogger(MyEmail.class.getName()).log(Level.SEVERE, null, ex);
                }  
               
            }
        }
             
    }
     
    public void stop() {

        if (this.getStatus().equals("0")) {
            
            return;
        }
        
        for (int i = 0; i < EmailPlannerModel.emails.size(); i++) {
            
            if ( EmailPlannerModel.emails.get(i).equals(this) ){
                
                try {
                    
                    Statement stm = MyMethod.makeConnectionToDataBase();
                    stm.executeUpdate("update Mails set Status = '0' where Recipients = '"+this.getRecipient()+"' AND Subject = '"+this.getSubject()+"'AND Message = '"+this.getMsg()+"'AND Attachments = '"+this.getAttachments()+"'AND Date = '"+this.getDate()+"' AND Status = '"+this.getStatus()+"';");
                    
                    EmailPlannerModel.getEmails();
                    
                } catch (SQLException ex) {
                    
                    MyMethod.showErrorAlert("Data Base Error", "There was an error while connecting to Data Base");
                }
               
            }
        }
        
//        this.setStatus("0");
      
    }

    public void remove() {
        
        for (int i = 0; i < EmailPlannerModel.emails.size(); i++) {
            
            if ( EmailPlannerModel.emails.get(i).equals(this) ){
                
                try {
                    
                    Statement stm = MyMethod.makeConnectionToDataBase();
                    stm.executeUpdate("delete from Mails where Recipients = '"+this.getRecipient()+"' AND Subject = '"+this.getSubject()+"'AND Message = '"+this.getMsg()+"'AND Attachments = '"+this.getAttachments()+"'AND Date = '"+this.getDate()+"' AND Status = '"+this.getStatus()+"';");
                    
                    EmailPlannerModel.getEmails();
                    
                } catch (SQLException ex) {
                    
                    MyMethod.showErrorAlert("Data Base Error", "There was an error while connecting to Data Base");
                }
               
            }
        }
   
    }
  
}
