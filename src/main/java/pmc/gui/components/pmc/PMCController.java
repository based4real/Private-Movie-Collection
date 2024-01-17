package pmc.gui.components.pmc;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Builder;
import pmc.be.Category;
import pmc.be.Genre;
import pmc.be.Movie;
import pmc.be.rest.omdb.OMDBMovieEntity;
import pmc.be.rest.tmdb.TMDBGenreEntity;
import pmc.be.rest.tmdb.TMDBMovieEntity;
import pmc.bll.*;
import pmc.gui.common.*;
import pmc.gui.components.categories.CategoriesModel;
import pmc.gui.components.dialog.addcategory.AddCategoryData;
import pmc.gui.components.dialog.addcategory.CategoryActions;
import pmc.gui.components.dialog.addmovie.AddMovieData;
import pmc.gui.components.genres.GenresModel;
import pmc.gui.components.genres.GenresController;
import pmc.gui.components.categories.CategoriesController;
import pmc.gui.components.dialog.DialogBuilder;
import pmc.gui.components.dialog.IDialogController;
import pmc.gui.components.dialog.addcategory.AddCategoryController;
import pmc.gui.components.dialog.addmovie.AddMovieController;
import pmc.gui.components.home.HomeController;
import pmc.gui.components.info.InfoController;
import pmc.gui.components.movies.MoviesController;
import pmc.gui.components.playback.PlaybackController;
import pmc.gui.utils.ErrorHandler;
import pmc.gui.utils.FileManagementService;
import pmc.utils.MovieException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * Hoved Controller for Private Movie Collection (PMC) applikationen.<br>
 * Fungerer som den centrale koordinator hvor den styrer applikationens flow og integrerer forskellige MVC komponenter.
 */
public class PMCController implements IViewController {
    private final Stage stage;
    private final PMCModel model;
    private final Builder<Region> viewBuilder;
    private final ViewHandler viewHandler;

    private final HomeController homeController;
    private final GenresController genresController;
    private final CategoriesController categoriesController;
    private final InfoController infoController;
    private final PlaybackController playbackController;
    private final MoviesController moviesController;

    private AddMovieController addMovieController;

    private MovieManager movieManager;
    private GenreManager genreManager;
    private CategoryManager categoryManager;
    private TMDBGenreManager tmdbGenreManager;
    private TMDBMovieManager tmdbMovieManager;

    public PMCController(Stage stage) {
        this.stage = stage;
        this.model = new PMCModel();
        this.viewHandler = new ViewHandler(model);

        try {
            this.movieManager = new MovieManager();
            this.genreManager = new GenreManager();
            this.categoryManager = new CategoryManager();
        } catch (MovieException e) {
            ErrorHandler.showErrorDialog("Fejl", e.getMessage());
        }

        this.tmdbMovieManager = new TMDBMovieManager();
        this.tmdbGenreManager = new TMDBGenreManager();

        this.homeController = new HomeController(model.movieModels(), model.categoryModels(), this::handleMoviePosterClick, this::handlePlayButtonClick);
        this.genresController = new GenresController(model.genreModels(), this::handleGenreCategoryClick);
        this.categoriesController = new CategoriesController(model.categoryModels(), this::handleGenreCategoryClick);
        this.playbackController = new PlaybackController(viewHandler::previousView);
        this.infoController = new InfoController(this::handlePlayButtonClick);
        this.moviesController = new MoviesController(this::handleMoviePosterClick, this::handlePlayButtonClick);

        this.viewBuilder = new PMCViewBuilder(model, viewHandler, this::showAddMovieDialog, this::showAddCategoryDialog,
                homeController.getView(),
                genresController.getView(),
                categoriesController.getView(),
                infoController.getView(),
                playbackController.getView(),
                moviesController.getView());

        fetchData();
        fetchDataGenre();
        fetchDataCategory();
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
            movieModels.add(new MovieModel(movie));
        }

