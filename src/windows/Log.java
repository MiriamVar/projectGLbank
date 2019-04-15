package windows;


import client.Client;
import database.mysqlDatabase;
import employee.employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
    public Label anyAccount;

    mysqlDatabase database = mysqlDatabase.getInstanceOfDatabase();
    private List<Client> client;
    private List<Account> account;
    public Client clientik;
    private Integer selectedInxAcc;


    public void initialize () throws SQLException {
        this.client = database.getAllClients();
        clients();
        showClientsInfo();
    }

    //vytvori zozanm clintov a naplni dropdown
    public void clients() throws SQLException{
        ObservableList<String> list = FXCollections.observableArrayList();

        for (int i=0; i<this.client.size();i++){
            list.add(this.client.get(i).getFname()+" "+this.client.get(i).getLname());
        }
        clientsNames.setItems(list);
        System.out.println("naplni sa dropdown - clients");
        clientsNames.getSelectionModel().select(0);
        System.out.println("nastavi sa prvy client aby ho bolo vidno");
        clientik=client.get(0);
        System.out.println("id prveho clienta"+clientik);
        showClientsInfo(clientik.getId());
        System.out.println("vola sa metoda na ukazanie prveho usera");

    }

    //vypise info hned o prvom useri
    public void showClientsInfo(int id) throws SQLException {
        System.out.println("vypisanie 1. usera");
        menoClient.setText(clientik.getFname());
        priezviskoCleint.setText(clientik.getLname());
        emailClient.setText(clientik.getEmail());

    }

    //naplni labely s info o useroch
    public void showClientsInfo() throws SQLException {
        int id = clientsNames.getSelectionModel().getSelectedIndex();
        System.out.println("id vybraneho usera "+ id);
        clientik = client.get(id);
        System.out.println("podla vybraneho usera sa vypisuje info o nom");
        menoClient.setText(clientik.getFname());
        priezviskoCleint.setText(clientik.getLname());
        emailClient.setText(clientik.getEmail());

        this.account = database.getAllAccounts(clientik.getId());
        System.out.println("clientikovo id "+clientik.getId());
        System.out.println("nastavenie accountu podla vybraneho usera");

        accounts();
        System.out.println("vola sa funkcia ktora naplni list accountov podla usera");
        clientsAccounts.getSelectionModel().select(0);
        System.out.println("esteticky aby to odbre vyzeralo");

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
        System.out.println("velkost accounts listu je:"+ account.size());
        ObservableList<String> list = FXCollections.observableArrayList();
        for (int i=0; i<this.account.size();i++){
            list.add(this.account.get(i).getAccountNumber());
        }
        clientsAccounts.setItems(list);
        System.out.println("list accountov je naplneny");


        if (selectedInxAcc == null){
            System.out.println("neni vybraty ziaden index accountu");
            selectedInxAcc =1;
            clientsAccounts.getSelectionModel().select(0);
        }

        System.out.println("STARY INDEX JE "+selectedInxAcc);
        showAccountsInfo();
        System.out.println("spusta sa funkcia na vypisanie account info");
    }


    public void showAccountsInfo() throws SQLException {
        anyAccount.setText("");
        selectedInxAcc = clientsAccounts.getSelectionModel().getSelectedIndex();
        System.out.println("NOVY INDEX JE o funkcii showAccounts info  "+selectedInxAcc);
        if (selectedInxAcc<0){
            anyAccount.setText("Any account");
            accNumber.setText("");
            money.setText("");
            return;
        }
        accNumber.setText(account.get(selectedInxAcc).getAccountNumber());
        System.out.println("nastavuje sa cislo uctu do labelu");
        System.out.println("selectedINDEX JEEEEE "+selectedInxAcc);
        money.setText(String.valueOf(account.get(selectedInxAcc).getAmount()));
        System.out.println("nastavuju s apeniaze z uctu");
    }

}