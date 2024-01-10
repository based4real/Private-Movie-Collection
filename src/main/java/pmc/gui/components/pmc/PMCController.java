package pmc.gui.components.pmc;

import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Builder;
import pmc.be.Movie;
import pmc.bll.MovieManager;
import pmc.gui.common.IViewController;
import pmc.gui.common.MovieModel;
import pmc.gui.components.categories.CategoriesController;
import pmc.gui.components.home.HomeController;

import java.util.ArrayList;
import java.util.List;

/**
 * Hoved Controller for Private Movie Collection (PMC) applikationen.<br>
 * Fungerer som den centrale koordinator hvor den styrer applikationens flow og integrerer forskellige MVC komponenter.
 */
public class PMCController implements IViewController {
    private final PMCModel model;
    private final Builder<Region> viewBuilder;
    private final MovieManager movieManager;

    public PMCController(Stage stage) {
        this.model = new PMCModel();
        this.movieManager = new MovieManager();
        this.viewBuilder = new PMCViewBuilder(model,
                new HomeController(model.movieModels()).getView(),
                new CategoriesController().getView());

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
            System.out.println(error.getMessage()); // todo: skal ændres så det bliver vist til brugeren
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
}
