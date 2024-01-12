package pmc.gui.components.dialog.addcategory;

import javafx.scene.control.Dialog;
import javafx.scene.layout.Region;
import pmc.gui.common.MovieModel;
import pmc.gui.components.dialog.IDialogController;

public class AddCategoryController implements IDialogController<MovieModel> {
    private final AddCategoryViewBuilder viewBuilder;
    private Dialog<MovieModel> dialog;

    public AddCategoryController() {
        this.viewBuilder = new AddCategoryViewBuilder();
    }

    @Override
    public Region getView() {
        return viewBuilder.build();
    }

    @Override
    public void initializeDialog(Dialog<MovieModel> dialog) {
        this.dialog = dialog;

        dialog.setResultConverter(btn -> new MovieModel());
    }
}
