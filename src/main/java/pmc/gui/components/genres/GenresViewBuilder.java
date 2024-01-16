package pmc.gui.components.genres;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Builder;
import pmc.gui.common.GenreModel;
import javafx.collections.ListChangeListener;
import pmc.gui.common.MovieModel;
import pmc.gui.widgets.ImageWidgets;
import pmc.gui.widgets.buttons.ButtonWidgets;
import pmc.gui.widgets.icons.ScrollPaneWidgets;

public class GenresViewBuilder implements Builder<Region> {

    private ObservableList<GenreModel> model;

    public GenresViewBuilder(ObservableList<GenreModel> model) {
        this.model = model;
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

        this.model.addListener((ListChangeListener.Change<? extends GenreModel> change) -> {
            for (GenreModel genreModel : model) {
                Button btn = ButtonWidgets.actionButtonStyle("name", "genre-category-button", event -> categoryClick(genreModel));
                btn.textProperty().bind(genreModel.nameProperty());

                tilePane.getChildren().add(btn);
            }
        });
        return tilePane;
    }


    private void categoryClick(GenreModel genreModel) {
        if (!genreModel.getMovies().isEmpty()) {
            for (MovieModel matchingMovie : genreModel.getMovies()) {
                System.out.println(genreModel.nameProperty().get() + " " + matchingMovie.tmdbIdProperty().get());
            }
        } else {
            System.out.println("moviemodels ikke fundet");
        }
    }
}

