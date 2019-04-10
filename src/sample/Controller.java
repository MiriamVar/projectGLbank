package sample;

import database.mysqlDatabase;
import employee.employee;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {
    public Button enterBtn;
    public TextField loginInput;
    public TextField passInput;
    public Label wrongName;
    public Label wrongPassword;

    public void logIn(ActionEvent actionEvent) {
        String userName = loginInput.getText();
        String password = passInput.getText();

        System.out.println(userName + password);

        mysqlDatabase database = mysqlDatabase.getInstanceOfDatabase();
        employee emp = database.getEmployee(userName,password);

        if(emp != null){
            System.out.println(emp.getId() + " " + emp.getFname() + " " + emp.getLname() + " " + emp.getPosition());
        } else {
            //este dokoncit
            wrongName.setVisible(true);
            wrongPassword.setVisible(true);
        }

    }
}
