package windows;


import client.Client;
import com.sun.jdi.IntegerType;
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
import client.Account;


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

    public ComboBox clientsAccounts;
    public Label accNumber;
    public Label money;

    mysqlDatabase database = mysqlDatabase.getInstanceOfDatabase();
    private List<Client> client;
    private List<Account> account;
    public int clientsIndex;
    public Client clientik;
    private Integer selectedInxAcc;


    public void initialize () throws SQLException {
        this.client = database.getAllClients();
        clients();
        this.account = database.getAllAccounts(clientik.getId());
        clientsAccounts.getSelectionModel().select(0);
        showClientsInfo();
    }


    public void clients() throws SQLException{
        ObservableList<String> list = FXCollections.observableArrayList();

        for (int i=0; i<this.client.size();i++){
            list.add(this.client.get(i).getFname()+" "+this.client.get(i).getLname());
        }
        clientsNames.setItems(list);
        clientsNames.getSelectionModel().select(0);
        clientik=client.get(0);
        showClientsInfo(clientik.getId());

    }

    //vypise info hned o prvom useri
    public void showClientsInfo(int id) throws SQLException {
        menoClient.setText(clientik.getFname());
        priezviskoCleint.setText(clientik.getLname());
        emailClient.setText(clientik.getEmail());

    }

    //naplni labely s info o useroch
    public void showClientsInfo() throws SQLException {
        String toAllNames = clientsNames.getValue().toString();

        mysqlDatabase database = mysqlDatabase.getInstanceOfDatabase();
        int id = clientsNames.getSelectionModel().getSelectedIndex();
        clientik = client.get(id);
        menoClient.setText(clientik.getFname());
        priezviskoCleint.setText(clientik.getLname());
        emailClient.setText(clientik.getEmail());
        this.account = database.getAllAccounts(clientik.getId());

        accounts();
        clientsAccounts.getSelectionModel().select(0);


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


    //ACCOUNT

    public void accounts() throws SQLException{
        System.out.println("KLIKOL SOM NA CLIENTA");
        ObservableList<String> list = FXCollections.observableArrayList();
        clientsAccounts.getItems().clear();
        for (int i=0; i<this.account.size();i++){
            list.add(this.account.get(i).getAccountNumber());

        }
        clientsAccounts.setItems(list);

        if (selectedInxAcc == null){
            System.out.println("neni vybraty ziaden index");
            clientsAccounts.getSelectionModel().select(0);
        }

        System.out.println("STARY INDEX JE "+selectedInxAcc);
        selectedInxAcc = clientsAccounts.getSelectionModel().getSelectedIndex();
        System.out.println("NOVY INDEX JE "+selectedInxAcc);
        showAccountsInfo();

    }


    public void showAccountsInfo() throws SQLException {
        if (selectedInxAcc<0){
            selectedInxAcc++;
        }
        accNumber.setText(account.get(selectedInxAcc).getAccountNumber());
        System.out.println("selectedINDEX JEEEEE"+selectedInxAcc);
        money.setText(String.valueOf(account.get(selectedInxAcc).getAmount()));
    }

}









