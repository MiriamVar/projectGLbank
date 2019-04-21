package client;

import java.util.ArrayList;
import java.util.List;

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
        return false;
    }
}
