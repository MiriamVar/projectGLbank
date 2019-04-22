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

    private mysqlDatabase database = mysqlDatabase.getInstanceOfDatabase();
    private List<Client> all_clients;
  //  private List<Account> account;
    private Client actual_clientik;
    private Account actual_accountik;
    private Integer selectedInxAcc;
    private Card actual_card;
   // private List<Card> card;

    public Log(){
    }


    public void initialize () throws SQLException {
        this.all_clients = database.getAllClients();
        loadAllClients();
        selectClient();
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


    }

    //vypise info o useroch
    public void showClientsInfo() throws SQLException {
        if (actual_clientik == null){
            return;
        }
        lblClientName.setText(actual_clientik.getFname());
        lblClientSurname.setText(actual_clientik.getLname());
        lblClientEmail.setText(actual_clientik.getEmail());
    }

    //konkretny client
    private void selectClient() {
        if (all_clients.size() == 0){
            return;
        }
        int id = comBoxClientsNames.getSelectionModel().getSelectedIndex();
        actual_clientik = all_clients.get(id);
        System.out.println("id vybraneho usera "+ id);
        try {
            loadClientAccounts();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        comBoxClientAccounts.getSelectionModel().select(0);
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
    private void loadClientAccounts() throws SQLException{
        if(this.actual_clientik == null || this.actual_clientik.countOfAccounts() ==0){
            return;
        }
        updateAccounts();
        actual_accountik = actual_clientik.getAccount(0);
        comBoxClientAccounts.getSelectionModel().select(0);
        actual_accountik.loadCards();
        showAccountsInfo();
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
    public void showAccountsInfo() throws SQLException {
        lblAnyAccount.setText("");
        lblAccNum.setText(this.actual_accountik.getAccountNumber());
        lblMoney.setText(String.valueOf(this.actual_accountik.getAmount()));
    }

    //pouzivany acccount
    public void selectAccount(){
        if (actual_clientik == null || actual_clientik.countOfAccounts() == 0){
            return;
        }
        int selected  = comBoxClientAccounts.getSelectionModel().getSelectedIndex();
//        if (selectedInxAcc<0){
//            lblAnyAccount.setText("Any account");
//            lblAccNumber.setText("");
//            lblMoney.setText("");
//            return;
//        }
        actual_accountik = actual_clientik.getAccount(selected);
        try {
            loadCards();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        actual_accountik.loadCards();
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


    public void createNewAccount() throws SQLException {
       String numAcc =  generetatingAccoutnNumber();
        if(!this.actual_clientik.addAccount(numAcc)){
            lblAccountCreated.setText("Account not created");
        }
       loadClientAccounts();
    }


    //CARD
    public void loadCards() throws SQLException{
        if(actual_accountik ==null || actual_accountik.getCountOfCards() == 0){
            return;
        }
        updateCards();
        actual_card =actual_accountik.getCard(0);
        comBoxClientCards.getSelectionModel().select(0);
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
        if(actual_accountik == null || this.actual_accountik.getCountOfCards() ==0){
            return;
        }
        int selected = comBoxClientCards.getSelectionModel().getSelectedIndex();
        actual_card = actual_accountik.getCard(selected);
        showCardInfo();

    }

    private String generatingPIN(){
        Random random = new Random();
        String pinNumber ="";
        for (int i=0;i<4;i++){
            pinNumber = pinNumber + random.nextInt(10);
        }

        System.out.println("pin "+pinNumber);
        return pinNumber;
    }

    //dokoncit
    private int geneteratingMonth(){
        LocalDate date = LocalDate.now();
        String txtDate = String.valueOf(LocalDate.from(date));
        System.out.println("Datum "+txtDate);
        return txtDate;
    }

    //dokoncit
    private int geneteratingYear(){
        LocalDate date = LocalDate.now();
        String txtDate = String.valueOf(LocalDate.from(date));
        System.out.println("Datum "+txtDate);
        return txtDate;
    }

    public void createNewCard() throws SQLException {
        String pin =  generatingPIN();
        int month = geneteratingMonth();
        int year = geneteratingYear();
        if(!this.actual_accountik.addCard(pin,month,year)){
           //vypis ked sa karta nevytvori
        }
        loadCards();
    }



}