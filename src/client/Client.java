package client;

public class Client {
    private int id;
    private String fname;
    private String lname;
    private String email;

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

}
