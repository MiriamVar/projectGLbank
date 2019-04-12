package sample;


import client.Client;
import database.mysqlDatabase;
import employee.employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Log {
    public Label name;
    public Label surname;
    public Button logOut;
    public Stage dialogStage;
    public ComboBox clientsNames;
    public Button addNewClient;

    public void initialize () throws SQLException {
        clients();
    }


    public void showData(employee emp) {
        name.setText(emp.getFname());
        surname.setText(emp.getLname());
    }

    public void logOut(ActionEvent actionEvent) throws IOException {

        Node node = (Node) actionEvent.getSource();
        dialogStage = (Stage) node.getScene().getWindow();
        dialogStage.close();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("sample.fxml"));
        Parent accountView = fxmlLoader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(accountView));

        stage.show();
    }


    public void clients() throws SQLException{
        mysqlDatabase database = mysqlDatabase.getInstanceOfDatabase();
        List<Client> client = database.getAllClients();
        ObservableList<String> list = FXCollections.observableArrayList();
        for (int i=0; i<client.size();i++){
            list.add(client.get(i).getFname()+" "+client.get(i).getLname());
        }
        clientsNames.setItems(list);
        clientsNames.getSelectionModel().select(0);
    }


}









