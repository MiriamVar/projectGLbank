package database;

import employee.employee;
import sample.Globals;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    static final String queryEmp = "secelt client.fname, client.lname from client";

    public List<employee> getAllEmployees(){
        Connection con = getConnection();
        ResultSet res;
        List<employee> clients = new ArrayList<>();
        try {
            PreparedStatement stmnt = con.prepareStatement(queryEmp);
            res = stmnt.executeQuery();
            while (res.next()){
                String name = res.getString("fname");
                String surname = res.getString("lname");
                employee employee = new employee(name,surname);
               clients.add(employee);
            }
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }
}
