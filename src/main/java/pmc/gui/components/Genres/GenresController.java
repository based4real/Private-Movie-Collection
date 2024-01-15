package pmc.gui.components.Genres;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import pmc.be.Genre;
import pmc.gui.common.GenreModel;
import pmc.gui.common.IViewController;
import pmc.gui.common.MovieDetailsModel;
import pmc.gui.common.MovieModel;

import java.util.List;

public class GenresController implements IViewController {
    private final Builder<Region> viewBuilder;

    private ObservableList<MovieModel> movieModel = FXCollections.observableArrayList();

    public GenresController(ObservableList<GenreModel> model, ObservableList<MovieModel> movieModel) {
        this.viewBuilder = new GenresViewBuilder(model, movieModel);
    }

    public void getMoviesFromGenre() {
        for (MovieModel movieModel : movieModel)
            System.out.println(movieModel.tmdbIdProperty().get());
    }

    @Override
    public Region getView() {
        return viewBuilder.build();
    }
}
