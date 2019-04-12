package sample;


import client.Client;
import database.mysqlDatabase;
import employee.employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class Log {
    public Label name;
    public Label surname;
    public Button logOut;
    public Stage dialogStage;
    public List<Client> nameOfClient;
    public ComboBox clientsNames;


    public void showData(employee emp){
        name.setText(emp.getFname());
        surname.setText(emp.getLname());
    }

    public void logOut(ActionEvent actionEvent) throws IOException {

        Node node = (Node)actionEvent.getSource();
        dialogStage = (Stage) node.getScene().getWindow();
        dialogStage.close();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("sample.fxml"));
        Parent accountView = fxmlLoader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(accountView));

        stage.show();
    }

    public void clients(ActionEvent actionEvent) {
        System.out.println("clieeeents");
        mysqlDatabase database = mysqlDatabase.getInstanceOfDatabase();
        nameOfClient =database.getAllClients();
        for (Client client: nameOfClient ) {
        //    nameOfClient = client.getFname()+client.getLname();
            clientsNames.setValue(nameOfClient);
            System.out.println(client);
            clientsNames.getSelectionModel().select(0);
    }

    }


//    public void gettingAllClients( List<Client> allClients ) {
//        mysqlDatabase database = mysqlDatabase.getInstanceOfDatabase();
//        database.getConnection();
//        for (Client client: allClients) {
//            nameOfClient = client.getFname()+client.getLname();
//            clientsNames.getItems().add(nameOfClient);
//            System.out.println(nameOfClient);
//            clientsNames.getSelectionModel().select(0);
//        }
//
//    }



}
