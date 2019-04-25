package client;

import java.sql.Date;

public class LoginHistory {
    private int id;
    private int idl;
    private String success;
    private Date logDate;

    public LoginHistory(int id, int idl, String success, Date logDate) {
        this.id = id;
        this.idl = idl;
        this.success = success;
        this.logDate = logDate;
    }

    public int getId() {
        return id;
    }

    public int getIdl() {
        return idl;
    }

    public String isSuccess() {
        return success;
    }

    public Date getLogDate() {
        return logDate;
    }
}
