package Controller;

import Datasource.MongoConnection;
import Model.CrudOperations;
import View.MongoDBUI;

public class MongoControl {
    private MongoConnection mongoConnection;
    private CrudOperations crudOperations;
    private MongoDBUI view;

    public MongoControl(MongoDBUI view) {
        mongoConnection = new MongoConnection();
        this.view = view;
    }

    public void init() {
        mongoConnection.init();
    }

    public void add() {
        crudOperations.addUser(Integer.parseInt(view.getId()), view.getName(), Integer.parseInt(view.getAge()), view.getCity());
    }

    public void read() {
        crudOperations.readUser(Integer.parseInt(view.getId()));
    }

    public void update() {
        crudOperations.updateUser(Integer.parseInt(view.getId()), view.getName(), Integer.parseInt(view.getAge()), view.getCity());
    }

    public void delete() {
        crudOperations.deleteUser(Integer.parseInt(view.getId()));
    }


}
