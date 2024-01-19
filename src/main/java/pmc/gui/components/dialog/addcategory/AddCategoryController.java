package pmc.gui.components.dialog.addcategory;

import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.Region;
import pmc.be.Category;
import pmc.gui.components.dialog.IDialogController;

public class AddCategoryController implements IDialogController<AddCategoryData> {
    private final AddCategoryModel model;
    private final AddCategoryViewBuilder viewBuilder;
    private Dialog<AddCategoryData> dialog;

    public AddCategoryController(CategoryActions actions,
                                 ObservableList<Category> categories) {
        this.model = new AddCategoryModel();
        model.setCategories(categories);
        this.viewBuilder = new AddCategoryViewBuilder(model, actions);
    }

    @Override
    public Region getView() {
        return viewBuilder.build();
    }

    @Override
    public void initializeDialog(Dialog<AddCategoryData> dialog) {
        this.dialog = dialog;

        dialog.setResultConverter(dialogBtn -> {
            if (dialogBtn == ButtonType.OK) {
                return new AddCategoryData(model.nameProperty().get());
            }
            return null;
        });
    }
}
