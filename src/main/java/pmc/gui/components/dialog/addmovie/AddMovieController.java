package pmc.gui.components.dialog.addmovie;

import javafx.scene.control.Dialog;
import javafx.scene.layout.Region;
import pmc.gui.common.MovieModel;
import pmc.gui.components.dialog.IDialogController;

public class AddMovieController implements IDialogController<MovieModel> {
    private final AddMovieViewBuilder viewBuilder;
    private Dialog<MovieModel> dialog;

    public AddMovieController() {
        this.viewBuilder = new AddMovieViewBuilder();
    }

    @Override
    public Region getView() {
        return viewBuilder.build();
    }

    @Override
    public void initializeDialog(Dialog<MovieModel> dialog) {
        this.dialog = dialog;

        dialog.setResultConverter(btn -> new MovieModel("hej fra add movie controller"));
    }
}
