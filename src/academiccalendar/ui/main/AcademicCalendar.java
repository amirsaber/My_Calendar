
package academiccalendar.ui.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class AcademicCalendar extends Application {
        
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        // Set main window icon
        stage.getIcons().add(
        new Image( "academiccalendar/ui/icons/app_icon.png" ));
        stage.setTitle("Calendar By. Amir");
        
        // Maximize window at launch
        stage.setMaximized(true);
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
   
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
