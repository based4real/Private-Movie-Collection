package pmc.gui.components.pmc;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Builder;
import pmc.be.Movie;
import pmc.bll.MovieManager;
import pmc.gui.common.IViewController;
import pmc.gui.common.MovieModel;
import pmc.gui.components.categories.CategoriesController;
import pmc.gui.components.dialog.DialogBuilder;
import pmc.gui.components.dialog.addmovie.AddMovieController;
import pmc.gui.components.home.HomeController;
import pmc.gui.components.info.InfoController;
import pmc.gui.components.info.InfoViewBuilder;
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

    private final HomeController homeController;
    private final CategoriesController categoriesController;
    private final InfoController infoController;

    private final MovieManager movieManager;

    public PMCController(Stage stage) {
        this.model = new PMCModel();
        this.movieManager = new MovieManager();

        this.homeController = new HomeController(model.movieModels(), this::handleMoviePosterClick);
        this.categoriesController = new CategoriesController();
        this.infoController = new InfoController();

        this.viewBuilder = new PMCViewBuilder(model, this::handleAddMovieResponse,
                homeController.getView(),
                categoriesController.getView(),
                infoController.getView());

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
            protected List<Movie> call() throws Exception {
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
            errorDialog("Database fejl", "Der var et problem at hente data: " + error.getMessage());
        });

        new Thread(fetchTask).start();
    }

    private List<MovieModel> convertToMovieModels(List<Movie> movies) {
        List<MovieModel> movieModels = new ArrayList<>();

        for (Movie movie : movies) {
            movieModels.add(new MovieModel(movie.getPosterPath()));
        }

        return movieModels;
    }

    private void errorDialog(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void handleMoviePosterClick(MovieModel movieModel) {
        infoController.setModel(movieModel);
        model.activeViewProperty().set(ViewType.INFO);
    }

    private void handleAddMovieResponse(MovieModel movieModel) { // skal ikke være MovieModel bare lige for at teste
        System.out.println("hej");
    }
}