        return movieModels;
    }

    private void fetchDataGenre() {
        performBackgroundTask(
                () -> genreManager.getAllGenres(),
                genres -> {
                    try {
                        model.genreModels().setAll(convertToGenreModels(genres));
                    } catch (MovieException e) {
                        throw new RuntimeException(e); // Træls
                    }
                },
                error -> ErrorHandler.showErrorDialog("Fejl", "Der var et problem at hente data: " + error.getMessage())
        );
    }

    private List<GenresModel> convertToGenreModels(List<Genre> genres) throws MovieException {
        List<GenresModel> genreModels = new ArrayList<>();

        for (Genre genre : genres) {
            TMDBGenreEntity tmdbGenre = tmdbGenreManager.getTMDBFromGenre(genre);

            genreModels.add(new GenresModel(
                    tmdbGenre,
                    model.movieModels()
            ));
        }

        return genreModels;
    }

    private void fetchDataCategory() {
        performBackgroundTask(
                () -> categoryManager.getAllCategories(),
                categories -> {
                    try {
                        model.categoryModels().setAll(convertToCategoryModels(categories));
                    } catch (MovieException e) {
                        throw new RuntimeException(e); // Træls
                    }
                },
                error -> ErrorHandler.showErrorDialog("Fejl", "Der var et problem at hente data: " + error.getMessage())
        );
    }

    private List<CategoriesModel> convertToCategoryModels(List<Category> categories) throws MovieException {
        List<CategoriesModel> categoriesModels = new ArrayList<>();

        for (Category category : categories) {
            categoriesModels.add(new CategoriesModel(
                    category,
                    model.movieModels()
            ));
        }

        return categoriesModels;
    }

    private MovieDetailsModel convertToMovieDetailsModel(TMDBMovieEntity tmdbMovie) {
        model.backdropPathProperty().set("https://image.tmdb.org/t/p/original" + tmdbMovie.getBackdropPath());
        OMDBMovieEntity omdbMovie = tmdbMovie.getOMDBMovie(); // todo: kører ikke på baggrundstråd kan blokere GUI

        System.out.println("TMDBMovie: " + tmdbMovie);
        System.out.println("OMDBMovie: " + omdbMovie);
        return new MovieDetailsModel(tmdbMovie, omdbMovie);
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

    private void handleGenreCategoryClick(MoviesData moviedata) {
        moviesController.setModel(moviedata);
        viewHandler.changeView(ViewType.MOVIES);
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

    private void showAddMovieDialog() {
        addMovieController = new AddMovieController(this::tmdbSearch);
        Dialog<AddMovieData> dialog = showDialog(addMovieController, "Tilføj film");
        dialog.showAndWait().ifPresent(this::addMovie);
        model.isDialogOpenProperty().set(false);
    }

    private void tmdbSearch(String title) {
        performBackgroundTask(
                () -> {
                    TMDBMovieEntity tmdbMovie = tmdbMovieManager.searchForMovie(title);
                    OMDBMovieEntity omdbMovie = tmdbMovie.getOMDBMovie();
                    return new MovieDetailsModel(tmdbMovie, omdbMovie);
                },
                movieDetails -> addMovieController.setTMDBMovie(movieDetails),
                error -> ErrorHandler.showErrorDialog("Fejl", "Filmen kunne ikke findes. Prøv igen eller en anden.")
        );
    }

    private void addMovie(AddMovieData addMovieData) {
        performBackgroundTask(
                () -> movieManager.addMovieWithGenres(addMovieData),
                movie -> downloadAndCopyFiles(addMovieData, movie),
                error -> ErrorHandler.showErrorDialog("Fejl", "Database fejl: " + error.getMessage())
        );
    }

    private void downloadAndCopyFiles(AddMovieData addMovieData, Movie movie) {
        performBackgroundTask(
                () -> {
                    Platform.runLater(() -> {
                        model.copyingFileProperty().set(true);
                        model.fileProgressProperty().set(0);
                    });
                    FileManagementService.downloadImageToDir(addMovieData.posterUrl(), "data/posters", addMovieData.posterPath(), 250, 374);
                    FileManagementService.copyFileToDir(new File(addMovieData.filePath()), "data/movies", this::updateTaskProgress);
                    Platform.runLater(() -> {
                        model.fileProgressProperty().set(0);
                        model.copyingFileProperty().set(false);
                    });
                    return true;
                },
                results -> model.movieModels().add(new MovieModel(movie)),
                error -> ErrorHandler.showErrorDialog("Fejl", "fil fejl: " + error.getMessage()));
    }

    private void updateTaskProgress(double progress) {
        Platform.runLater(() -> model.fileProgressProperty().set(progress));
    }

    private void showAddCategoryDialog() {
        try {
            ObservableList<Category> categories = FXCollections.observableArrayList(categoryManager.getAllCategories());


            CategoryActions actions = new CategoryActions(
                    categoryName -> addCategory(categoryName, categories),
                    category -> deleteCategory(category, categories),
                    (category, newTitle) -> updateCategory(category, newTitle, categories)
            );

            AddCategoryController controller = new AddCategoryController(actions, categories);

            Dialog<AddCategoryData> dialog = showDialog(controller, "Tilføj kategori");
            dialog.showAndWait();
            model.isDialogOpenProperty().set(false);
        }  catch (MovieException e) {
            ErrorHandler.showErrorDialog("Fejl", e.getMessage());
        }
    }

    private void addCategory(String categoryName, ObservableList<Category> categories) {
        try { // todo: skal køre på baggrundstråd
            Category category = categoryManager.addCategory(new Category(categoryName));
            categories.add(category);
            model.categoryModels().add(new CategoriesModel(category));
        } catch (MovieException e) {
            ErrorHandler.showErrorDialog("Fejl", e.getMessage());
        }
    }

    private void deleteCategory(Category category, ObservableList<Category> categories) {
        try { // todo: skal køre på baggrundstråd
            categoryManager.deleteCategory(category);
            categories.remove(category);
            CategoriesModel toRemove = null;
            for (CategoriesModel categoriesModel : model.categoryModels()) {
                if (categoriesModel.idProperty().get() == category.getId()) {
                    toRemove = categoriesModel;
                    break;
                }
            }

            if (toRemove != null) model.categoryModels().remove(toRemove);
        } catch (MovieException e) {
            ErrorHandler.showErrorDialog("Fejl", e.getMessage());
        }
    }

    private void updateCategory(Category category, String newTitle, ObservableList<Category> categories) {
        try { // todo: skal køre på baggrundstråd
            boolean isUpdated = categoryManager.updateCategory(category, new Category(newTitle));
            if (!isUpdated) return;

            for (Category cat : categories) {
                if (cat.getId() == category.getId()) {
                    cat.setName(newTitle);
                }
            }

            for (CategoriesModel cat : model.categoryModels()) {
                if (cat.idProperty().get() == category.getId()) {
                    cat.nameProperty().set(newTitle);
                }
            }
        } catch (MovieException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> Dialog<T> showDialog(IDialogController<T> controller, String title) {
        model.isDialogOpenProperty().set(true);
        Dialog<T> dialog = new DialogBuilder<>(controller)
                .withTitle(title)
                .addButtonTypes(ButtonType.CANCEL, ButtonType.OK)
                .build();

        setupDialogButtons(dialog);
        setupDialogStyle(dialog);

        double dialogWidth = 600;
        double dialogHeight = 400;

        dialog.getDialogPane().setPrefSize(dialogWidth, dialogHeight);

        dialog.setOnShown(event -> {
            dialog.setX(stage.getX() + (stage.getWidth() / 2) - (dialogWidth / 2));
            dialog.setY(stage.getY() + (stage.getHeight() / 2) - (dialogHeight / 2));
        });

        return dialog;
    }

    private void setupDialogButtons(Dialog<?> dialog) {
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.getStyleClass().add("ok-button");

        Button cancelButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.getStyleClass().add("cancel-button");
    }

    private void setupDialogStyle(Dialog<?> dialog) {
        dialog.getDialogPane().getScene().getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/css/theme.css")).toExternalForm()
        );
        dialog.getDialogPane().getScene().getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/css/pmc.css")).toExternalForm()
        );
    }
}