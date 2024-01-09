package pmc.gui.components.pmc;

import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Builder;
import pmc.gui.common.IViewController;
import pmc.gui.components.categories.CategoriesController;
import pmc.gui.components.home.HomeController;

/**
 * Hoved Controller for Private Movie Collection (PMC) applikationen.<br>
 * Fungerer som den centrale koordinator hvor den styrer applikationens flow og integrerer forskellige MVC komponenter.
 */
public class PMCController implements IViewController {
    private final PMCModel model;
    private final Builder<Region> viewBuilder;

    public PMCController(Stage stage) {
        this.model = new PMCModel();
        this.viewBuilder = new PMCViewBuilder(model,
                new HomeController().getView(),
                new CategoriesController().getView());
    }

    @Override
    public Region getView() {
        return viewBuilder.build();
    }
}
