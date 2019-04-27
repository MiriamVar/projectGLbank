package client;

import database.mysqlDatabase;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Account {
    private int id;
    private int idc;
    private String  accountNumber;
    private double amount;
    private List<Card> cards = new ArrayList<>();

    public Account(int id, int idc, String accountNumber, double amount) {
        this.id = id;
        this.idc = idc;
        this.accountNumber = accountNumber;
        this.amount = amount;
    }

    public Account( String accountNumber){
        this.accountNumber = accountNumber;
    }

    public int getId() {
        return id;
    }

    public int getIdc() {
        return idc;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getAmount() {
        return amount;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }


    public boolean addCard(){
        mysqlDatabase database = mysqlDatabase.getInstanceOfDatabase();
        LocalDate date = LocalDate.now();
        int month = date.getMonthValue();
        int year = (date.getYear()+3) % 100;
        System.out.println("mesiac "+month+" year "+year);
        String pin = generatingPIN();
            return database.addNewCard(this.id,pin,month,year);
        }

    public boolean loadCards(){
        mysqlDatabase database = mysqlDatabase.getInstanceOfDatabase();
        List<Card> swapCards = database.getAllCards(this.id);
        if (swapCards == null || swapCards.size() == 0){
            return false;
        }
        else{
            cards = swapCards;
            return true;
        }
    }

    public int getCountOfCards(){
        return this.cards.size();
    }

    public Card getCard(int id){
        return this.cards.get(id);
    }


    public static String generatingPIN(){
        Random random = new Random();
        String pinNumber ="";
        for (int i=0;i<4;i++){
            pinNumber = pinNumber + random.nextInt(10);
        }

        System.out.println("pin "+pinNumber);
        return pinNumber;
    }

}
