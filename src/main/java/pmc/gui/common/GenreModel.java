package pmc.gui.common;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class GenreModel {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty("");
    private String stringName = "";

    public GenreModel() {}

    public GenreModel(int id, String name) {
        this.id.set(id);
        //this.name.set(name);
        this.stringName = name;
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String name() {
        return stringName;
    }

    public StringProperty nameProperty() {
        return name;
    }
}
