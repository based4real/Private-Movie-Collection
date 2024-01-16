package pmc.gui.components.genres;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.util.Builder;
import pmc.gui.common.GenreModel;
import javafx.collections.ListChangeListener;
import pmc.gui.common.MovieModel;
import pmc.gui.common.MoviesData;
import pmc.gui.widgets.buttons.ButtonWidgets;
import pmc.gui.widgets.ScrollPaneWidgets;

import java.util.function.Consumer;

public class GenresViewBuilder implements Builder<Region> {

    private ObservableList<GenreModel> model;
    private Consumer<MoviesData> viewChangehandler;

    public GenresViewBuilder(ObservableList<GenreModel> model, Consumer<MoviesData> viewChangeHandler) {
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
        TilePane tilePane = new TilePane();
        tilePane.setPadding(new Insets(5, 20, 0, 20));
        tilePane.setHgap(15);
        tilePane.setVgap(15);

         /*
            Lidt dumt tjek.. men vi lytter først om modellen opdaterer sig, hvis ja så loop igennem dem.
            Derefter tjekker vi om .getMovies() ændrer sig, hvis ja så tjek om den er tom og hvis den ikke
            er så tilføj til tilepane..
            Bruges så der ikke vises en tom genre.
         */
        this.model.addListener((ListChangeListener.Change<? extends GenreModel> change) -> {
            for (GenreModel genreModel : model) {
                genreModel.getMovies().addListener((ListChangeListener.Change<? extends MovieModel> moviesUpdate) -> {
                    if (!genreModel.getMovies().isEmpty()) {
                        Button btn = ButtonWidgets.actionButtonStyle(genreModel.nameProperty().get(), "genre-category-button", event -> categoryClick(genreModel));

                        tilePane.getChildren().add(btn);
                    }
                });
            }
        });
        return tilePane;
    }


    private void categoryClick(GenreModel genreModel) {
        //if (!genreModel.getMovies().isEmpty())
        this.viewChangehandler.accept(new MoviesData(genreModel.nameProperty().get(), genreModel.getMovies()));
    }
}

