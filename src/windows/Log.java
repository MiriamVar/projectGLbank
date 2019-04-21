package windows;


import client.Client;
import database.mysqlDatabase;
import employee.Employee;
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
import java.util.Random;

import client.Account;
import client.Card;

//add anotations to variables @FML

public class Log<client> {
    public Label lblEmpName;
    public Label lblEmpSurname;

    public Button lblEmpLogOut;
    public Stage dialogStage;

    public ComboBox comBoxClientsNames;
    public Button btnAddNewClient;
    public Label lblClientName;
    public Label lblClientSurname;
    public Label lblClientEmail;

    public ComboBox comBoxClientAccounts;
    public Label lblAccNumber;
    public Label lblMoney;
    public Label lblAnyAccount;
    public Label lblAccountCreated;
    public Label lblPin;
    public Label lblActive;
    public Label lblExpireDate;
    public Label lblAccNum;
    public ComboBox comBoxClientCards;
    public Button btnNewAccount;

    private mysqlDatabase database = mysqlDatabase.getInstanceOfDatabase();
    private List<Client> all_clients;
    private List<Account> account;
    private Client actual_clientik;
    private Account actual_accountik;
    private Integer selectedInxAcc;
    private List<Card> card;


    public void initialize () throws SQLException {
        this.all_clients = database.getAllClients();
        clients();
        showClientsInfo();
    }

    //vytvori zozanm clientov a naplni dropdown
    public void clients() throws SQLException{
        ObservableList<String> list = FXCollections.observableArrayList();

        for (int i=0; i<this.all_clients.size();i++){
            list.add(this.all_clients.get(i).getFname()+" "+this.all_clients.get(i).getLname());
        }
        comBoxClientsNames.setItems(list);
        System.out.println("naplni sa dropdown - clients");
        comBoxClientsNames.getSelectionModel().select(0);
        System.out.println("nastavi sa prvy client aby ho bolo vidno");
        actual_clientik=all_clients.get(0);
        System.out.println("id prveho clienta"+actual_clientik);
        showClientsInfo(actual_clientik.getId());
        System.out.println("vola sa metoda na ukazanie prveho usera");

    }

    //vypise info hned o prvom useri
    public void showClientsInfo(int id) throws SQLException {
        System.out.println("vypisanie 1. usera");
        lblClientName.setText(actual_clientik.getFname());
        lblClientSurname.setText(actual_clientik.getLname());
        lblClientEmail.setText(actual_clientik.getEmail());

    }

    //naplni labely s info o useroch
    public void showClientsInfo() throws SQLException {
        int id = comBoxClientsNames.getSelectionModel().getSelectedIndex();
        System.out.println("id vybraneho usera "+ id);
        actual_clientik = all_clients.get(id);
        System.out.println("podla vybraneho usera sa vypisuje info o nom");
        lblClientName.setText(actual_clientik.getFname());
        lblClientSurname.setText(actual_clientik.getLname());
        lblClientEmail.setText(actual_clientik.getEmail());

        this.account = database.getAllAccounts(actual_clientik.getId());
        System.out.println("clientikovo id "+actual_clientik.getId());
        System.out.println("nastavenie accountu podla vybraneho usera");

        accounts();
        System.out.println("vola sa funkcia ktora naplni list accountov podla usera");
        comBoxClientAccounts.getSelectionModel().select(0);
        System.out.println("esteticky aby to odbre vyzeralo");

    }

    public void showData(Employee emp) {
        lblEmpName.setText(emp.getFname());
        lblEmpSurname.setText(emp.getLname());
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
        ObservableList<String> list2 = FXCollections.observableArrayList();
        for (int i=0; i<this.account.size();i++){
            list2.add(this.account.get(i).getAccountNumber());
        }
        comBoxClientAccounts.setItems(list2);
        System.out.println("list accountov je naplneny");


        if (selectedInxAcc == null){
            System.out.println("neni vybraty ziaden index accountu");
            selectedInxAcc =1;
            comBoxClientAccounts.getSelectionModel().select(0);
        }

        System.out.println("STARY INDEX JE "+selectedInxAcc);
        showAccountsInfo();
        System.out.println("spusta sa funkcia na vypisanie account info");

    }


    public void showAccountsInfo() throws SQLException {
        lblAnyAccount.setText("");
        selectedInxAcc = comBoxClientAccounts.getSelectionModel().getSelectedIndex();
        System.out.println("NOVY INDEX JE o funkcii showAccounts info  "+selectedInxAcc);
        if (selectedInxAcc<0){
            lblAnyAccount.setText("Any account");
            lblAccNumber.setText("");
            lblMoney.setText("");
            return;
        }
        lblAccNumber.setText(account.get(selectedInxAcc).getAccountNumber());
        System.out.println("nastavuje sa cislo uctu do labelu");
        System.out.println("selectedINDEX JEEEEE "+selectedInxAcc);
        lblMoney.setText(String.valueOf(account.get(selectedInxAcc).getAmount()));
        System.out.println("nastavuju s apeniaze z uctu");

        if (selectedInxAcc == null){
            System.out.println("neni vybraty ziaden index accountu");
            selectedInxAcc =1;
            comBoxClientCards.getSelectionModel().select(0);
        }

        this.card = database.getAllCards(selectedInxAcc);
        System.out.println("account id "+selectedInxAcc);
        System.out.println("nastavenie card podla vybraneho accountu");

        cards();
        System.out.println("vola sa funkcia ktora naplni list kart podla accountu");
        comBoxClientCards.getSelectionModel().select(0);
        System.out.println("esteticky aby to dobre vyzeralo");
    }

    public String generetatingAccoutnNumber(){
        Random random = new Random();
        String number ="";
        for (int i=0;i<10;i++){
            number = number + random.nextInt(10);
        }

        System.out.println("desatmiestne sislo "+number);

        for (int i=0;i < this.account.size(); i++){
            if (account.get(i).getAccountNumber().equals(number)){
                System.out.println("Taky account uz existuje");
            }
            else {
                System.out.println("vytvaram novy account .. cislo uz mam");
                return number;
            }
        }
        return number;
    }

    public void createNewAccount() throws SQLException {
       String numAcc =  generetatingAccoutnNumber();
       database.addNewAccount(actual_clientik.getId(), numAcc);
       this.account.add(new Account(numAcc));
       accounts();
    }


    //CARD
    public void cards() throws SQLException{
        System.out.println("KLIKOL SOM NA KARTU");
        System.out.println("velkost cards listu je:"+ card.size());
        ObservableList<String> list3 = FXCollections.observableArrayList();
        for (int i=0; i<this.card.size();i++){
            list3.add(String.valueOf(this.card.get(i).getId()));
        }
        comBoxClientCards.setItems(list3);
        System.out.println("list cards je naplneny");



    }

}