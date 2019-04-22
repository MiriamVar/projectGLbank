package client;

import database.mysqlDatabase;

import java.util.ArrayList;
import java.util.List;

public class Client {
    private int id;
    private String fname;
    private String lname;
    private String email;
    private List<Account> accounts = new ArrayList<>();


    public Client(int id, String fname, String lname, String email) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
    }
   public Client (String fname, String lname){
       this.fname = fname;
       this.lname = lname;
   }

    public Client( String fname, String lname, String email) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
    }


    public int getId() {
        return id;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getEmail() { return email;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public boolean addAccount(String accNum){
        mysqlDatabase database = mysqlDatabase.getInstanceOfDatabase();
        return database.addNewAccount(this.id,accNum);
    }

    public boolean loadAccounts(){
        mysqlDatabase database = mysqlDatabase.getInstanceOfDatabase();
        List<Account> swapAccounts = database.getAllAccounts(this.id);
        if (swapAccounts == null || swapAccounts.size() == 0){
            return false;
        }else {
           accounts = swapAccounts;
           return true;
        }
    }

    public int countOfAccounts(){
        return this.accounts.size();
    }

    public Account getAccount(int id){
        return this.accounts.get(id);
    }


}
