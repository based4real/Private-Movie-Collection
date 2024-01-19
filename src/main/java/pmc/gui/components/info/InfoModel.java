package pmc.gui.components.info;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import pmc.be.rest.tmdb.TMDBCreditEntity;
import pmc.be.rest.tmdb.TMDBGenreEntity;
import pmc.be.rest.tmdb.TMDBVideoEntity;
import pmc.gui.common.MovieModel;
import pmc.gui.components.genres.GenresModel;

public class InfoModel {
    private final StringProperty posterPath = new SimpleStringProperty("");

    private final IntegerProperty tmdbId = new SimpleIntegerProperty();
    private final StringProperty title = new SimpleStringProperty("");
    private final StringProperty director = new SimpleStringProperty("");
    private final StringProperty release = new SimpleStringProperty("");
    private final StringProperty runtime = new SimpleStringProperty("");
    private final StringProperty rated = new SimpleStringProperty("");
    private final StringProperty description = new SimpleStringProperty("");

    private final ObservableList<TMDBGenreEntity> genres = FXCollections.observableArrayList();
    private final ObservableList<TMDBCreditEntity> credits = FXCollections.observableArrayList();
    private final ObservableList<TMDBVideoEntity> videos = FXCollections.observableArrayList();
    private ObservableList<GenresModel> genresModels = FXCollections.observableArrayList();

    private final ObjectProperty<MovieModel> movieModel = new SimpleObjectProperty<>();

    public InfoModel(ObservableList<GenresModel> genresModels) {
        this.genresModels = genresModels;
    }

    public void setMovieModel(MovieModel movieModel) {
        this.movieModel.set(movieModel);
    }

    public ObjectProperty<MovieModel> movieModelProperty() {
        return movieModel;
    }

    public IntegerProperty tmdbIdProperty() {
        return tmdbId;
    }

    public StringProperty posterPathProperty() {
        return posterPath;
    }

    public StringProperty titleProperty() {
        return title;
    }

    public StringProperty directorProperty() {
        return director;
    }

    public StringProperty releaseProperty() {
        return release;
    }

    public StringProperty runtimeProperty() {
        return runtime;
    }

    public StringProperty ratedProperty() {
        return rated;
    }

    public ObservableList<TMDBGenreEntity> genresProperty() {
        return genres;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public ObservableList<TMDBCreditEntity> creditsProperty() {
        return credits;
    }

    public ObservableList<GenresModel> genresModelsProperty() {
        return genresModels;
    }

    public ObservableList<TMDBVideoEntity> videosProperty() {
        return videos;
    }

    public FilteredList<GenresModel> getGenreModels() {
        FilteredList<GenresModel> filteredList = new FilteredList<>(genresModels);

        filteredList.setPredicate(genreModel ->
                genres.stream().anyMatch(tmdbGenreEntity ->
                        genreModel.idProperty().get() == tmdbGenreEntity.getID())
        );

        return filteredList;
    }

    public FilteredList<TMDBCreditEntity> getDirector() {
        return new FilteredList<>(
                credits,
                credit -> credit.getDepartment().equals("Directing") // <= Ville give en for meget da den starter ved 0
        );
    }

    public FilteredList<TMDBCreditEntity> creditsPropertyMaxResults(int max) {
        return new FilteredList<>(
                credits,
                credit -> credit.getOrderID() != -1 && credit.getOrderID() < max // <= Ville give en for meget da den starter ved 0
        );
    }

}
