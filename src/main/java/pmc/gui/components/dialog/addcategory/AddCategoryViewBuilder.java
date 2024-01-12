package pmc.gui.components.dialog.addcategory;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

public class AddCategoryViewBuilder implements Builder<Region> {
    @Override
    public Region build() {
        VBox results = new VBox(10);

        results.getChildren().add(new Button("Tilføj kategori"));
        results.setAlignment(Pos.CENTER);

        return results;
    }
}
