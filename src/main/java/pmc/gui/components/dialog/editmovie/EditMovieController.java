package pmc.gui.components.dialog.editmovie;

import javafx.collections.ObservableList;
import javafx.scene.control.Dialog;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import pmc.gui.common.MovieModel;
import pmc.gui.components.categories.CategoriesModel;
import pmc.gui.components.dialog.IDialogController;

import java.util.function.Consumer;

public class EditMovieController implements IDialogController<EditMovieData> {
    private final EditMovieModel model;
    private final Builder<Region> viewBuilder;
    private final MovieModel movieToEdit;
    private final ObservableList<CategoriesModel> categories;
    private Dialog<EditMovieData> dialog;

    public EditMovieController(MovieModel movieToEdit, ObservableList<CategoriesModel> categories, Consumer<EditMovieData> updateHandler) {
        this.model = new EditMovieModel();
        this.viewBuilder = new EditMovieViewBuilder(model, movieToEdit, categories, updateHandler);
        this.movieToEdit = movieToEdit;
        this.categories = categories;
    }

    @Override
    public Region getView() {
        return viewBuilder.build();
    }

    @Override
    public void initializeDialog(Dialog<EditMovieData> dialog) {
        this.dialog = dialog;

    }
}