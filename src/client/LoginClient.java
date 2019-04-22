package client;

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

    private String generatingLogin(){
        Random random = new Random();
        String userName ="";
        for (int i=0;i<7;i++){
            userName = userName + random.nextInt(10);
        }

        System.out.println("login "+userName);
        return userName;
    }


    private String generatingPass(){
        Random random = new Random();
        String userPassowrd ="";
        for (int i=0;i<7;i++){
            userPassowrd = userPassowrd + random.nextInt(26)+65;
        }

        System.out.println("passowrd "+userPassowrd);
        return userPassowrd;
    }
}
