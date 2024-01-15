package pmc.gui.common;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class GenreModel {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty("");

    public GenreModel() {}

    public GenreModel(int id,
                      String name) {
        this.id.set(id);
        this.name.set("name");
    }

    public IntegerProperty idProperty() { return id; }

    public StringProperty nameProperty() {
        return name;
    }

}
