/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pochesconsulting.helpers;

import com.pochesconsulting.dto.OrderDetails;
import com.pochesconsulting.filtersfinder.MainSceneController;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author poche
 */
public class SystemHelper {

    public static String getTimeStamp() {
        Date date = new Date();
        long time = date.getTime();
        Timestamp ts = new Timestamp(time);
        String s = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(ts);

        return s;
    }

    public static Session getSession() {
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();

        return session;
    }
}
