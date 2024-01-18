package pmc.gui.components.info;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import pmc.be.Movie;
import pmc.gui.common.*;
import pmc.gui.components.genres.GenresModel;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class InfoController implements IViewController {
    private final InfoModel model;
    private final Builder<Region> viewBuilder;

    private ObservableList<MovieDetailsModel> movieDetails = FXCollections.observableArrayList();

    public InfoController(ObservableList<GenresModel> genreModels,
                          Consumer<MovieModel> playMovieHandler,
                          Consumer<MoviesData> viewChangeHandler,
                          Consumer<MovieUpdate> movieUpdate) {
        model = new InfoModel(genreModels);
        this.viewBuilder = new InfoViewBuilder(model, playMovieHandler, viewChangeHandler, movieUpdate);
    }

    @Override
    public Region getView() {
        return viewBuilder.build();
    }

    public void setModel(MovieModel model) {
        this.model.setMovieModel(model);
        this.model.posterPathProperty().set(model.posterPathProperty().get());
    }

    public FilteredList<MovieDetailsModel> getCachedList() {
        Predicate<MovieDetailsModel> predicate = movieDetails ->
                movieDetails.tmdbIdProperty().get() == model.movieModelProperty().get().tmdbIdProperty().get();

        return new FilteredList<>(movieDetailsList(), predicate);
    }

    public MovieDetailsModel getCachedMovie() {
        return getCachedList().getFirst();
    }

    public boolean isCached() {
        return !getCachedList().isEmpty();
    }

    public ObservableList<MovieDetailsModel> movieDetailsList() {
        return movieDetails;
    }

    public void setDetailsModel(MovieDetailsModel detailsModel) {
        if (this.model.tmdbIdProperty().get() != detailsModel.tmdbIdProperty().get()) {
            this.model.tmdbIdProperty().set(detailsModel.tmdbIdProperty().get());
            this.model.titleProperty().set(detailsModel.titleProperty().get());
            this.model.directorProperty().set(detailsModel.directorProperty().get());
            this.model.releaseProperty().set(detailsModel.releaseProperty().get());
            this.model.runtimeProperty().set(detailsModel.runtimeProperty().get());
            this.model.ratedProperty().set(detailsModel.ratedProperty().get());
            this.model.genresProperty().setAll(detailsModel.genresProperty());

            this.model.descriptionProperty().set(detailsModel.descriptionProperty().get());
            this.model.creditsProperty().setAll(detailsModel.creditProperty());
            this.model.videosProperty().setAll(detailsModel.videoProperty());

            if (!getCachedList().contains(detailsModel))
                movieDetails.add(detailsModel);
        }
    }
}