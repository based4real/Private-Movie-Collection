package pmc.gui.components.pmc;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pmc.gui.common.MovieModel;

public class PMCModel {
    private final ObservableList<MovieModel> movieModels = FXCollections.observableArrayList();
    private final BooleanProperty homeSelected = new SimpleBooleanProperty(false);
    private final BooleanProperty categoriesSelected = new SimpleBooleanProperty(false);

    public ObservableList<MovieModel> movieModels(){
        return movieModels;
    }

    public BooleanProperty homeSelectedProperty() {
        return homeSelected;
    }

    public BooleanProperty categoriesSelectedProperty() {
        return categoriesSelected;
    }
}
