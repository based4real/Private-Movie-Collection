package pmc.gui.components.pmc;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
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
import pmc.gui.components.dialog.editmovie.EditMovieController;
import pmc.gui.components.dialog.editmovie.EditMovieData;
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
import pmc.utils.PMCException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
            this.tmdbGenreManager = new TMDBGenreManager();
        } catch (PMCException e) {
            ErrorHandler.showErrorDialog("Fejl", e.getMessage());
        }

        this.tmdbMovieManager = new TMDBMovieManager();

        MoviePosterActions moviePosterActions = new MoviePosterActions(
                this::handleMoviePosterClick,
                this::handlePlayButtonClick,
                this::deleteMovie,
                this::showEditMovieDialog
        );

        this.homeController = new HomeController(model.movieModels(), model.categoryModels(), moviePosterActions);
        this.genresController = new GenresController(model.genreModels(), this::handleGenreCategoryClick);
        this.categoriesController = new CategoriesController(model.categoryModels(), this::handleGenreCategoryClick);
        this.playbackController = new PlaybackController(viewHandler::previousView);
        this.infoController = new InfoController(model.genreModels(), this::handlePlayButtonClick, this::handleGenreCategoryClick);
        this.moviesController = new MoviesController(model.movieModels(), moviePosterActions);

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
                    } catch (PMCException e) {
                        throw new RuntimeException(e); // Træls
                    }
                },
                error -> ErrorHandler.showErrorDialog("Fejl", "Der var et problem at hente data: " + error.getMessage())
        );
    }

    private List<GenresModel> convertToGenreModels(List<Genre> genres) throws PMCException {
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
                    } catch (PMCException e) {
                        throw new RuntimeException(e); // Træls
                    }
                },
                error -> ErrorHandler.showErrorDialog("Fejl", "Der var et problem at hente data: " + error.getMessage())
        );
    }

    private List<CategoriesModel> convertToCategoryModels(List<Category> categories) throws PMCException {
        List<CategoriesModel> categoriesModels = new ArrayList<>();

        for (Category category : categories) {
            categoriesModels.add(new CategoriesModel(
                    category,
                    model.movieModels()
            ));
        }

        return categoriesModels;
    }

    private void handleMoviePosterClick(MovieModel movieModel) {
        viewHandler.changeView(ViewType.INFO);
        infoController.setModel(movieModel);

        performBackgroundTask(
                () -> {
                    TMDBMovieEntity tmdbMovie = tmdbMovieManager.getTMDBMovie(movieModel.tmdbIdProperty().get());
                    OMDBMovieEntity omdbMovie = tmdbMovie.getOMDBMovie();
                    return new MovieDetailsModel(tmdbMovie, omdbMovie);
                },
                detailsModel -> Platform.runLater(() -> infoController.setDetailsModel(detailsModel)),
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

    private void showEditMovieDialog(MovieModel movieModel) {

        EditMovieController editMovieController = new EditMovieController(
                movieModel, model.categoryModels(), this::handleCategoryUpdate
        );

        Dialog<EditMovieData> dialog = showDialog(editMovieController, "Ret film");
        dialog.showAndWait();
        model.isDialogOpenProperty().set(false);
    }

    private void handleCategoryUpdate(EditMovieData data) {
        try { // todo: skal køre på baggrundstråd
            Movie movie = data.movieModel().toEntity();
            List<Category> currentCategories = new ArrayList<>(data.movieModel().categoryObservableList());
            int categoryId = data.categoryId();

            List<Category> updatedCategories = movieManager.updateMovieCategories(movie, currentCategories, categoryId);
            data.movieModel().categoryObservableList().setAll(updatedCategories);
            updateCategoryModels(data.movieModel());
        } catch (PMCException e) {
            ErrorHandler.showErrorDialog("Fejl", e.getMessage());
        }
    }

    private void updateCategoryModels(MovieModel updatedMovie) {
        for (CategoriesModel categoriesModel : model.categoryModels()) {
            boolean isInCategory = updatedMovie.categoryObservableList().stream()
                    .anyMatch(category -> category.getId() == categoriesModel.idProperty().get());
            boolean isInMatching = categoriesModel.getMovies().contains(updatedMovie);

            if (isInCategory && !isInMatching) {
                categoriesModel.getMovies().add(updatedMovie);
            } else if (!isInCategory && isInMatching) {
                categoriesModel.getMovies().remove(updatedMovie);
            }
        }
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
                movie -> {
                    downloadAndCopyFiles(addMovieData, movie);
                    updateGenreModels(movie.getGenres());
                },
                error -> ErrorHandler.showErrorDialog("Fejl", "Database fejl: " + error.getMessage())
        );
    }

    private void updateGenreModels(List<Genre> genres) {
        try { // todo: skal køres på baggrundstråd
            List<GenresModel> newGenres = convertToGenreModels(genres);
            for (GenresModel genreModel : newGenres) {
                if (model.genreModels().stream().noneMatch(g -> g.idProperty().get() == genreModel.idProperty().get())) {
                    model.genreModels().add(genreModel);
                }
            }
        } catch (PMCException e) {
            ErrorHandler.showErrorDialog("Fejl", "Kunne ikke hente genrer fra TMDb: " + e.getMessage());
        }
    }


    private void deleteMovie(MovieModel movieModel) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Bekræft sletning");
        alert.setHeaderText("Slet film");
        alert.setContentText("Er du sikker på du vil slette filmen: " + movieModel.titleProperty().get() + " ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try { // todo: skal køres på en baggrundstråd
                movieManager.deleteMovie(movieModel.toEntity());
                updateGenresOnMovieDeletion(movieModel);
                FileManagementService.deleteFile(movieModel.filePathProperty().get());
                FileManagementService.deleteFile(movieModel.posterPathProperty().get());
                model.movieModels().remove(movieModel);
            } catch (PMCException | IOException e) {
                ErrorHandler.showErrorDialog("Fejl", e.getMessage());
            }
        }
    }

    private void updateGenresOnMovieDeletion(MovieModel movieModel) {
        for (Genre genre : movieModel.genreObservableList()) {
            GenresModel genresModel = findGenresModelById(genre.getTmdbId());
            if (genresModel != null) {
                genresModel.getMovies().remove(movieModel);
            }
        }
    }

    private GenresModel findGenresModelById(int genreId) {
        for (GenresModel genresModel : model.genreModels()) {
            if (genresModel.idProperty().get() == genreId) {
                return genresModel;
            }
        }
        return null;
    }

    private void updateMovie(MovieModel movieModel) {
        System.out.println("ret ret ret! fikser når jeg står op");
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
        }  catch (PMCException e) {
            ErrorHandler.showErrorDialog("Fejl", e.getMessage());
        }
    }

    private void addCategory(String categoryName, ObservableList<Category> categories) {
        try { // todo: skal køre på baggrundstråd
            if (!categoryManager.isValidName(categoryName)) {
                ErrorHandler.showErrorDialog("Fejl", "Kategori navn ikke gyldig");
                return;
            }

            Category category = categoryManager.addCategory(new Category(categoryName));
            categories.add(category);
            model.categoryModels().add(new CategoriesModel(category));
        } catch (PMCException e) {
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
        } catch (PMCException e) {
            ErrorHandler.showErrorDialog("Fejl", e.getMessage());
        }
    }

    private void updateCategory(Category category, String newTitle, ObservableList<Category> categories) {
        try { // todo: skal køre på baggrundstråd
            if (!categoryManager.isValidName(newTitle)) {
                ErrorHandler.showErrorDialog("Fejl", "Kategori navn ikke gyldig");
                return;
            }

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
        } catch (PMCException e) {
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