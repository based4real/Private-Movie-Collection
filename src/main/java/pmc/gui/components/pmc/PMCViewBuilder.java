package pmc.gui.components.pmc;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Builder;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2OutlinedAL;
import pmc.gui.common.MovieModel;
import pmc.gui.components.genres.GenresModel;
import pmc.gui.widgets.*;
import pmc.gui.widgets.buttons.ButtonWidgets;
import pmc.gui.widgets.controls.NavigationGroup;
import pmc.gui.widgets.icons.IconWidgets;

import javax.swing.text.View;

/**
 * Ansvarlig for at bygge brugergrænsefladen for det overordnede View i Private Movie Collection (PMC) applikationen.
 */
public class PMCViewBuilder implements Builder<Region> {
    private final PMCModel model;
    private final ViewHandler viewHandler;
    private final Runnable addMovieClick;
    private final Runnable addCategoryClick;
    private final ResourceBundle labelsBundle;

    private final Region homeView;
    private final Region genresView;
    private final Region categoriesView;
    private final Region infoView;
    private final Region playbackView;
    private final Region moviesView;

    public PMCViewBuilder(PMCModel model,
                          ViewHandler viewHandler,
                          Runnable addMovieClick,
                          Runnable addCategoryClick,
                          Region homeView,
                          Region genresView,
                          Region categoriesView,
                          Region infoView,
                          Region playbackView,
                          Region moviesView) {
        this.model = model;
        this.viewHandler = viewHandler;
        this.addMovieClick = addMovieClick;
        this.addCategoryClick = addCategoryClick;
        this.labelsBundle = ResourceBundle.getBundle("bundles.labels", Locale.getDefault());
        this.homeView = homeView;
        this.genresView = genresView;
        this.categoriesView = categoriesView;
        this.infoView = infoView;
        this.playbackView = playbackView;
        this.moviesView = moviesView;
    }

    @Override
    public Region build() {
        StackPane results = new StackPane();
        results.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("/css/theme.css")).toExternalForm());
        //results.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("/css/debugTheme.css")).toExternalForm());
        results.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("/css/pmc.css")).toExternalForm());

        BorderPane main = new BorderPane();
        main.getStyleClass().add("main");
        Region top = createTop();
        Region left = createLeft();
        Region center = createCenter();

        BorderPane.setMargin(top, new Insets(5, 5, 5, 5));
        BorderPane.setMargin(left, new Insets(0, 5, 5, 5));
        BorderPane.setMargin(center, new Insets(0, 5, 5, 0));

        main.setTop(top);
        main.setLeft(left);
        main.setCenter(center);

        // Eksmepl på at få backdrop på baggrund, skal dog laves ordentligt hvis det er
