package windows;

import client.Client;
import database.mysqlDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class newClient {

    public TextField meno;
    public TextField priezvisko;
    public TextField email;
    public Label lblMess;


    public void sendingNC(ActionEvent actionEvent) throws IOException {
        String clientsName = meno.getText();
        String clientsSurname = priezvisko.getText();
        String clientsEmail = email.getText();

        System.out.println(clientsName + clientsSurname + clientsEmail);

        if(clientsName.isEmpty() || clientsSurname.isEmpty() || clientsEmail.isEmpty() ){
            lblMess.setVisible(true);
        }
        else {
            Client newClient = new Client(clientsName, clientsSurname, clientsEmail);
            mysqlDatabase database = mysqlDatabase.getInstanceOfDatabase();

            int idClient = database.addNewClient(newClient);
            if (idClient < 0) {
                System.out.println("neexistujuce id clienta");
            } else {
                database.addNewLoginClient(idClient);
            }


            Node node = (Node) actionEvent.getSource();
            Stage dialogStage = (Stage) node.getScene().getWindow();
            dialogStage.close();
        }

        
    }
}
