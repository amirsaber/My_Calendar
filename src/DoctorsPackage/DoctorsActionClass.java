/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DoctorsPackage;

import SqlDataBase.SqliteConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

/**
 *
 * @author Amir
 */
public class DoctorsActionClass {

    private Connection conn = SqliteConnection.Connector();

    private ResultSet rs;

    private PreparedStatement ps;

    public List DoctorsValues;
// this for bind autocomplete
    public void LoadNames() throws SQLException {

        //    conn = DriverManager.getConnection("jdbc:sqlite:Clinic_Manager_DB.db");
        try {

            String query = "Select DoctorFullName from Doctors";
            ps = conn.prepareStatement(query);

            rs = ps.executeQuery();
            DoctorsValues = new ArrayList();

            while (rs.next()) {
                DoctorsValues.add(rs.getString(1));

            }
            ps.close();
            rs.close();

        } catch (Exception eViewAllPatient) {
            System.err.println(eViewAllPatient);
            Alert CreateNewUserErrorAlert = new Alert(Alert.AlertType.ERROR);
            CreateNewUserErrorAlert.setHeaderText(null);
            CreateNewUserErrorAlert.setContentText(eViewAllPatient.getMessage());
            CreateNewUserErrorAlert.showAndWait();
            ps.close();
            rs.close();
        }

    }
    // this for fill combobox
     public ObservableList<String> getListOfTerms() throws SQLException {

        //ArrayList that will contain all terms saved in the TERMS Tables
        ObservableList<String> listOfTerms = FXCollections.observableArrayList();// = new ObservableList();

        //Query that will obtain all available Term Names from the database table TERMS
        String queryListOfTerms ="Select DoctorFullName from Doctors";
        //Variable that will hold the result of executing the previous query
      ps = conn.prepareStatement(queryListOfTerms);

            rs = ps.executeQuery();

        try {
            //While there are Term Names in the ResultSet variable, add each one of them to the ObservableList of Strings
            while (rs.next()) {
                //get the term name and store it in a String variable
                String termName = rs.getString("DoctorFullName");
                //add Term Name to list of terms
                listOfTerms.add(termName);
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage() + "--- error at getListOfTerms method in DBHandler class");
           ps.close();
            rs.close();
        }

        return listOfTerms;
    }
}
