package pmc.gui.components.Genres;

import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class GenresViewBuilder implements Builder<Region> {

    public GenresViewBuilder() {

    }

    @Override
    public Region build() {
        return new Button("Genres");
    }
}
