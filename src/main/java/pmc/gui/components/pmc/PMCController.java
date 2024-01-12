package pmc.gui.components.pmc;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Builder;
import pmc.be.Movie;
import pmc.be.rest.tmdb.TMDBMovieEntity;
import pmc.bll.MovieManager;
import pmc.bll.TMDBMovieManager;
import pmc.gui.common.IViewController;
import pmc.gui.common.MovieDetailsModel;
import pmc.gui.common.MovieModel;
import pmc.gui.components.categories.CategoriesController;
import pmc.gui.components.home.HomeController;
import pmc.gui.components.info.InfoController;
import pmc.gui.components.playback.PlaybackController;
import pmc.gui.utils.ErrorHandler;
import pmc.utils.MovieException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * Hoved Controller for Private Movie Collection (PMC) applikationen.<br>
 * Fungerer som den centrale koordinator hvor den styrer applikationens flow og integrerer forskellige MVC komponenter.
 */
public class PMCController implements IViewController {
    private final PMCModel model;
    private final Builder<Region> viewBuilder;
    private final ViewHandler viewHandler;

    private final HomeController homeController;
    private final CategoriesController categoriesController;
    private final InfoController infoController;
    private final PlaybackController playbackController;

    private MovieManager movieManager;
    private TMDBMovieManager tmdbMovieManager;

    public PMCController(Stage stage) {
        this.model = new PMCModel();
        this.viewHandler = new ViewHandler(model);


        try {
            this.movieManager = new MovieManager();
        } catch (MovieException e) {
            ErrorHandler.showErrorDialog("Fejl", e.getMessage());
        }

        this.tmdbMovieManager = new TMDBMovieManager();

        this.homeController = new HomeController(model.movieModels(), this::handleMoviePosterClick, this::handlePlayButtonClick);
        this.categoriesController = new CategoriesController();
        this.infoController = new InfoController();
        this.playbackController = new PlaybackController(viewHandler::previousView);

        this.viewBuilder = new PMCViewBuilder(model, viewHandler, this::handleAddMovieResponse,
                homeController.getView(),
                categoriesController.getView(),
                infoController.getView(),
                playbackController.getView());

        fetchData();
    }

    @Override
    public Region getView() {
        return viewBuilder.build();
    }

    private void fetchData() {
        performBackgroundTask(
                () -> movieManager.getAllMovies(),
                movies -> model.movieModels().setAll(convertToMovieModels(movies)),
                error -> ErrorHandler.showErrorDialog("Fejl", "Der var et problem at hente data: " + error.getMessage())
        );
    }

    private List<MovieModel> convertToMovieModels(List<Movie> movies) {
        List<MovieModel> movieModels = new ArrayList<>();

        for (Movie movie : movies) {
            movieModels.add(new MovieModel(
                    movie.getTmdbId(),
                    movie.getPosterPath(),
                    movie.getFilePath())
            );
        }

        return movieModels;
    }

    private MovieDetailsModel convertToMovieDetailsModel(TMDBMovieEntity movie) {
        return new MovieDetailsModel(movie.getDescription());
    }

    private void handleMoviePosterClick(MovieModel movieModel) {
        viewHandler.changeView(ViewType.INFO);
        infoController.setModel(movieModel);

        performBackgroundTask(
                () -> tmdbMovieManager.getTMDBMovie(movieModel.tmdbIdProperty().get()),
                tmdbMovie -> Platform.runLater(() -> infoController.setDetailsModel(convertToMovieDetailsModel(tmdbMovie))),
                error -> ErrorHandler.showErrorDialog("Fejl", "Der var et problem med at hente data: " + error.getMessage())
        );
    }

    private void handlePlayButtonClick(MovieModel movieModel) {
        playbackController.setModel(movieModel);
        viewHandler.changeView(ViewType.PLAYBACK);
    }

    private void handleAddMovieResponse(MovieModel movieModel) { // todo: skal nok ikke være MovieModel bare lige for at teste
        System.out.println("håndter tilføj movie dialog respons");
    }

    private <T> void performBackgroundTask(Callable<T> task, Consumer<T> onSuccess, Consumer<Exception> onError) {
        // Hent data i baggrunden så vi ikke bloker Java FX Application Thread (FXAT), hvis forbindelsen f.eks. er langsom
        Task<T> backgroundTask = new Task<T>() {
            @Override
            protected T call() throws Exception {
                return task.call();
            }
        };

        // Håndter successful hentning af data
        backgroundTask.setOnSucceeded(event -> {
            T result = backgroundTask.getValue();
            onSuccess.accept(result);
        });

        // Håndter exceptions
        backgroundTask.setOnFailed(event -> {
            Throwable error = backgroundTask.getException();
            onError.accept((Exception) error);
        });

        // Start ny tråd
        new Thread(backgroundTask).start();
    }
}