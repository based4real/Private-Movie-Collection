package pmc.gui.components.dialog.addmovie;

import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class AddMovieViewBuilder implements Builder<Region> {
    @Override
    public Region build() {
        return new Button("add movie dialog");
    }
}