/*        model.backdropPathProperty().addListener((obs, ov, nv) -> {
            System.out.println(nv);
            if (nv != null && !nv.isEmpty()) main.setBackground(ImageWidgets.stretchedBackground(nv));
        });*/

        Pane overlay = new Pane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");

        overlay.visibleProperty().bind(model.isDialogOpenProperty());

        results.getChildren().addAll(main, overlay);
        StackPane.setAlignment(main, Pos.TOP_LEFT);

        return results;
    }

    private Region createTop() {
        Region topbar = createTopbar();

        topbar.visibleProperty().bind(model.activeViewProperty().isNotEqualTo(ViewType.PLAYBACK));
        topbar.managedProperty().bind(model.activeViewProperty().isNotEqualTo(ViewType.PLAYBACK));

        return topbar;
    }

    private Region createLeft() {
        Region sidebar = createSidebar();

        sidebar.visibleProperty().bind(model.activeViewProperty().isNotEqualTo(ViewType.PLAYBACK));
        sidebar.managedProperty().bind(model.activeViewProperty().isNotEqualTo(ViewType.PLAYBACK));

        return sidebar;
    }

    private Region createCenter() {
        homeView.visibleProperty().bind(model.activeViewProperty().isEqualTo(ViewType.HOME));
        genresView.visibleProperty().bind(model.activeViewProperty().isEqualTo(ViewType.GENRES));
        categoriesView.visibleProperty().bind(model.activeViewProperty().isEqualTo(ViewType.CATEGORIES));
        infoView.visibleProperty().bind(model.activeViewProperty().isEqualTo(ViewType.INFO));
        playbackView.visibleProperty().bind(model.activeViewProperty().isEqualTo(ViewType.PLAYBACK));
        moviesView.visibleProperty().bind(model.activeViewProperty().isEqualTo(ViewType.MOVIES));

        infoView.managedProperty().bind(model.activeViewProperty().isEqualTo(ViewType.INFO));

        return new StackPane(homeView, genresView, categoriesView, infoView, playbackView, moviesView);
    }

    private Region createTopbar() {
        HBox results = new HBox();
        results.getStyleClass().add("topbar");

        FontIcon menuIcon = IconWidgets.styledIcon(Material2MZ.MENU, "icon");
        Label pmc = LabelWidgets.styledLabel("PMC", "logo");
        pmc.setWrapText(false);


        ContextMenu settingsMenu = new ContextMenu();
        MenuItem toggleImdb = new MenuItem("Filter IMDb rating");
        MenuItem toggleGenres = new MenuItem("Filter genrer");
        settingsMenu.getItems().addAll(toggleImdb, toggleGenres);

        // Søgefelt
        TextField searchField = new TextField();
        FontIcon searchIcon = IconWidgets.styledIcon(Material2MZ.SEARCH, "icon-textfield-icon", "search-icon");

        Button settingsIcon = ButtonWidgets.styledIconButton(Material2MZ.SETTINGS, "icon-textfield-action");
        settingsIcon.setOnAction(e -> {
            settingsMenu.show(settingsIcon, Side.BOTTOM, 0, 0);
        });

        toggleImdb.setOnAction(e -> {
            System.out.println("Toggle IMDb Rating Field");
        });

        toggleGenres.setOnAction(e -> {
            System.out.println("Toggle Genre ComboBox");
        });

        Platform.runLater(pmc::requestFocus); // bruges til sådan at searchField ikke focused når programmet åbner

        StackPane searchContainer = new StackPane();
        searchContainer.getChildren().addAll(searchField, searchIcon, settingsIcon);
        searchField.getStyleClass().add("icon-textfield");
        StackPane.setAlignment(searchIcon, Pos.CENTER_LEFT);
        StackPane.setAlignment(settingsIcon, Pos.CENTER_RIGHT);

        StackPane.setMargin(searchIcon, new Insets(5));
        StackPane.setMargin(settingsIcon, new Insets(5));

        settingsIcon.visibleProperty().bind(searchField.focusedProperty());

        searchField.focusedProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                searchIcon.setIconColor(Color.BLACK);
            } else {
                searchIcon.setIconColor(Color.WHITE);
            }
        });

        ContextMenu searchResultsMenu = new ContextMenu();

        List<GenresModel> selectedGenres = new ArrayList<>();
        HBox selectedGenresDisplay = new HBox(5);
        selectedGenresDisplay.setAlignment(Pos.CENTER_LEFT);

        // Genre selector
        ComboBox<GenresModel> genreComboBox = new ComboBox<>();
        FilteredList<GenresModel> filteredGenres = new FilteredList<>(model.genreModels(), genre -> true);
        genreComboBox.setItems(filteredGenres);
        genreComboBox.setPromptText("Vælg genre...");

        // Imdb rating felt
        TextField imdbRatingField = new TextField();
        FontIcon imdbIcon = IconWidgets.styledIcon(FontAwesomeBrands.IMDB, "icon-textfield-icon", "imdb-icon");

        StackPane imdbContainer = new StackPane();
        imdbContainer.getChildren().addAll(imdbRatingField, imdbIcon);
        imdbRatingField.getStyleClass().add("icon-textfield");
        imdbRatingField.setPrefWidth(100);
        StackPane.setAlignment(imdbIcon, Pos.CENTER_LEFT);
        StackPane.setMargin(imdbIcon, new Insets(5));


        // gør sådan at når vi 'dropdowner' at det udskriver som genre navn
        genreComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(GenresModel item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                    setVisible(true);
                } else {
                    setText(item.nameProperty().get());
                    setVisible(!selectedGenres.contains(item));
                }
            }
        });


        genreComboBox.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
            if (nv != null && !selectedGenres.contains(nv)) {
                selectedGenres.add(nv);
                // combobx fejl hvis ikke Platform.runLater()
                Platform.runLater(() -> updateComboBoxItems(genreComboBox, selectedGenres, model.genreModels()));
                updateSelectedGenresDisplay(selectedGenresDisplay, selectedGenres, searchField, searchResultsMenu, genreComboBox, imdbRatingField);
            }
        });

        // gør sådan at når vi vælger en genre at den vises som genre navn
        genreComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(GenresModel item, boolean empty) {
                super.updateItem(item, empty);
                setText("Vælg genre...");
            }
        });

        searchField.textProperty().addListener((obs, ov, nv) -> {
            updateSearchResult(nv, searchResultsMenu, searchField, genreComboBox, selectedGenres, imdbRatingField);
        });

        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DOWN && !searchResultsMenu.isShowing()) {
                searchResultsMenu.show(searchField, Side.BOTTOM, 0, 0);
            }
        });

        searchField.focusedProperty().addListener((obs, ov, nv) -> {
            if (nv && searchField.getText().isEmpty()) {
                updateSearchResult("", searchResultsMenu, searchField, genreComboBox, selectedGenres, imdbRatingField);
            }
        });

        imdbRatingField.textProperty().addListener((obs, ov, nv) -> {
            updateSearchResult(searchField.getText(), searchResultsMenu, searchField, genreComboBox, selectedGenres, imdbRatingField);
        });

        Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);

        Button addCategoryIcon = ButtonWidgets.actionIconButton(Material2AL.LIBRARY_ADD, "icon", e -> addCategoryClick.run());
        Button addMovieIcon = ButtonWidgets.actionIconButton(Material2AL.ADD_BOX, "icon", e -> addMovieClick.run());

        addMovieIcon.disableProperty().bind(model.copyingFileProperty());

        ProgressBar progressBar = new ProgressBar(0);
        progressBar.progressProperty().bind(model.fileProgressProperty());
        progressBar.visibleProperty().bind(model.copyingFileProperty());

        results.getChildren().addAll(menuIcon, pmc, searchContainer, imdbContainer, genreComboBox, selectedGenresDisplay, space, progressBar, addCategoryIcon, addMovieIcon);
        results.setAlignment(Pos.CENTER_LEFT);

        return results;
    }

    private void updateComboBoxItems(ComboBox<GenresModel> genreComboBox, List<GenresModel> selectedGenres, ObservableList<GenresModel> allGenres) {
        List<GenresModel> filteredList = allGenres.stream()
                .filter(genre -> !selectedGenres.contains(genre))
                .collect(Collectors.toList());
        genreComboBox.setItems(FXCollections.observableArrayList(filteredList));
    }

    private void updateSelectedGenresDisplay(HBox selectedGenresDisplay, List<GenresModel> selectedGenres,
                                             TextField searchField, ContextMenu searchResultsMenu,
                                             ComboBox<GenresModel> genreComboBox, TextField imdbRatingField) {
        selectedGenresDisplay.getChildren().clear();
        for (GenresModel genreModel : selectedGenres) {
            Label genreLabel = LabelWidgets.styledLabel(genreModel.nameProperty(), "filter-genre-selected");

            Button removeButton = ButtonWidgets.actionIconButton(Material2AL.CLOSE, "filter-genre-remove", e -> {
                selectedGenres.remove(genreModel);
                updateComboBoxItems(genreComboBox, selectedGenres, model.genreModels());
                updateSelectedGenresDisplay(selectedGenresDisplay, selectedGenres, searchField, searchResultsMenu, genreComboBox, imdbRatingField);
                updateSearchResult(searchField.getText(), searchResultsMenu, searchField, genreComboBox, selectedGenres, imdbRatingField);
            });

            HBox genreBox = new HBox(genreLabel, removeButton);
            genreBox.setAlignment(Pos.CENTER_LEFT);
            selectedGenresDisplay.getChildren().add(genreBox);
        }

        updateSearchResult(searchField.getText(), searchResultsMenu, searchField, genreComboBox, selectedGenres, imdbRatingField);

    }

    private void updateSearchResult(String searchQuery, ContextMenu searchResultsMenu,
                                    TextField searchField, ComboBox<GenresModel> genreComboBox,
                                    List<GenresModel> selectedGenres, TextField imdbRatingField) {
        searchResultsMenu.getItems().clear();

        if (searchQuery == null && genreComboBox.getSelectionModel().isEmpty() && imdbRatingField.getText().isEmpty()) {
            searchResultsMenu.hide();
            return;
        }

        Stream<MovieModel> movieStream = model.movieModels().stream();

        // Filter på titel
        if (searchQuery != null && !searchQuery.isEmpty()) {
            movieStream = movieStream.filter(movie -> movie.titleProperty().get().toLowerCase().contains(searchQuery));
        }

        // Filter på genre
        if (!selectedGenres.isEmpty()) {
            Set<Integer> selectedGenreIds = selectedGenres.stream()
                    .map(genreModel -> genreModel.idProperty().get())
                    .collect(Collectors.toSet());

            movieStream = movieStream.filter(movie -> selectedGenreIds.stream()
                    .allMatch(id -> movie.genreObservableList().stream()
                            .anyMatch(genre -> genre.getTmdbId() == id)));
        }

        // Filter på IMDb rating
        System.out.println(imdbRatingField.getText());
        if (!imdbRatingField.getText().isEmpty()) {
            try {
                System.out.println(imdbRatingField.getText());
                float minRating = Float.parseFloat(imdbRatingField.getText());
                movieStream = movieStream.filter(movie -> movie.imdbRatingProperty().get() >= minRating);
            } catch (NumberFormatException e) {
                System.out.println("forkert input");
            }
        }

        movieStream.forEach(movie -> {
            MenuItem item = new MenuItem(movie.titleProperty().get());
            item.setOnAction(e -> {
                System.out.println("Valgt filmen: " + movie.titleProperty().get());
            });
            searchResultsMenu.getItems().add(item);
        });

        if (!searchResultsMenu.getItems().isEmpty()) {
            searchResultsMenu.show(searchField, Side.BOTTOM, 0, 0);
        } else {
            searchResultsMenu.hide();
        }
    }


    private Region createSidebar() {
        NavigationGroup results = new NavigationGroup("sidebar", model.activeViewProperty());

        results.add(Material2OutlinedAL.HOME, labelsBundle.getString("home"), ViewType.HOME);
        results.add(Material2OutlinedAL.COLLECTIONS, labelsBundle.getString("genres"), ViewType.GENRES);
        results.add(Material2OutlinedAL.CATEGORY, labelsBundle.getString("categories"), ViewType.CATEGORIES);

        return results.getView();
    }
}
