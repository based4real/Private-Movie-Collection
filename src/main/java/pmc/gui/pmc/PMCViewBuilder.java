package pmc.gui.pmc;

import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.util.Builder;

/**
 * Ansvarlig for at bygge brugergrænsefladen for det overordnede View i Private Movie Collection (PMC) applikationen.
 */
public class PMCViewBuilder implements Builder<Region> {
    private final PMCModel model;

    public PMCViewBuilder(PMCModel model) {
        this.model = model;
    }

    @Override
    public Region build() {
        return new Label("hej");
    }
}
