
//Packages and Imports

package academiccalendar.ui.editevent;

import DoctorsPackage.DoctorsActionClass;
import academiccalendar.data.model.Model;
import academiccalendar.database.DBHandler;
import academiccalendar.ui.addevent.AddEventController;
import academiccalendar.ui.main.FXMLDocumentController;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class EditEventController implements Initializable {
    
    // Main Controller -------------------------------
    private FXMLDocumentController mainController;
    // -------------------------------------------------------------------
    
    //--------------------------------------------------------------------
    //---------Database Object -------------------------------------------
    DBHandler databaseHandler;
    //--------------------------------------------------------------------
    @FXML
    private Label topLabel;
    @FXML
    private JFXTimePicker subject;
    @FXML
    private JFXComboBox<String> termSelect;
    @FXML
    private JFXDatePicker date;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private JFXComboBox<String> doctor;
    @FXML
    private JFXTextField indicator;
    
    //Set main controller
    public void setMainController(FXMLDocumentController mainController) {
        this.mainController = mainController ;
    }

    // These fields are for mouse dragging of window
    private double xOffset;
    private double yOffset;
    
    
    //Function that fills the date picker based on the clicked event's date
    void autofillDatePicker() {
        
        // Get selected day, month, and year and autofill date selection
        int day = Model.getInstance().event_day;
        int month = Model.getInstance().event_month + 1;
        int year = Model.getInstance().event_year;
        int termID = Model.getInstance().event_term_id;
        String descript = Model.getInstance().event_subject;
        String Doctor = Model.getInstance().event_doctor;
        String indicatorr = Model.getInstance().event_indicator;
       
        //Query to get ID for the selected Term
        String getIDQuery = "SELECT TermName From TERMS "
                + "WHERE TERMS.TermID= " + termID + " ";
        
        //Varialbe that holds the name of the current event's term based on a given term ID
        String chosenTermName = "";

        //Store the results from executing the Query
        ResultSet result = databaseHandler.executeQuery(getIDQuery);
        //Try-catch statements that will get the ID if a result was actually gotten back from the database
        try {
             while(result.next()){
                 //store ID into the corresponding variable
                 chosenTermName = result.getString("TermName");
             
             }
             result.close();
        } catch (SQLException ex) {
            try {
                result.close();
            } catch (SQLException ex1) {
                Logger.getLogger(EditEventController.class.getName()).log(Level.SEVERE, null, ex1);
            }
             Logger.getLogger(AddEventController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       // Set default value for datepicker
       date.setValue(LocalDate.of(year, month, day));
       
       // Fill description field
         
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

LocalTime localDate = LocalTime.parse(descript, formatter);
       subject.setValue(localDate);
       
       //Fill term drop-down menu with current event's term
       termSelect.getSelectionModel().select(chosenTermName);
       doctor.getSelectionModel().select(Doctor);
       indicator.setText(indicatorr);
       
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        subject.setEditable(false);
        
      Platform.runLater(()->indicator.selectEnd());
        
        //*** Instantiate DBHandler object *******************
        databaseHandler = new DBHandler();
        //****************************************************
        
        //Fill the date picker
        autofillDatePicker();
        
        //Get the list of exisitng terms from the database and show them in the correspondent drop-down menu
        ObservableList<String> termsList;
        try {
            //Get terms from database and store them in the ObservableList variable "termsList"
            termsList = databaseHandler.getListOfTerms();
            //Show list of terms in the drop-down menu
            termSelect.setItems(termsList);
               DoctorsActionClass a = new DoctorsActionClass();
            ObservableList<String> termss = a.getListOfTerms();
            doctor.setItems(termss);
        } catch (SQLException ex) {
            Logger.getLogger(EditEventController.class.getName()).log(Level.SEVERE, null, ex);
        }   
        
        
        // ************* Everything below is for Draggable Window ********
        
        // Set up Mouse Dragging for the Event pop up window
        topLabel.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage stage = (Stage) rootPane.getScene().getWindow();
                xOffset = stage.getX() - event.getScreenX();
                yOffset = stage.getY() - event.getScreenY();
            }
        });
        // Set up Mouse Dragging for the Event pop up window
        topLabel.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage stage = (Stage) rootPane.getScene().getWindow();
                stage.setX(event.getScreenX() + xOffset);
                stage.setY(event.getScreenY() + yOffset);
            }
        });
        // Change cursor when hover over draggable area
        topLabel.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage stage = (Stage) rootPane.getScene().getWindow();
                Scene scene = stage.getScene();
                scene.setCursor(Cursor.HAND); //Change cursor to hand
            }
        });
        
        // Change cursor when hover over draggable area
        topLabel.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage stage = (Stage) rootPane.getScene().getWindow();
                Scene scene = stage.getScene();
                scene.setCursor(Cursor.DEFAULT); //Change cursor to hand
            }
        });
        
        String indicatvalue1 =  termSelect.getValue();
        if (indicatvalue1.equals("Appointment")){indicator.setPromptText("New Booking information");}
    
    else indicator.setPromptText("New Task");
        
    termSelect.setOnAction(e->{
    String indicatvalue =  termSelect.getValue();
        System.out.println(indicatvalue);
    
    if (indicatvalue.equals("Appointment")){indicator.setPromptText("New Booking information");}
    else indicator.setPromptText("New Task");
    });
    }    

    @FXML
    private void exit(MouseEvent event) {
        // Close the window
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void update(MouseEvent event) {
        updateEvent();
    }

    //Function that deletes a selected event
    @FXML
    private void delete(MouseEvent event) {
        
        //Show confirmation dialog to make sure the user want to delete the selected event
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Event Deletion");
        alert.setContentText("Are you sure you want to delete this event?");
        //Customize the buttons in the confirmation dialog
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        //Set buttons onto the confirmation window
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
        
        //Get the user's answer on whether deleting or not
        Optional<ButtonType> result = alert.showAndWait();
        
        //If the user wants to delete the event, call the function that deletes the event. Otherwise, close the window
        if (result.get() == buttonTypeYes){
            deleteEvent();
        } 
        else 
        {
            // Close the window
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close(); 
        }
        
    }
    
    
    //Function that updates the information of a selected event from the calendar
    public void updateEvent(){
        
        // Define date format
        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        //**********************************************************************
        //*******   Get OLD INFO of the Event to be edited/upated         ******
        //*******   which is the term ID, event description, event date,  ******
        //*******   and calendar name of the event to be edited           ******
        //**********************************************************************
        int day = Model.getInstance().event_day;
        int month = Model.getInstance().event_month + 1;
        int year = Model.getInstance().event_year;
        String eventDate = year + "-" + month + "-" + day;
        int termID = Model.getInstance().event_term_id;
        String descript = Model.getInstance().event_subject;
        String calName = Model.getInstance().calendar_name; 
        String olddoc = Model.getInstance().event_doctor;
        String oldindicator = Model.getInstance().event_indicator; 
        
        //Get the original date of the event to be updated in the format yyyy-mm-dd
        SimpleDateFormat auxDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        String auxDate = "empty";
        Date auxEventDate = new Date();
        try {
            auxEventDate = auxDateFormat.parse(eventDate);
            auxDate = auxDateFormat.format(auxEventDate);
        } catch (ParseException ex) {
            Logger.getLogger(EditEventController.class.getName()).log(Level.SEVERE, null, ex);
        }    
        
        //**********************************************************************
        //******    Get NEW INFO for the Event to be edited/updated   **********
        //**********************************************************************
        
        // Get the date value from the date picker
        String newCalendarDate = date.getValue().format(myFormat);
        // Subject for the event
          DateTimeFormatter myFormat1 = DateTimeFormatter.ofPattern("HH:mm");

        String v = subject.getValue().format(myFormat1);

        String newEventSubject = subject.getValue().format(myFormat1);

        // Get term that was selected by the user
        String term = termSelect.getValue();
        String newDoctor = doctor.getValue();
        String newindicator = indicator.getText();
        
        
        //Check if the event descritption contains the character ~ because it cannot contain it due to database and filtering issues
        if (newEventSubject.contains("~"))
        {
            //Show message indicating that the event description cannot contain the character ~
            Alert alertMessage = new Alert(Alert.AlertType.WARNING);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Event Description cannot contain the character ~");
            alertMessage.showAndWait();
            return;
        }
        
        
        //Get the ID of the new term selected by the user when editing the event's information
        int newTerm = databaseHandler.getTermID(term);
        
        //Query to will update the selected event with the new information
        String updateEventQuery = "UPDATE EVENTS"
                                + " SET "
                                + "EventDescription='" + newEventSubject + "', "
                                + "EventDate='" + newCalendarDate + "', "
                                + "DoctorName='" + newDoctor + "', "
                                + "Indicator='" + newindicator + "', "
                                + "TermID=" + newTerm
                                + " WHERE "
                                + "EVENTS.EventDescription='" + descript + "' AND "
                                + "EVENTS.EventDate='" + auxDate + "' AND "
                                + "EVENTS.TermID=" + termID + " AND "
                                + "EVENTS.DoctorName='" + olddoc + "' AND "
                                + "EVENTS.CalendarName='" + calName + "' ";
        
        
        //Execute query in otder to update the info for the selected event
        //and
        //Check if the update of the event in the database was successful, and show message either if it was or not
        if (subject.getValue() == null || termSelect.getSelectionModel().isEmpty()
             || doctor.getSelectionModel().isEmpty()   || date.getValue() == null
                || indicator.getText().isEmpty()) {
            Alert alertMessage = new Alert(Alert.AlertType.ERROR);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Please fill out all fields");
            alertMessage.showAndWait();
            return;
        }
        
        
      else  if(databaseHandler.executeAction(updateEventQuery)) {
            Alert alertMessage = new Alert(Alert.AlertType.INFORMATION);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Event was updated successfully");
            alertMessage.showAndWait();
            
            // Update view
            mainController.repaintView();
            
        }
        else //if there is an error
        {
            Alert alertMessage = new Alert(Alert.AlertType.ERROR);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Updating Event Failed!\nThere is already an event with the same information");
            alertMessage.showAndWait();
        }
        
        // Close the window
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
        
    }
    
    
    public void deleteEvent() {
        
        //**********************************************************************
        //*******   Get INFO of the Event to be edited/upated             ******
        //*******   which is the term ID, event description, event date,  ******
        //*******   and calendar name of the event to be edited           ******
        //**********************************************************************
        int day = Model.getInstance().event_day;
        int month = Model.getInstance().event_month + 1;
        int year = Model.getInstance().event_year;
        String eventDate = year + "-" + month + "-" + day;
        int termID = Model.getInstance().event_term_id;
        String descript = Model.getInstance().event_subject;
        String calName = Model.getInstance().calendar_name;
        
        //Get the original date of the event to be updated in the format yyyy-mm-dd
        SimpleDateFormat auxDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        String auxDate = "empty";
        Date auxEventDate = new Date();
        try {
            auxEventDate = auxDateFormat.parse(eventDate);
            auxDate = auxDateFormat.format(auxEventDate);
        } catch (ParseException ex) {
            Logger.getLogger(EditEventController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Query that will delete the selected event
        String deleteEventQuery = "DELETE FROM EVENTS "
                                + "WHERE "
                                + "EVENTS.EventDescription='" + descript + "' AND "
                                + "EVENTS.EventDate='" + auxDate + "' AND "
                                + "EVENTS.TermID=" + termID + " AND "
                                + "EVENTS.CalendarName='" + calName + "' ";
        
        //Execute query that deletes the selected event
        boolean eventWasDeleted = databaseHandler.executeAction(deleteEventQuery);
        
        if (eventWasDeleted)
        {
            //Show message indicating that the selected rule was deleted
            Alert alertMessage = new Alert(Alert.AlertType.INFORMATION);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Selected event was successfully deleted");
            alertMessage.showAndWait();
            
            // Update view
            mainController.repaintView();
                
            // Close the window, so that when user clicks on "Manage Rules" only the remaining existing rules appear
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close();
        }
        else
        {
            //Show message indicating that the rule could not be deleted
            Alert alertMessage = new Alert(Alert.AlertType.ERROR);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Deleting Event Failed!");
            alertMessage.showAndWait();
        }
        
    }
    
}// end of EditEventController class
