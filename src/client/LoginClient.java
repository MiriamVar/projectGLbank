package client;


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

    public void setId(int id) {
        this.id = id;
    }

    public void setIdc(int idc) {
        this.idc = idc;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
