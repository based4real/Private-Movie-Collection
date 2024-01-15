package pmc.gui.components.Genres;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.util.Builder;
import pmc.gui.common.GenreModel;
import javafx.collections.ListChangeListener;
import pmc.gui.common.MovieModel;
import pmc.gui.widgets.buttons.ButtonWidgets;

public class GenresViewBuilder implements Builder<Region> {

    private ObservableList<GenreModel> model;
    private ObservableList<MovieModel> movieModels;

    public GenresViewBuilder(ObservableList<GenreModel> model, ObservableList<MovieModel> movieModel) {
        this.model = model;
        this.movieModels = movieModel;
    }

    @Override
    public Region build() {
        return addGenres();
    }

    private TilePane addGenres() {
        TilePane tilePane = new TilePane();
        tilePane.setPadding(new Insets(5, 20, 0, 20));
        tilePane.setHgap(15);
        tilePane.setVgap(15);

        this.model.addListener((ListChangeListener.Change<? extends GenreModel> change) -> {
            for (GenreModel genreModel : model) {
                Button btn = ButtonWidgets.actionButtonStyle(genreModel.name(), "genre-button", event -> genreClick(genreModel));

                tilePane.getChildren().add(btn);
            }
        });

        return tilePane;
    }

    private void genreClick(GenreModel genreModel) {
        System.out.println(genreModel.idProperty());
    }
}

