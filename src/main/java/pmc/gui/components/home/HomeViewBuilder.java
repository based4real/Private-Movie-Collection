package pmc.gui.components.home;

import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class HomeViewBuilder implements Builder<Region> {
    @Override
    public Region build() {
        return new Button("Home");
    }
}
