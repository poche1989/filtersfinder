package com.pochesconsulting.filtersfinder;

import com.pochesconsulting.helpers.SystemHelper;

import com.pochesconsulting.dto.FilterDetails;
import com.pochesconsulting.dto.OrderDetails;
import com.pochesconsulting.hibernate.LoadFiltersDetails;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class MainSceneController implements Initializable {

    private Integer isPleated = 0;
    private Integer minOrderAmount = 12;

    @FXML
    private Spinner<Double> lenghtSpn;

    @FXML
    private Spinner<Double> widthSpn;

    @FXML
    private Spinner<Integer> depthSpn;

    @FXML
    private Spinner<Integer> amountSpn;

    @FXML
    private TextField skuTxt;

    @FXML
    private TextField unitPriceTxt;

    @FXML
    private TextField totalPriceTxt;

    @FXML
    private CheckBox pleatedChk;

    @FXML
    private Button findBtn;

    @FXML
    private TextField specialOrderTxt;

    @FXML
    private Button submitOrderBtn;

    @FXML
    private Button closeBtn;

    @FXML
    private Button clearAllBtn;

    @FXML
    private Button receiveBtn;

    @FXML
    private Tab openOrdersTab;

    @FXML
    private TableView<OrderDetails> openOrdersTable;

    @FXML
    private TableColumn<OrderDetails, String> orderNumCol;

    @FXML
    private TableColumn<OrderDetails, String> skuNumCol;

    @FXML
    private TableColumn<OrderDetails, Integer> amtCol;

    @FXML
    private TableColumn<OrderDetails, Double> totalPriceCol;

    @FXML
    private TableColumn<OrderDetails, String> lastActCol;

    @FXML
    private TableColumn<OrderDetails, String> statusCol;

    @FXML
    private TableColumn<?, ?> actionCol;

    @FXML
    private Button filtersDetailsCloseBtn;

    FilterDetails filter = new FilterDetails();
    OrderDetails order = new OrderDetails();
    //SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    public SpinnerValueFactory createValueFactory(String type, String minValue, String maxValue, String startValue, String stepBy) {

        SpinnerValueFactory factory = null;

        if ("Double".equals(type)) {
            factory = new SpinnerValueFactory.DoubleSpinnerValueFactory(Double.valueOf(minValue), Double.valueOf(maxValue), Double.valueOf(startValue), Double.valueOf(stepBy));
        } else if ("Integer".equals(type)) {
            factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(Integer.valueOf(minValue), Integer.valueOf(maxValue), Integer.valueOf(startValue), Integer.valueOf(stepBy));
        }

        return factory;
    }

    @FXML
    void isChecked(ActionEvent event) {
        if (pleatedChk.isSelected()) {
            this.minOrderAmount = 6;
            this.isPleated = 1;
        } else {
            this.minOrderAmount = 12;
            this.isPleated = 0;
        }

        this.amountSpn.setValueFactory(createValueFactory("Integer", this.minOrderAmount.toString(), "5000", this.minOrderAmount.toString(), this.minOrderAmount.toString()));
        onFindBtnClicked(event);
    }

    @FXML
    void onFindBtnClicked(ActionEvent event) {

        try (Session session = SystemHelper.getSession()) {
            session.beginTransaction();

            this.amountSpn.setValueFactory(createValueFactory("Integer", this.minOrderAmount.toString(), "5000", this.minOrderAmount.toString(), this.minOrderAmount.toString()));
            Double lookUpValue = lenghtSpn.getValue() * widthSpn.getValue() * Double.valueOf(depthSpn.getValue());
            String hql = "FROM FilterDetails WHERE startRange <= " + lookUpValue + "AND endRange >=" + lookUpValue + "AND isPleated = " + this.isPleated;

            List filters = session.createQuery(hql).list();
            for (Iterator iter = filters.iterator(); iter.hasNext();) {

                filter = (FilterDetails) iter.next();

                skuTxt.setText(filter.getSkuNumber());
                unitPriceTxt.setText(filter.getUnitPrice().toString());
                setTotalPrice();
            }
            session.close();
            submitOrderBtn.setDisable(false);
        }
    }

    public void loadOpenOrdersList() {
        Session session = SystemHelper.getSession();
        String hql = "FROM OrderDetails WHERE orderStatus NOT LIKE :Closed";

        ObservableList<OrderDetails> openOrders = FXCollections.observableArrayList(session.createQuery(hql).setParameter("Closed", "Closed").list());

        for (OrderDetails order : openOrders) {
            System.out.println(order);
        }

        this.openOrdersTable.setItems(openOrders);
        this.openOrdersTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        session.clear();
        session.close();
    }

    @FXML
    void onRefreshBtnClicked(ActionEvent event) {
        loadOpenOrdersList();
    }

    @FXML
    void ondeleteOpenOrderBtnClicked(ActionEvent event) {
        Session session = SystemHelper.getSession();
        ObservableList<OrderDetails> selected = this.openOrdersTable.getSelectionModel().getSelectedItems();
        for (OrderDetails order : selected) {

            session.remove(order);
            Transaction transaction = session.beginTransaction();
            session.getTransaction().commit();
        }
        session.close();
        onRefreshBtnClicked(event);
    }

    @FXML
    void onSubmitOrderClicked(ActionEvent event) {

        if (!specialOrderTxt.getText().isEmpty()) {
            Session session = SystemHelper.getSession();
            OrderDetails order = new OrderDetails(specialOrderTxt.getText(), skuTxt.getText(), amountSpn.getValue(), Double.valueOf(totalPriceTxt.getText()), "Created", SystemHelper.getTimeStamp());
            Transaction transaction = session.beginTransaction();

            try {
                session.save(order);
                session.getTransaction().commit();
                onClearAllClicked(event);
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!!!");
                alert.setHeaderText("Check your order number,\n this one already exists.");
                alert.setContentText(ex.getMessage());
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.show();

            } finally {
                if (session != null) {
                    session.close();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!!!");
            alert.setHeaderText("Order number field is empty.");
            alert.setContentText(("The Order number is a required field."));
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.show();
        }
    }

    @FXML
    void amountChanged(MouseEvent event) {
        setTotalPrice();
    }

    void setTotalPrice() {
        totalPriceTxt.setText(String.valueOf(Double.valueOf(unitPriceTxt.getText()) * amountSpn.getValue()));
    }

    @FXML
    void onClearAllClicked(ActionEvent event) {
        this.lenghtSpn.setValueFactory(createValueFactory("Double", "1.25", "100.00", "1.25", "0.25"));
        this.widthSpn.setValueFactory(createValueFactory("Double", "1.25", "100.00", "1.25", "0.25"));
        this.depthSpn.setValueFactory(createValueFactory("Integer", "1", "5", "1", "1"));
        this.skuTxt.setText("");
        this.amountSpn.setValueFactory(createValueFactory("Integer", "0", "0", "0", "0"));
        this.unitPriceTxt.setText("");
        this.totalPriceTxt.setText("");
        this.specialOrderTxt.setText("");
        this.submitOrderBtn.setDisable(true);
    }

    @FXML
    void onCloseBtnClicked(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void loadFiltersDetailsClicked(ActionEvent event) {
        LoadFiltersDetails.load();
    }

    @FXML
    void manageFilters(ActionEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ManageFiltersDetails.fxml"));
            Parent manageFiltersRoot = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Manage Filters Details");
            stage.setScene(new Scene(manageFiltersRoot));
            stage.show();

        } catch (Exception ex) {
            System.out.println("Can't load window: " + ex);
        }
    }
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.lenghtSpn.setValueFactory(createValueFactory("Double", "1.25", "100.00", "1.25", "0.25"));
        this.widthSpn.setValueFactory(createValueFactory("Double", "1.25", "100.00", "1.25", "0.25"));
        this.depthSpn.setValueFactory(createValueFactory("Integer", "1", "5", "1", "1"));

        this.orderNumCol.setCellValueFactory(new PropertyValueFactory<>("orderNumber"));
        this.skuNumCol.setCellValueFactory(new PropertyValueFactory<>("sku"));
        this.amtCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        this.totalPriceCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        this.lastActCol.setCellValueFactory(new PropertyValueFactory<>("lastActivity"));
        this.statusCol.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));

        Tooltip pleatedHelp = new Tooltip("Pleated filters last up to 3 months each vs 1 month on the non-pleated filters");
        this.pleatedChk.setTooltip(pleatedHelp);

        loadOpenOrdersList();

    }
}
