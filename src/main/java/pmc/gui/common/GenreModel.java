package pmc.gui.common;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class GenreModel {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty("");

    private ObservableList<MovieModel> movies;

    public GenreModel() {}

    public GenreModel(int id, String name) {
        this.id.set(id);
        this.name.set(name);
    }

    public ObservableList<MovieModel> getMovies() {
        return movies;
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty nameProperty() {
        return name;
    }
}
