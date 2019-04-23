package client;

import database.mysqlDatabase;

import java.util.Random;

public class LoginClient {
    private int id;
    private int idc;
    private String login;
    private String password;

    public LoginClient(int id, int idc, String login, String password) {
        this.id = id;
        this.idc = idc;
        this.login = login;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public int getIdc() {
        return idc;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

}
