package database;

import client.*;
import employee.Employee;
import main.Globals;
import windows.Log;

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

    public int addNewClient(Client client){
        Connection conn = getConnection();
        System.out.println("vytvaram clienta");
        int ret =-1;
        try{
            PreparedStatement statement = conn.prepareStatement(queryNewClient, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1,client.getFname());
            statement.setString(2,client.getLname());
            statement.setString(3,client.getEmail());

            if(statement.execute()){
                System.out.println("client nevytvoreny");
                conn.close();
                return ret;
            }else{
                System.out.println("client vytvoreny");
                ResultSet res123 = statement.getGeneratedKeys();
                if (res123.next()){
                    ret = res123.getInt(1);
                    System.out.println("NOOOOOOVE ID JE: "+ret);
                    return ret;
                }
                conn.close();
                return ret;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
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

    private static final String queryNewCard = "insert into card(ida,pin,active,expireM,expireY) values(?,?,?,?,?)";

    public boolean addNewCard(int ida,String pin, int month, int year){
        Connection conn = getConnection();
        System.out.println("vytvaram novu kartu v databaze");
        try{
            PreparedStatement statement = conn.prepareStatement(queryNewCard);
            statement.setInt(1,ida);
            statement.setString(2,pin);
            statement.setBoolean(3,true);
            statement.setInt(4,month);
            statement.setInt(5,year);
            if(statement.execute()){
                conn.close();
                System.out.println("karta nie je vytvorena");
                return false;
            }else{
                conn.close();
                System.out.println("karta je vytvorena");
                return true;
            }

        }  catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    static final String queryAccountNumber = "select count(id) as countId from account where accnum = ?";

    public boolean accNumberExist(String accNum){
        Connection conn = getConnection();
        ResultSet res;
        try {
            PreparedStatement stmnt = conn.prepareStatement(queryAccountNumber);
            stmnt.setString(1,accNum);
            res = stmnt.executeQuery();
            res.next();
            int count = res.getInt("countId");
            conn.close();
            if (count == 0){
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    static final String queryLoginClient = "select count(login) as countLogin from loginclient where login = ?";

    public boolean loginClientExist(String login){
        Connection conn = getConnection();
        ResultSet res;
        try {
            PreparedStatement stmnt = conn.prepareStatement(queryLoginClient);
            stmnt.setString(1,login);
            res = stmnt.executeQuery();
            res.next();
            int count = res.getInt("countLogin");
            conn.close();
            if (count == 0){
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    private static final String queryNewLoginClient = "insert into loginclient(idc,login,password) values(?,?,?)";

    public boolean addNewLoginClient(int clientID){
        Connection conn = getConnection();
        System.out.println("vytvaram login clienta");
        String login;
        String password;
        try{
            PreparedStatement statement = conn.prepareStatement(queryNewLoginClient);
            statement.setInt(1,clientID);
            login = Log.generatingLogin();
            password =Log.generatingPass();
            if (login.equals("")){
                System.out.println("login je prazdny");
                return false;
            }
            statement.setString(2,login);
            statement.setString(3,password);
            if(statement.execute()){
                conn.close();
                System.out.println("login client vytvorey");
                return true;
            }else{
                conn.close();
                System.out.println("login client nevytvoreny");
                return false;
            }
        }  catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static final String queryLoginClientLbl = "select login from loginclient where idc = ?";

    public String userNameLoginClient(int clientID){
        Connection conn = getConnection();
        ResultSet res;
        String login = null;
        try {
            PreparedStatement stmnt = conn.prepareStatement(queryLoginClientLbl);
            stmnt.setInt(1,clientID);
            res = stmnt.executeQuery();
            res.next();
            login = res.getString("login");
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return login;
    }

    static final String queryResetPass = "update loginclient set password = ? where idc = ?";

    public String resetIBPass(int clientID) {
        Connection con = getConnection();
        String newPass = Log.generatingPass();
        try {
            PreparedStatement statement = con.prepareStatement(queryResetPass);
            statement.setString(1,newPass);
            statement.setInt(2,clientID);
            statement.executeUpdate();
            return newPass;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newPass;
    }

    private static final String queryClientLogin = "select * from loginclient where idc =?";

    public List<LoginClient> getAllLogins(int idClient){
        Connection con = getConnection();
        System.out.println("hello you there");
        ResultSet res;
        List<LoginClient> logins = new ArrayList<>();
        try {
            PreparedStatement stmnt = con.prepareStatement(queryClientLogin);
            stmnt.setInt(1,idClient);
            res = stmnt.executeQuery();
            while (res.next()){
                int id = res.getInt("id");
                int idc = res.getInt("idc");
                String login = res.getString("login");
                String pass = res.getString("password");
                LoginClient loginClient = new LoginClient(id, idc, login,pass);
                logins.add(loginClient);
            }
            con.close();
            return logins;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logins;
    }

    //posledne tri zaznamy
    private static final String queryLastrecords = "select * from loginhistory where idl = (select id from loginclient where idc = ?)order by UNIX_TIMESTAMP(logDate) desc limit 3";

    public List<LoginHistory> getThreeLastRecords(int idClient){
        Connection con = getConnection();
        System.out.println("vytahujem posledne tri zaznamy");
        ResultSet res;
        List<LoginHistory> loginHistories = new ArrayList<>();
        try {
            PreparedStatement stmnt = con.prepareStatement(queryLastrecords);
            stmnt.setInt(1,idClient);
            res = stmnt.executeQuery();
            while (res.next()){
                int id = res.getInt("id");
                int idl = res.getInt("idl");
                String success = res.getString("success");
                System.out.println("success je "+success);
                Date dateLog = res.getDate("logDate");
                LoginHistory record = new LoginHistory(id, idl,success,dateLog);
                loginHistories.add(record);
            }
            con.close();
            return loginHistories;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loginHistories;
    }


    //zablokovanie zamestnamcom
    private static final String queryBlockByEmp = "insert into loginhistory(idl) select id from loginclient where idc = ?";
    //idl poriesit
    public void blockByEmp(int idClient){
        Connection con = getConnection();
        System.out.println("blokujem IB ako bankar");
        try {
            PreparedStatement statement = con.prepareStatement(queryBlockByEmp);
            statement.setInt(1, idClient);
            System.out.println(idClient);
            statement.execute();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //posledny zaznam
    private static final String queryLastrecord = "select * from loginhistory where idl = (select id from loginclient where idc = ?)order by UNIX_TIMESTAMP(logDate) desc limit 1";

    public boolean isIBblock(int Client){
        Connection con = getConnection();
        System.out.println("aky je posledny zaznam");
        String isSuccess;
        try {
            PreparedStatement statement = con.prepareStatement(queryLastrecord);
            statement.setInt(1, Client);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                isSuccess = resultSet.getString("success");
                System.out.println(isSuccess);
                if (isSuccess == null) {
                    return false;
                } else {
                    System.out.println(isSuccess);
                    return true;
                }
            }
            System.out.println("neni ziaden zazanm o prihlaseni");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    //odblokovanie zamestnancom
    private static final String queryUnblockByEmp = "insert into loginhistory(idl,success) values((select id from loginclient where idc = ?),1)";

    public void unblockByEmp(int clientID){
        Connection conn = getConnection();
        System.out.println("odblokuvavam IB");
        try{
            PreparedStatement statement = conn.prepareStatement(queryUnblockByEmp);
            statement.setInt(1,clientID);
            statement.execute();
            conn.close();
        }  catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static final String queryBlockingCard = "update card set active = ? where id = ?";

    public void blockCard(int idCard){
        Connection con = getConnection();
        System.out.println("blokujem kartu");
        try {
            PreparedStatement statement = con.prepareStatement(queryBlockingCard);
            statement.setBoolean(1,false);
            statement.setInt(2,idCard);
            statement.execute();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void unBlockCard(int idCard){
        Connection con = getConnection();
        System.out.println("blokujem kartu");
        try {
            PreparedStatement statement = con.prepareStatement(queryBlockingCard);
            statement.setBoolean(1,true);
            statement.setInt(2,idCard);
            statement.execute();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static final String queryChangingPIN = "update card set pin = ? where id = ?";

    public void changePin(String pin,int idCard){
        Connection con = getConnection();
        System.out.println("menim pin");
        try {
            PreparedStatement statement = con.prepareStatement(queryChangingPIN);
            statement.setString(1,pin);
            statement.setInt(2,idCard);
            statement.execute();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static final String queryWithdrawMoney = "update account set amount = amount + ? where accnum = ?";

    public void sendingMoney(String acountNum,double money){
        Connection con = getConnection();
        System.out.println("posielam peniaze");
        try {
            PreparedStatement statement = con.prepareStatement(queryWithdrawMoney);
            statement.setDouble(1,money);
            statement.setString(2,acountNum);
            statement.execute();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static final String queryDepositMoney = "update account set amount = amount - ? where accnum = ?";

    public void gettingMoney(String acountNum,double money){
        Connection con = getConnection();
        System.out.println("ziskavam peniaze");
        try {
            PreparedStatement statement = con.prepareStatement(queryDepositMoney);
            statement.setDouble(1,money);
            statement.setString(2,acountNum);
            statement.execute();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static final String queryNewTransaction = "insert into transaction(idacc,idEmployee,recAccount,transAmount) values(?,?,?,?)";

    public void makeingTransaction(int ida,int ide, String recAccNum, double money){
        Connection conn = getConnection();
        System.out.println("vytvaram novu transakciu");
        try{
            PreparedStatement statement = conn.prepareStatement(queryNewTransaction);
            statement.setInt(1,ida);
            statement.setInt(2,ide);
            statement.setString(3,recAccNum);
            statement.setDouble(4,money);
            statement.execute();
            conn.close();
        }  catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
