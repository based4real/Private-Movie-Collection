package pmc.gui.components.categories;

import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class CategoriesViewBuilder implements Builder<Region> {
    @Override
    public Region build() {
        return new Button("Categories");
    }
}
