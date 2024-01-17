package pmc.gui.components.genres;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.util.Builder;
import javafx.collections.ListChangeListener;
import pmc.gui.common.MovieModel;
import pmc.gui.common.MoviesData;
import pmc.gui.utils.StringHandler;
import pmc.gui.widgets.buttons.ButtonWidgets;
import pmc.gui.widgets.ScrollPaneWidgets;

import java.util.function.Consumer;

public class GenresViewBuilder implements Builder<Region> {

    private ObservableList<GenresModel> model;
    private Consumer<MoviesData> viewChangehandler;
    private TilePane tilePane;

    public GenresViewBuilder(ObservableList<GenresModel> model, Consumer<MoviesData> viewChangeHandler) {
        this.model = model;
        this.viewChangehandler = viewChangeHandler;
    }

    @Override
    public Region build() {
        ScrollPane scrollPane = ScrollPaneWidgets.defaultPageScrollPane(addGenres());
        scrollPane.setFitToHeight(true);
        return scrollPane;
    }

    private TilePane addGenres() {
        tilePane = new TilePane();
        tilePane.setPadding(new Insets(5, 20, 0, 20));
        tilePane.setHgap(15);
        tilePane.setVgap(15);

        this.model.addListener((ListChangeListener.Change<? extends GenresModel> change) -> {
            updateGenreButtons();
        });

        return tilePane;
    }

    private void updateGenreButtons() {
        tilePane.getChildren().clear();

        for (GenresModel genreModel : model) {
            genreModel.getMovies().addListener((ListChangeListener.Change<? extends MovieModel> moviesUpdate) -> {
                handleGenreMoviesChange(genreModel);
            });
        }
    }

    private void handleGenreMoviesChange(GenresModel genreModel) {
        if (!genreModel.getMovies().isEmpty()) {
            Button btn = ButtonWidgets.actionButtonStyle(StringHandler.trimName(genreModel.nameProperty().get(), 15), "genre-category-button", event -> categoryClick(genreModel));

            tilePane.getChildren().add(btn);
        }
    }


    private void categoryClick(GenresModel genreModel) {
        //if (!genreModel.getMovies().isEmpty())
        this.viewChangehandler.accept(new MoviesData(genreModel.nameProperty().get(), genreModel.getMovies()));
    }
}

