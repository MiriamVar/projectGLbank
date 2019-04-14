package client;

public class Account {
    private int id;
    private int idc;
    private String  accountNumber;
    private double amount;

    public Account(int id, int idc, String accountNumber, double amount) {
        this.id = id;
        this.idc = idc;
        this.accountNumber = accountNumber;
        this.amount = amount;
    }

    public Account( String accountNumber, double amount){
        this.accountNumber = accountNumber;
        this.amount = amount;
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
}
