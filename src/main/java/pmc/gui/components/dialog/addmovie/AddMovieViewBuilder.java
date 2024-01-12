package pmc.gui.components.dialog.addmovie;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

public class AddMovieViewBuilder implements Builder<Region> {
    @Override
    public Region build() {
        VBox results = new VBox(10);

        results.getChildren().add(new Button("Tilføj film"));
        results.setAlignment(Pos.CENTER);

        return results;
    }
}
