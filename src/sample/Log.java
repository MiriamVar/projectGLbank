package sample;


import database.mysqlDatabase;
import employee.employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Log {
    public Label name;
    public Label surname;
    public Button logOut;
    public Stage dialogStage;
    public ChoiceBox<employee> clientsNames;
    public List<employee> emp;

    public void showData(employee emp){
        name.setText(emp.getFname());
        surname.setText(emp.getLname());
    }

    public void logOut(ActionEvent actionEvent) throws IOException {

        Node node = (Node)actionEvent.getSource();
        dialogStage = (Stage) node.getScene().getWindow();
        dialogStage.close();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("sample.fxml"));
        Parent accountView = fxmlLoader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(accountView));

        stage.show();
    }

    public void gettingAllClients(DragEvent actionEvent) {
        mysqlDatabase database = mysqlDatabase.getInstanceOfDatabase();
        emp = database.getAllEmployees();
        for (employee person: emp) {
            System.out.println(person.getFname()+" "+person.getLname());
        }


    }
}
