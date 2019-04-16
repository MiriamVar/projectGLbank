package database;

import client.Account;
import client.Client;
import employee.employee;
import main.Globals;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class mysqlDatabase {

    private static mysqlDatabase database = new mysqlDatabase();

    private mysqlDatabase(){
    }

    public static mysqlDatabase getInstanceOfDatabase(){
        return database;
    }


    public Connection getConnection(){
        Connection connection;
        try {
            Class.forName(Globals.driver);
            System.out.println("Driver loaded!");
            connection = DriverManager.getConnection(Globals.url, Globals.user, Globals.password);
            return connection;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }


    static final String queryEmp1 = "select * from employee inner join loginemp on employee.id=loginemp.ide where login = ? and password = ?";

    public employee getEmployee(String login, String pass){
        Connection con = getConnection();
        ResultSet res;
        try {
            PreparedStatement stmnt = con.prepareStatement(queryEmp1);
            stmnt.setString(1,login);
            stmnt.setString(2,pass);
            res = stmnt.executeQuery();
            while (res.next()){
                int id = res.getInt("id");
                String fname = res.getString("fname");
                String lname = res.getString("lname");
                int position = res.getInt("position");

                employee employee = new employee(id,fname,lname,position);
                return employee;
            }
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    static final String queryClient = "select * from client";

    public List<Client> getAllClients(){
        Connection con = getConnection();
      //  System.out.println("geeeeet");
        ResultSet res;
        List<Client> clients = new ArrayList<>();
        try {
            PreparedStatement stmnt = con.prepareStatement(queryClient);
            res = stmnt.executeQuery();
            while (res.next()){
                int id =res.getInt("id");
                String name = res.getString("fname");
                String surname = res.getString("lname");
                String email = res.getString("email");
                Client client =  new Client(id, name, surname,email);
               clients.add(client);
            }
            return clients;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    static final String queryNewClient = "insert into client(fname,lname,email) values(?,?,?)";

    public void addNewClient(Client client){
        Connection conn = getConnection();
        System.out.println("vytvaram clienta");
        try{
            PreparedStatement statement = conn.prepareStatement(queryNewClient);
            statement.setString(1,client.getFname());
            statement.setString(2,client.getLname());
            statement.setString(3,client.getEmail());
            if(statement.execute()){
                System.out.println("client vytvorey");
            }else{
                System.out.println("client nevytvoreny");
            }
            conn.close();

        }  catch (SQLException e) {
            e.printStackTrace();
        }

    }


    static final String queryAcc = "select * from account where idc =?";

    public List<Account> getAllAccounts(int idClient){
        Connection con = getConnection();
        System.out.println("jaaaj");
        ResultSet res;
        List<Account> accounts = new ArrayList<>();
        try {
            PreparedStatement stmnt = con.prepareStatement(queryAcc);
            stmnt.setInt(1,idClient);
            res = stmnt.executeQuery();
            while (res.next()){
                    int id = res.getInt("id");
                    int idc = res.getInt("idc");
                    String accountNum = res.getString("accnum");
                    double amount = res.getDouble("amount");
                    Account account = new Account(id, idc, accountNum, amount);
                    accounts.add(account);

            }
            return accounts;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    static final String queryNewAccount = "insert into account(idc,accNum,amount) values(?,?,?)";

    public void addNewAccount(int idc, String number){
        Connection conn = getConnection();
        System.out.println("vytvaram novy account");
        try{
            PreparedStatement statement = conn.prepareStatement(queryNewAccount);
            statement.setInt(1,idc);
            statement.setString(2,number);
            statement.setDouble(3,0);
            if(statement.execute()){
                System.out.println("account nie je vytvorey");
            }else{
                System.out.println("account je vytvoreny");
            }
            conn.close();

        }  catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
