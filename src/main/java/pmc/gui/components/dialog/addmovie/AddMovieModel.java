package pmc.gui.components.dialog.addmovie;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pmc.be.rest.tmdb.TMDBGenreEntity;

public class AddMovieModel {
    private final BooleanProperty chooseFileView = new SimpleBooleanProperty(true);
    private final BooleanProperty fileChosenView = new SimpleBooleanProperty(false);
    private final BooleanProperty chosenFile = new SimpleBooleanProperty(false);

    private final StringProperty imdbId = new SimpleStringProperty("");
    private final StringProperty title = new SimpleStringProperty("");
    private final StringProperty originalTitle = new SimpleStringProperty("");
    private final StringProperty tmdbRating = new SimpleStringProperty("");
    private final StringProperty imdbRating = new SimpleStringProperty("");

    private final StringProperty description = new SimpleStringProperty("");

    private final StringProperty posterPath = new SimpleStringProperty("");
    private final StringProperty posterUrl = new SimpleStringProperty("");

    private ObservableList<TMDBGenreEntity> genres = FXCollections.observableArrayList();

    private final StringProperty fileName = new SimpleStringProperty("");
    private final StringProperty filePath = new SimpleStringProperty("");
    private final StringProperty absoluteFilePath = new SimpleStringProperty("");

    public void resetFetchedData() {
        imdbId.set("");
        title.set("");
        originalTitle.set("");
        tmdbRating.set("");
        imdbRating.set("");
        posterPath.set("");
        description.set("");
        posterUrl.set("");
        genres = FXCollections.observableArrayList();
    }

    public BooleanProperty chooseFileViewProperty() {
        return chooseFileView;
    }

    public BooleanProperty fileChosenViewProperty() {
        return fileChosenView;
    }

    public BooleanProperty chosenFileProperty() {
        return chosenFile;
    }

    public StringProperty fileNameProperty() {
        return fileName;
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

    public StringProperty descriptionProperty() {
        return description;
    }

    public StringProperty filePathProperty() {
        return filePath;
    }

    public StringProperty absoluteFilePathProperty() {
        return absoluteFilePath;
    }

    public StringProperty posterUrlProperty() {
        return posterUrl;
    }

    public ObservableList<TMDBGenreEntity> getGenres() {
        return genres;
    }

    public void setGenres(ObservableList<TMDBGenreEntity> genres) {
        this.genres = genres;
    }
}