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
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import client.Account;
import client.Card;
import client.LoginHistory;

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
    public Button btnResetPass;
    public CheckBox checkBoxBlock;
    public Button btnNewCard;
    public Label lblMessNewCArd;
    public Label lblMessChangePin;
    public CheckBox checkBoxBlockCard;
    public ComboBox comBoxTransAccounts;
    public TextField textFieldWithdraw;
    public TextField textFieldDeposit;
    public Button btnWithdraw;
    public Button btnDeposit;
    public Label lblMessWithdraw;
    public Label lblMessDeposit;

    private mysqlDatabase database = mysqlDatabase.getInstanceOfDatabase();
    private List<Client> all_clients;
    private Client actual_clientik;
    private Account actual_accountik;
    private Card actual_card;
    private Employee emp;


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
        showIBinfo();
        setBlockIB();
    }

    //vypisuje data o zamestnancovi
     void showData(Employee emp) {
        lblEmpName.setText(emp.getFname());
        lblEmpSurname.setText(emp.getLname());
        this.emp= emp;
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
        comBoxTransAccounts.setItems(list2);
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
//            selected = 0;
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
            clearCardInfo();
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

    private void clearCardInfo(){
        ObservableList<String> list3 = FXCollections.observableArrayList();
        comBoxClientCards.setItems(list3);
        checkBoxBlockCard.setSelected(false);
        lblPin.setText("");
        lblActive.setText("");
        lblAccNum.setText("");
        lblExpireDate.setText("");
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
            lblPin.setText("");
            lblActive.setText("");
            lblAccNum.setText("");
            lblExpireDate.setText("");
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

    public void changePIN(){
        String newPIN = Account.generatingPIN();
        int id = actual_card.getId();
        database.changePin(newPIN,id);
        lblPin.setText(newPIN);
    }


    public void blockingCard(){
        boolean block = actual_card.isActive();
        int id = actual_card.getId();
        if(block){
            database.blockCard(id); // zablokuje kartu
            actual_card.setActive(false);
            checkBoxBlockCard.setSelected(true);
            lblActive.setText("false");// zaskrtne
        }else{
            database.unBlockCard(id);
            actual_card.setActive(true);
            checkBoxBlockCard.setSelected(false);
            lblActive.setText("true");
        }
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

    private void showIBinfo(){
        int id = actual_clientik.getId();
        String userName = database.userNameLoginClient(id);
        lblLogin.setText(userName);
    }


    public void btnResetPass(ActionEvent actionEvent) {
        int id = actual_clientik.getId();
        database.resetIBPass(id);
    }

    // zaskrtne tlacitko block a zablokuje si client sam ucet
    public void setBlockIB() {
        int id = actual_clientik.getId();
        List<LoginHistory> threeLastRecords =database.getThreeLastRecords(id);
        int blocks = 0;
        for(int i =0; i<threeLastRecords.size();i++){
            if ( i == 0 && threeLastRecords.get(i).isSuccess() == null){
                checkBoxBlock.setSelected(true);
            } else if ( i == 0 && threeLastRecords.get(i).isSuccess().equals("1")){
                checkBoxBlock.setSelected(false);
            } else if (threeLastRecords.get(i).isSuccess() == null){
                //NIC lebo by bol bloknuty a nevedel nic robit bez odblokovania takze logicky uz medzi tymi troma vysledkami neboli 3 nespravne prihlasenia
            } else if (threeLastRecords.get(i).isSuccess().equals("1")) {
                // NIC
            } else {
                blocks++;
            }
        }
        if (blocks == 3){
            checkBoxBlock.setSelected(true);
        } else {
            checkBoxBlock.setSelected(false);
        }
        if (!database.isIBblock(id)){
            checkBoxBlock.setSelected(true);
        }else{
            checkBoxBlock.setSelected(false);
        }
    }

    public void blockByEmp(ActionEvent actionEvent) {
        int id = actual_clientik.getId();
        if(!checkBoxBlock.isSelected()){
            database.unblockByEmp(id);
        }
        else{
            database.blockByEmp(id);
        }
    }

    public void selectAccountTran(ActionEvent actionEvent) {
        System.out.println("tu su accoutny");
        if (actual_clientik == null || actual_clientik.countOfAccounts() == 0){
            System.out.println("select account - osetrenie" + actual_clientik);
            return;
        }
        int selected  = comBoxTransAccounts.getSelectionModel().getSelectedIndex();
        if (selected<0){
            return;
        }
        actual_accountik = actual_clientik.getAccount(selected);
        showAccountsInfo();
        actual_accountik.loadCards();
        loadCards();
    }

    public void withdrawMoney(ActionEvent actionEvent) {
        double number = Double.parseDouble(textFieldWithdraw.getText());
        String accNum = actual_accountik.getAccountNumber();
        double actualAmountOfMoney = actual_accountik.getAmount();
        int id = actual_accountik.getId();

        if((actualAmountOfMoney-number) < 0){
            lblMessWithdraw.setText("You don't have enough money.");
        }else{
            database.sendingMoney(accNum,number);
            database.makeingTransaction(id, emp.getId(), accNum,number);
            textFieldWithdraw.setText("");
            lblMessWithdraw.setText("Money was sent.");
        }
    }

    public void depositMoney(ActionEvent actionEvent) {
        double number = Double.parseDouble(textFieldDeposit.getText());
        String accNum = actual_accountik.getAccountNumber();
        int id = actual_accountik.getId();
        database.gettingMoney(accNum,number);
        database.makeingTransaction(id, emp.getId(), accNum,number);
        textFieldDeposit.setText("");
        lblMessDeposit.setText("Money was get.");
    }


}