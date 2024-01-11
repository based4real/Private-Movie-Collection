package pmc.gui.components.pmc;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pmc.gui.common.MovieModel;

import javax.swing.text.View;

public class PMCModel {
    private final ObjectProperty<ViewType> activeView = new SimpleObjectProperty<>(ViewType.HOME);
    private final ObjectProperty<ViewType> previousView = new SimpleObjectProperty<>(ViewType.HOME);
    private final ObservableList<MovieModel> movieModels = FXCollections.observableArrayList();


    public ObjectProperty<ViewType> activeViewProperty() {
        return activeView;
    }

    public ObjectProperty<ViewType> previousViewProperty() {
        return previousView;
    }

    public ObservableList<MovieModel> movieModels(){
        return movieModels;
    }

}
