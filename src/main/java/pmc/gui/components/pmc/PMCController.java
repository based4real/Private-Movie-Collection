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
import pmc.utils.MovieException;

import java.util.ArrayList;
import java.util.List;

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
            errorDialog("Fejl", e.getMessage());
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
        // Hent data asynkront så vi ikke bloker Java FX Application Thread (FXAT), hvis forbindelsen f.eks. er langsom
        Task<List<Movie>> fetchTask = new Task<>() {
            @Override
            protected List<Movie> call() throws MovieException {
                return movieManager.getAllMovies();
            }
        };

        // Håndter successful hentning af data
        fetchTask.setOnSucceeded(evt -> {
            model.movieModels().setAll(convertToMovieModels(fetchTask.getValue()));
        });

        // Håndter exceptions
        fetchTask.setOnFailed(evt -> {
            Throwable error = fetchTask.getException();
            errorDialog("Fejl", "Der var et problem at hente data: " + error.getMessage());
        });

        new Thread(fetchTask).start();
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

    private void errorDialog(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void handleMoviePosterClick(MovieModel movieModel) {
        Task<MovieDetailsModel> fetchTask = new Task<>() {
            @Override
            protected MovieDetailsModel call() throws Exception {
                TMDBMovieEntity tmdbMovie = tmdbMovieManager.getTMDBMovie(movieModel.tmdbIdProperty().get());
                return convertToMovieDetailsModel(tmdbMovie);
            }
        };

        fetchTask.setOnSucceeded(evt -> {
            MovieDetailsModel movieDetails = fetchTask.getValue();
            Platform.runLater(() -> {
                infoController.setModel(movieModel, movieDetails);
                viewHandler.changeView(ViewType.INFO);
            });
        });

        fetchTask.setOnFailed(EVT -> {
            Throwable error = fetchTask.getException();
            errorDialog("Fejl", "Der var et problem med at hente data: " + error.getMessage());
        });

        new Thread(fetchTask).start();
    }

    private void handlePlayButtonClick(MovieModel movieModel) {
        playbackController.setModel(movieModel);
        viewHandler.changeView(ViewType.PLAYBACK);
    }

    private void handleAddMovieResponse(MovieModel movieModel) { // todo: skal nok ikke være MovieModel bare lige for at teste
        System.out.println("håndter tilføj movie dialog respons");
    }
}
