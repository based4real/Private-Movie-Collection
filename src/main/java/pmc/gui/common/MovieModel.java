package pmc.gui.common;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pmc.be.Category;
import pmc.be.Genre;
import pmc.be.Movie;
import pmc.be.rest.tmdb.TMDBVideoEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MovieModel {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty tmdbId = new SimpleIntegerProperty();
    private final StringProperty imdbId = new SimpleStringProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final FloatProperty imdbRating = new SimpleFloatProperty();
    private final IntegerProperty personalRating = new SimpleIntegerProperty();
    private final StringProperty filePath = new SimpleStringProperty("");
    private final StringProperty posterPath = new SimpleStringProperty("");
    private final ObjectProperty<LocalDateTime> lastSeen = new SimpleObjectProperty<>();

    private final ObservableList<Genre> genres = FXCollections.observableArrayList();
    private final ObservableList<Category> categories = FXCollections.observableArrayList();
    private final ObservableList<TMDBVideoEntity> videos = FXCollections.observableArrayList();

    public MovieModel() {}

    public MovieModel(int id,
                      int tmdbId,
                      String imdbId,
                      String title,
                      float imdbRating,
                      int personalRating,
                      String filePath,
                      String posterPath,
                      LocalDateTime lastSeen,
                      List<Genre> genres,
                      List<Category> categories) {
        this.id.set(id);
        this.tmdbId.set(tmdbId);
        this.imdbId.set(imdbId);
        this.title.set(title);
        this.imdbRating.set(imdbRating);
        this.personalRating.set(personalRating);
        this.filePath.set("data/movies/" + filePath);
        this.posterPath.set("data/posters/" + posterPath);
        this.lastSeen.set(lastSeen);
        this.genres.setAll(genres);
        this.categories.setAll(categories);
    }

    public MovieModel(Movie movie) {
        this.id.set(movie.getId());
        this.tmdbId.set(movie.getTmdbId());
        this.imdbId.set(movie.getImdbId());
        this.title.set(movie.getTitle());
        this.imdbRating.set(movie.getImdbRating());
        this.personalRating.set(movie.getPersonalRating());
        this.filePath.set("data/movies/" + movie.getFilePath());
        this.posterPath.set("data/posters/" + movie.getPosterPath());
        this.lastSeen.set(movie.getLastSeen());
        this.genres.setAll(movie.getGenres());
        this.categories.setAll(movie.getCategories());
    }

    public Movie toEntity() {
        Movie movie = new Movie(id.get(),
                tmdbId.get(),
                imdbId.get(),
                title.get(),
                imdbRating.get(),
                personalRating.get(),
                filePath.get().replace("data/movies/", ""),
                posterPath.get().replace("data/posters/", ""),
                lastSeen.get());
        movie.setGenres(new ArrayList<>(this.genres));
        movie.setCategories(new ArrayList<>(this.categories));

        return movie;
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public IntegerProperty tmdbIdProperty() { return tmdbId; }

    public StringProperty imdbIdProperty() {
        return imdbId;
    }

    public StringProperty titleProperty() {
        return title;
    }

    public FloatProperty imdbRatingProperty() {
        return imdbRating;
    }

    public IntegerProperty personalRatingProperty() {
        return personalRating;
    }

    public StringProperty posterPathProperty() {
        return posterPath;
    }

    public StringProperty filePathProperty() {
        return filePath;
    }

    public ObjectProperty<LocalDateTime> lastSeenProperty() {
        return lastSeen;
    }
    public ObservableList<Genre> genreObservableList() {
        return genres;
    }
    public ObservableList<Category> categoryObservableList() { return categories; }

    public ObservableList<TMDBVideoEntity> tmdbVideosObservableList() { return videos; }

}
