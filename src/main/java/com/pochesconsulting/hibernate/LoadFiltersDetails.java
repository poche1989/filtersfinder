/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pochesconsulting.hibernate;

import com.pochesconsulting.dto.FilterDetails;
import com.pochesconsulting.filtersfinder.MainSceneController;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author poche
 */
public class LoadFiltersDetails {

    @FXML
    public static void load() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

        Stage messageWindow = new Stage() {
        };

        File selectedFile = fileChooser.showOpenDialog(messageWindow);
        if (selectedFile != null) {
            try {
                Scanner scan = new Scanner(selectedFile);
                SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

                Session session = sessionFactory.openSession();
                session.beginTransaction();

                while (scan.hasNext()) {
                    String[] filterData = scan.nextLine().split(", ");

                    FilterDetails filter = new FilterDetails(Double.valueOf(filterData[0]), Double.valueOf(filterData[1]), filterData[2], Double.valueOf(filterData[3]), Integer.valueOf(filterData[4]));
                    session.save(filter);

                }
                session.getTransaction().commit();
                session.close();

            } catch (FileNotFoundException ex) {
                Logger.getLogger(MainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
