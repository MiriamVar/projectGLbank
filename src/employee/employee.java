package employee;

public class employee {
    private int id;
    private String fname;
    private String lname;
    private int position;

    public employee(int id, String fname, String lname, int position) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.position = position;
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

    public int getPosition() {
        return position;
    }
}
