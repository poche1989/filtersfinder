package com.pochesconsulting.filtersfinder;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.pochesconsulting.dto.FilterDetails;
import com.pochesconsulting.helpers.SystemHelper;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import org.hibernate.Session;

/**
 * FXML Controller class
 *
 * @author poche
 */
public class ManageFiltersDetailsController implements Initializable {

    @FXML
    private TableView<FilterDetails> filtersDetailsTbl;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadData();
    }

    private void loadData() {
        Session session = SystemHelper.getSession();
        String hql = "FROM FilterDetails";

        ObservableList<FilterDetails> filtersDetails = FXCollections.observableArrayList(session.createQuery(hql).list());
        this.filtersDetailsTbl.setItems(filtersDetails);
//        this.filtersDetailsTbl.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        session.clear();
        session.close();
    }

}
