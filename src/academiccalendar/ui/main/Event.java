
package academiccalendar.ui.main;

import javafx.beans.property.SimpleStringProperty;

public class Event {
    private final SimpleStringProperty term;
    private final SimpleStringProperty subject;
    private final SimpleStringProperty date;
    private final SimpleStringProperty doctor;
    private final SimpleStringProperty indicator;
 
    public Event(String term, String subject, String date , String doctor , String indicator) {
        this.term = new SimpleStringProperty(term);
        this.subject = new SimpleStringProperty(subject);
        this.date = new SimpleStringProperty(date);
        this.doctor = new SimpleStringProperty(doctor);
        this.indicator = new SimpleStringProperty(indicator);
    }
    
    // Note: We need these accessors.
    public String getTerm() {
        return term.get();
    }

    public String getSubject() {
        return subject.get();
    }

    public String getDate() {
        return date.get();
    }
    public String getDoctor() {
        return doctor.get();
    }
    public String getIndicator() {
        return indicator.get();
    }
}
