package windows;


import client.Client;
import database.mysqlDatabase;
import employee.employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class Log<client> {
    public Label name;
    public Label surname;
    public Button logOut;
    public Stage dialogStage;
    public ComboBox clientsNames;
    public Button addNewClient;
    public Label menoClient;
    public Label priezviskoCleint;
    public Label emailClient;

    mysqlDatabase database = mysqlDatabase.getInstanceOfDatabase();
    List<Client>  client = database.getAllClients();


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
        fxmlLoader.setLocation(getClass().getResource("../windows/sample.fxml"));
        Parent accountView = fxmlLoader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(accountView));

        stage.show();
    }


    public void clients() throws SQLException{
        ObservableList<String> list = FXCollections.observableArrayList();

        for (int i=0; i<client.size();i++){
            list.add(client.get(i).getFname()+" "+client.get(i).getLname());
        }
        clientsNames.setItems(list);
        clientsNames.getSelectionModel().select(0);
    }


    public void createNewClient(ActionEvent actionEvent) throws IOException {
        Node node = (Node) actionEvent.getSource();
        dialogStage = (Stage) node.getScene().getWindow();
        dialogStage.close();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../windows/newClient.fxml"));
        Parent accountView = fxmlLoader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(accountView));

        stage.show();
    }

    public void showClientsInfo() throws SQLException {
        System.out.println("chcem nacitat info");
        mysqlDatabase database = mysqlDatabase.getInstanceOfDatabase();
        Client clientik = database.getClientInfo(getSelectedClientID());
        menoClient.setText(clientik.getFname());
        priezviskoCleint.setText(clientik.getLname());
        emailClient.setText(clientik.getEmail());
    }


    public int getSelectedClientID() {
        System.out.println("vyberam idcko clienta ktory je vybraty");
        System.out.println(clientsNames.getSelectionModel().getSelectedIndex());
        int clientsIndex=clientsNames.getSelectionModel().getSelectedIndex();
        return client.get(clientsIndex).getId();
    }
}









