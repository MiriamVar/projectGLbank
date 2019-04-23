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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
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
    public Label lblLogin;
    public Label lblPass;
    public Button btnResetPass;
    public CheckBox checkBoxBlock;
    public Button btnNewCard;
    public Label lblMessNewCArd;
    public Label lblMessChangePin;

    private mysqlDatabase database = mysqlDatabase.getInstanceOfDatabase();
    private List<Client> all_clients;
    private Client actual_clientik;
    private Account actual_accountik;
    private Card actual_card;

    public Log(){
    }


    public void initialize () throws SQLException {
        this.all_clients = database.getAllClients();
        loadAllClients();

    }


    //vytvori zozanm clientov a naplni dropdown
    public void loadAllClients() throws SQLException{
        if(all_clients.size() == 0){
            //vipis ze nie je ziaden klient
            actual_clientik = null;
            return;
        }

        ObservableList<String> list = FXCollections.observableArrayList();

        for (int i=0; i<this.all_clients.size();i++){
            list.add(this.all_clients.get(i).getFname()+" "+this.all_clients.get(i).getLname());
        }
        comBoxClientsNames.setItems(list);
        comBoxClientsNames.getSelectionModel().select(0);
        actual_clientik=all_clients.get(0);
        showClientsInfo();
        actual_clientik.loadAccounts();
        loadClientAccounts();
    }

    //vypise info o useroch
    public void showClientsInfo() {
        if (actual_clientik == null){
            return;
        }
        lblClientName.setText(actual_clientik.getFname());
        lblClientSurname.setText(actual_clientik.getLname());
        lblClientEmail.setText(actual_clientik.getEmail());
    }

    //konkretny client
    public void selectClient() {
        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("");
        comBoxClientAccounts.setItems(list);
        comBoxClientCards.setItems(list);
        actual_clientik = null;
        actual_accountik = null;
        actual_card = null;

        if (all_clients.size() == 0){
            return;
        }
        int id = comBoxClientsNames.getSelectionModel().getSelectedIndex();
        actual_clientik = all_clients.get(id);
        System.out.println("id vybraneho usera "+ id);
        actual_clientik.loadAccounts();
        showClientsInfo();
        loadClientAccounts();

    }

    //vypisuje data o zamestnancovi
     void showData(Employee emp) {
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

    //vytvaranie noveho clienta
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
    //vypisuje accounty clienta
    private void loadClientAccounts() {
        if(this.actual_clientik == null || this.actual_clientik.countOfAccounts() ==0){
            System.out.println("load client account spadol");
            return;
        }
        updateAccounts();
        actual_accountik = actual_clientik.getAccount(0);
        comBoxClientAccounts.getSelectionModel().select(0);
        showAccountsInfo();
        actual_accountik.loadCards();
        loadCards();
    }

    //zobrazi vsetky accounty
    private void updateAccounts(){
        ObservableList<String> list2 = FXCollections.observableArrayList();
        for (Account swap : this.actual_clientik.getAccounts()){
            list2.add(swap.getAccountNumber());
        }
        comBoxClientAccounts.setItems(list2);
    }

    //vypise info o accounte
    public void showAccountsInfo() {
        lblAnyAccount.setText("");
        lblAccNumber.setText(this.actual_accountik.getAccountNumber());
        lblMoney.setText(String.valueOf(this.actual_accountik.getAmount()));
    }

    //pouzivany acccount
    public void selectAccount(){
        if (actual_clientik == null || actual_clientik.countOfAccounts() == 0){
            lblAnyAccount.setText("Any account");
            lblAccNumber.setText("");
            lblMoney.setText("");
            System.out.println("select account - osetrenie" + actual_clientik);
            return;
        }
        int selected  = comBoxClientAccounts.getSelectionModel().getSelectedIndex();
        if (selected<0){
            lblAccNumber.setText("");
            lblMoney.setText("");
            return;
        }
        actual_accountik = actual_clientik.getAccount(selected);
        showAccountsInfo();
        actual_accountik.loadCards();
        loadCards();
        lblAnyAccount.setText("");
    }

    //vytvaranie cisla uctu
    private String generetatingAccoutnNumber(){
        Random random = new Random();
        String number ="";
        for (int i=0;i<10;i++){
            number = number + random.nextInt(10);
        }

        System.out.println("desatmiestne sislo "+number);

        if (database.accNumberExist(number)){
            return "";
        }
        else{
            return number;
        }
    }


    public void createNewAccount() {
       String numAcc =  generetatingAccoutnNumber();
        if (numAcc.equals("")){
            return;
        }
        if(!this.actual_clientik.addAccount(numAcc)){
            lblAccountCreated.setText("Account not created");
        }
        actual_clientik.loadAccounts();
        loadClientAccounts();
    }


    //CARD
    public void loadCards() {
        if(actual_accountik ==null || actual_accountik.getCountOfCards() == 0){
            return;
        }
        updateCards();
        actual_card =actual_accountik.getCard(0);
        comBoxClientCards.getSelectionModel().select(0);
        showCardInfo();
    }

    private void updateCards(){
        ObservableList<String> list3 = FXCollections.observableArrayList();
        for (Card swap : actual_accountik.getCards()){
            list3.add(String.valueOf(swap.getId()));
        }
        comBoxClientCards.setItems(list3);
    }

    private void showCardInfo(){
        lblPin.setText(this.actual_card.getPin());
        lblActive.setText(String.valueOf(this.actual_card.isActive()));
        lblExpireDate.setText(this.actual_card.getExpireM()+"/"+this.actual_card.getExpireY());
        lblAccNum.setText(String.valueOf(this.actual_card.getIda()));
    }

    public void selectCard(){
        lblPin.setText("");
        lblActive.setText("");
        lblAccNum.setText("");
        lblExpireDate.setText("");

        if(actual_accountik == null || this.actual_accountik.getCountOfCards() ==0){
            return;
        }
        int selected = comBoxClientCards.getSelectionModel().getSelectedIndex();
        if (selected<0){
            lblAccNumber.setText("");
            lblMoney.setText("");
            return;
        }
        actual_card = actual_accountik.getCard(selected);
        showCardInfo();


    }

    public void createNewCard() {
        if(!this.actual_accountik.addCard()){
           lblMessNewCArd.setText("Cand not created");
        }
        lblMessNewCArd.setText("Card created");
        actual_accountik.loadCards();
        updateCards();
        loadCards();
    }

    public void changePin(){
        //urobit
    }

    public static String generatingLogin(){

        Random random = new Random();
        String userName ="";
        for (int i=0;i<7;i++){
            userName = userName + random.nextInt(10);
        }

        System.out.println("login "+userName);
        mysqlDatabase database = mysqlDatabase.getInstanceOfDatabase();
        if (database.loginClientExist(userName)){
            return "";
        }
        else{
            return userName;
        }
    }


    public static String generatingPass(){
        Random random = new Random();
        String userPassowrd ="";
        for (int i=0;i<7;i++){
            userPassowrd = userPassowrd + random.nextInt(26)+65;
        }

        System.out.println("passowrd "+userPassowrd);
        return userPassowrd;
    }

}