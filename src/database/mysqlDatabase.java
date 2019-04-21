package database;

import client.Account;
import client.Card;
import client.Client;
import employee.Employee;
import main.Globals;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class mysqlDatabase {

    private static mysqlDatabase database;

    private mysqlDatabase(){
    }

    public static mysqlDatabase getInstanceOfDatabase(){
        if (database == null){
            database = new mysqlDatabase();
        }
        return database;
    }


    public Connection getConnection(){
        Connection connection;
        try {
            Class.forName(Globals.driver);
            System.out.println("Driver loaded!");
            connection = DriverManager.getConnection(Globals.url, Globals.user, Globals.password);
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static final String queryEmp1 = "select * from Employee inner join loginemp on Employee.id=loginemp.ide where login = ? and password = ?";

    public Employee getEmployee(String login, String pass){
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

                con.close();
                return new Employee(id,fname,lname,position);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    private static final String queryClient = "select * from client";

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
            con.close();
            return clients;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final String queryNewClient = "insert into client(fname,lname,email) values(?,?,?)";

    public boolean addNewClient(Client client){
        Connection conn = getConnection();
        System.out.println("vytvaram clienta");
        try{
            PreparedStatement statement = conn.prepareStatement(queryNewClient);
            statement.setString(1,client.getFname());
            statement.setString(2,client.getLname());
            statement.setString(3,client.getEmail());
            if(statement.execute()){
                conn.close();
                System.out.println("client vytvorey");
                return true;
            }else{
                conn.close();
                System.out.println("client nevytvoreny");
                return false;
            }
        }  catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    private static final String queryAcc = "select * from account where idc =?";

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
            con.close();
            return accounts;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final String queryNewAccount = "insert into account(idc,accNum,amount) values(?,?,?)";

    public boolean addNewAccount(int idc, String number){
        Connection conn = getConnection();
        System.out.println("vytvaram novy account v databaze");
        try{
            PreparedStatement statement = conn.prepareStatement(queryNewAccount);
            statement.setInt(1,idc);
            statement.setString(2,number);
            statement.setDouble(3,0);
            if(statement.execute()){
                conn.close();
                System.out.println("account nie je vytvorey");
                return false;
            }else{
                conn.close();
                System.out.println("account je vytvoreny");
                return true;
            }

        }  catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static final String queryCard = "select * from card where ida =?";

    public List<Card> getAllCards(int idAcc){
        Connection con = getConnection();
        System.out.println("beeee");
        ResultSet res;
        List<Card> cards = new ArrayList<>();
        try {
            PreparedStatement stmnt = con.prepareStatement(queryCard);
            stmnt.setInt(1,idAcc);
            res = stmnt.executeQuery();
            while (res.next()){
                int id = res.getInt("id");
                int ida = res.getInt("ida");
                String pin = res.getString("pin");
                boolean active = res.getBoolean("active");
                int expireM = res.getInt("expireM");
                int expireY = res.getInt("expireY");
                Card card = new Card(id, ida,pin,active,expireM,expireY);
                cards.add(card);

            }
            con.close();
            return cards;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
