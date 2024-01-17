package pmc.gui.common;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pmc.be.rest.omdb.OMDBMovieEntity;
import pmc.be.rest.tmdb.TMDBCreditEntity;
import pmc.be.rest.tmdb.TMDBGenreEntity;
import pmc.be.rest.tmdb.TMDBMovieEntity;
import pmc.be.rest.tmdb.TMDBVideoEntity;
import pmc.dal.rest.tmdb.extra.TMDBLang;

import java.util.List;
import java.util.function.Consumer;

public class MovieDetailsModel {
    private final IntegerProperty tmdbid = new SimpleIntegerProperty();
    private final StringProperty imdbId = new SimpleStringProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty originalTitle = new SimpleStringProperty();
    private final StringProperty tmdbRating = new SimpleStringProperty();
    private final StringProperty imdbRating = new SimpleStringProperty();
    private final StringProperty posterPath = new SimpleStringProperty();
    private final StringProperty director = new SimpleStringProperty();
    private final StringProperty release = new SimpleStringProperty();
    private final StringProperty runtime = new SimpleStringProperty();
    private final StringProperty rated = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();

    private final ObservableList<TMDBGenreEntity> genres = FXCollections.observableArrayList();
    private final ObservableList<TMDBCreditEntity> credits = FXCollections.observableArrayList();
    private final ObservableList<TMDBVideoEntity> videos = FXCollections.observableArrayList();

    public MovieDetailsModel() {}

    public MovieDetailsModel(TMDBMovieEntity tmdbMovie, OMDBMovieEntity omdbMovie) {
        // tmdbMovie
        if (tmdbMovie == null) return;
        if (omdbMovie == null) return;

        this.tmdbid.set(tmdbMovie.getID());
        this.title.set(tmdbMovie.getTitle());
        this.originalTitle.set(tmdbMovie.getOriginalTitle());
        this.tmdbRating.set(String.valueOf(tmdbMovie.getID()));
        this.posterPath.set(tmdbMovie.getPosterPath());
        this.description.set(tmdbMovie.getDescription());
        this.genres.setAll(tmdbMovie.getGenres());
        this.credits.setAll(tmdbMovie.getCredits());
        this.videos.setAll(tmdbMovie.getVideos());

        // omdbMovie
        this.imdbId.set(omdbMovie.getImdbID());
        this.imdbRating.set(omdbMovie.getImdbRating());
        this.director.set(omdbMovie.getDirector());
        this.director.set(omdbMovie.getDirector());
        this.runtime.set(omdbMovie.getRuntime());
        this.rated.set(omdbMovie.getRated());
        this.release.set(omdbMovie.getReleaseYear()); //getReleased outputter specifik dato f.eks. 16. Jan 2024

    }

    public MovieDetailsModel(String imdbId,
                             String title,
                             String originalTitle,
                             String tmdbRating,
                             String imdbRating,
                             String posterPath,
                             String director,
                             String release,
                             String runtime,
                             String rated,
                             String description,
                             List<TMDBGenreEntity> genres,
                             List<TMDBCreditEntity> credits) {
        this.imdbId.set(imdbId);
        this.title.set(title);
        this.originalTitle.set(originalTitle);
        this.tmdbRating.set(tmdbRating);
        this.imdbRating.set(imdbRating);
        this.posterPath.set(posterPath);
        this.director.set(director);
        this.release.set(release);
        this.runtime.set(runtime);
        this.rated.set(rated);
        this.description.set(description);
        this.genres.setAll(genres);
        this.credits.setAll(credits);
    }

    public IntegerProperty tmdbIdProperty() {
        return tmdbid;
    }

    public StringProperty imdbIdProperty() {
        return imdbId;
    }

    public StringProperty titleProperty() {
        return title;
    }

    public StringProperty originalTitleProperty() {
        return originalTitle;
    }

    public StringProperty tmdbRatingProperty() {
        return tmdbRating;
    }

    public StringProperty imdbRatingProperty() {
        return imdbRating;
    }

    public StringProperty posterPathProperty() {
        return posterPath;
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

    public StringProperty descriptionProperty() {
        return description;
    }

    public ObservableList<TMDBGenreEntity> genresProperty() {
        return genres;
    }

    public ObservableList<TMDBCreditEntity> creditProperty() {
        return credits;
    }

    public ObservableList<TMDBVideoEntity> videoProperty() {
        return videos;
    }

}
