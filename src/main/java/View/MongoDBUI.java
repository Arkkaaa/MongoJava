package View;
import Datasource.MongoConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.types.ObjectId;

public class MongoDBUI extends Application {

    private MongoCollection<Document> collection;
    private TextField idInput;
    private TextField nameInput;
    private TextField ageInput;
    private TextField cityInput;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("User Management");
        MongoConnection mongoConnection = new MongoConnection();
        mongoConnection.init();
        MongoDatabase db = mongoConnection.getDatabase();
        collection = db.getCollection("users");

        // Create UI
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(8);
        grid.setHgap(10);

        // Create UI elements
        // TextFields & Labels
        // ID
        Label idLabel = new Label("ID:");
        GridPane.setConstraints(idLabel, 0, 0);
        idInput = new TextField();
        idInput.setPromptText("Mongo _id");
        idInput.setEditable(false);
        idInput.setFocusTraversable(false);
        GridPane.setConstraints(idInput, 1, 0);

        // Name
        Label nameLabel = new Label("Name:");
        GridPane.setConstraints(nameLabel, 0, 1);
        nameInput = new TextField();
        nameInput.setPromptText("Name");
        GridPane.setConstraints(nameInput, 1, 1);

        // Age
        Label ageLabel = new Label("Age:");
        GridPane.setConstraints(ageLabel, 0, 2);
        ageInput = new TextField();
        ageInput.setPromptText("Age");
        ageInput.addEventFilter(KeyEvent.KEY_TYPED, numericOnly(3));
        GridPane.setConstraints(ageInput, 1, 2);

        // City
        Label cityLabel = new Label("City:");
        GridPane.setConstraints(cityLabel, 0, 3);
        cityInput = new TextField();
        cityInput.setPromptText("City");
        GridPane.setConstraints(cityInput, 1, 3);

        // Buttons
        // Add
        Button addButton = new Button("Add");
        GridPane.setConstraints(addButton, 0, 4);
        addButton.setOnAction(e -> {
            if (checkFields()) {
                boolean success = addDocument(nameInput.getText(), Integer.parseInt(ageInput.getText()), cityInput.getText());
                showAlert(success, "Add");
            }

        });
        // Update
        Button updateButton = new Button("Update");
        GridPane.setConstraints(updateButton, 1, 4);
        updateButton.setOnAction(e -> {
            if (checkFields()) {
                if (checkName()) {
                    boolean success = updateDocument(idInput.getText(), nameInput.getText(), Integer.parseInt(ageInput.getText()), cityInput.getText());
                    showAlert(success, "Update");
                }
            }
        });
        // Delete
        Button deleteButton = new Button("Delete");
        GridPane.setConstraints(deleteButton, 2, 4);
        deleteButton.setOnAction(e -> {
            if (checkName()) {
                boolean success = deleteDocument(nameInput.getText());
                showAlert(success, "Delete");
            }
        });
        // Retrieve
        Button retrieveButton = new Button("Retrieve");
        GridPane.setConstraints(retrieveButton, 3, 4);
        retrieveButton.setOnAction(e -> {
            if (checkName()) {
                retrieveDocument(nameInput.getText());
            }
        });

        // Add elements to grid
        grid.getChildren().addAll(idLabel, idInput, nameLabel, nameInput, ageLabel, ageInput, cityLabel, cityInput,
                addButton, updateButton, deleteButton, retrieveButton);

        // Create scene
        Scene scene = new Scene(grid, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Button & TextField event handlers
    // Restrict input to numeric only for age field
    private EventHandler<KeyEvent> numericOnly(final int maxLength) {
        return event -> {
            TextField textField = (TextField) event.getSource();
            if (!event.getCharacter().matches("\\d") || textField.getText().length() >= maxLength) {
                event.consume();
            }
        };

    }

    // Check if name field is empty
    private boolean checkName() {
        if (nameInput.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid Name!");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    // Check if all fields are filled
    private boolean checkFields() {
        if (nameInput.getText().isEmpty() || ageInput.getText().isEmpty() || cityInput.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all fields!");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    // Reset fields
    public void clearFields() {
        idInput.clear();
        nameInput.clear();
        ageInput.clear();
        cityInput.clear();
    }

    // Show alert based on operation success
    private void showAlert(boolean success, String operation) {
        Alert alert = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle("Operation Result");
        alert.setHeaderText(null);
        alert.setContentText(success ? operation + " operation successful!" : "Error during " + operation + " operation!");
        alert.showAndWait();
    }

    // CRUD operations
    // Create
    private boolean addDocument(String name, int age, String city) {
        Document document = new Document("name", name)
                .append("age", age)
                .append("city", city);
        try {
            collection.insertOne(document);
            idInput.setText(document.getObjectId("_id").toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    // Update
    private boolean updateDocument(String id, String name, int age, String city) {
        Document filter = new Document("_id", new ObjectId(id));
        Document update = new Document("$set", new Document("name", name)
                .append("age", age)
                .append("city", city));
        try {
            collection.updateOne(filter, update);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    // Delete
    private boolean deleteDocument(String name) {
        Document filter = new Document("name", name);
        try {
            if (collection.find(filter).first() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("No document found with name: " + name);
                alert.showAndWait();
                return false;
            }
            collection.deleteOne(filter);
            clearFields();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    // Retrieve
    // show alert with retrieved document
    // & fill fields with retrieved document
    private void retrieveDocument(String name) {
        Document filter = new Document("name", name);
        MongoCursor<Document> cursor = collection.find(filter).iterator();
        if (cursor.hasNext()) {
            Document doc = cursor.next();
            idInput.setText(doc.getObjectId("_id").toString());
            nameInput.setText(doc.getString("name"));
            ageInput.setText(String.valueOf(doc.getInteger("age")));
            cityInput.setText(doc.getString("city"));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Retrieved document");
            alert.setHeaderText(null);
            alert.setContentText(
                     "ID: " + doc.getObjectId("_id") + "\n" +
                     "Name: " + doc.getString("name") + "\n" +
                     "Age: " + doc.getInteger("age") + "\n" +
                     "City: " + doc.getString("city"));
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No document found with name: " + name);
            alert.showAndWait();
        }
        cursor.close();
    }
}
