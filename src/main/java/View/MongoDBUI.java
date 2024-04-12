package View;

import Controller.MongoControl;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class MongoDBUI extends Application {
    private MongoControl mongoControl;

    private TextField idField = new TextField();
    private TextField nameField = new TextField();
    private TextField ageField = new TextField();
    private TextField cityField = new TextField();
    private Button addButton = new Button("Add");
    private Button readButton = new Button("Read");
    private Button updateButton = new Button("Update");
    private Button deleteButton = new Button("Delete");
    private Scene scene;

    public void init() {
        mongoControl = new MongoControl(this);
        mongoControl.init();
    }
    public String getId() {
        return idField.getText();
    }

    public String getName() {
        return nameField.getText();
    }

    public String getAge() {
        return ageField.getText();
    }

    public String getCity() {
        return cityField.getText();
    }




    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("MongoDB UI");
        primaryStage.setScene(scene);

        primaryStage.show();

    }
}
