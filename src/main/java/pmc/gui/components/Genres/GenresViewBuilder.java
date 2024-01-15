package pmc.gui.components.Genres;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import pmc.gui.common.GenreModel;

public class GenresViewBuilder implements Builder<Region> {

    public GenresViewBuilder() {

    }

    @Override
    public Region build() {
        return new Button("Genres");
    }
}
