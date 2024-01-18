package pmc.gui.widgets;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import pmc.gui.common.MovieModel;
import pmc.gui.components.genres.GenresModel;
import pmc.gui.components.pmc.PMCModel;
import pmc.gui.utils.RatingFilter;
import pmc.gui.widgets.buttons.ButtonWidgets;
import pmc.gui.widgets.icons.IconWidgets;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilterSortBox {
    private TextField searchField;
    private ComboBox<GenresModel> genreComboBox;
    private TextField imdbRatingField;
    private ContextMenu searchResultsMenu;
    private List<GenresModel> selectedGenres;
    private PMCModel model;
    private Consumer<MovieModel> handleMenuItemClick;

    private StackPane searchContainer;
    private StackPane imdbContainer;
    private HBox selectedGenresDisplay;

    private boolean sortByTitle = false;
    private boolean sortByImdbRating = false;
    private boolean sortByGenre = false;

    public FilterSortBox(PMCModel model, Consumer<MovieModel> handleMenuItemClick) {
        this.model = model;
        this.handleMenuItemClick = handleMenuItemClick;

        createComponents();
    }

    private void createComponents() {
        ContextMenu settingsMenu = new ContextMenu();
        MenuItem toggleTitle = new MenuItem("Sorter titel");
        MenuItem toggleImdb = new MenuItem("Sorter IMDb rating");
        MenuItem toggleGenres = new MenuItem("Sorter genre");
        settingsMenu.getItems().addAll(toggleTitle, toggleImdb, toggleGenres);

        // Søgefelt
        searchField = new TextField();
        searchField.setPrefWidth(300);
        FontIcon searchIcon = IconWidgets.styledIcon(Material2MZ.SEARCH, "icon-textfield-icon", "search-icon");

        Button settingsIcon = ButtonWidgets.styledIconButton(Material2MZ.SETTINGS, "icon-textfield-action");
        settingsIcon.setOnAction(e -> {
            settingsMenu.show(settingsIcon, Side.BOTTOM, 0, 0);
        });


        searchContainer = new StackPane();
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
        selectedGenresDisplay = new HBox(5);
        selectedGenresDisplay.setAlignment(Pos.CENTER_LEFT);

        // Genre selector
        genreComboBox = new ComboBox<>();
        FilteredList<GenresModel> filteredGenres = new FilteredList<>(
                model.genreModels().sorted(Comparator.comparing(genreModel -> genreModel.nameProperty().get())),
                genre -> true
        );
        genreComboBox.setItems(filteredGenres);
        genreComboBox.setPromptText("Vælg genre...");

        // Imdb rating felt
        imdbRatingField = new TextField();
        imdbRatingField.setTextFormatter(new TextFormatter<>(new RatingFilter()));

        FontIcon imdbIcon = IconWidgets.styledIcon(FontAwesomeBrands.IMDB, "icon-textfield-icon", "imdb-icon");

        imdbContainer = new StackPane();
        imdbContainer.getChildren().addAll(imdbRatingField, imdbIcon);
        imdbRatingField.getStyleClass().addAll("icon-textfield", "imdb-textfield");
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

        imdbRatingField.focusedProperty().addListener((obs, ov, nv) -> {
            if (nv) updateSearchResult(searchField.getText(), searchResultsMenu, searchField, genreComboBox, selectedGenres, imdbRatingField);
        });

        genreComboBox.focusedProperty().addListener((obs, ov, nv) -> {
            if (nv) updateSearchResult(searchField.getText(), searchResultsMenu, searchField, genreComboBox, selectedGenres, imdbRatingField);
        });

        FontIcon enabledIcon = IconWidgets.styledIcon(Material2AL.CHECK, "menu-setting-enabled-icon");

        toggleTitle.setOnAction(e -> {
            sortByTitle = !sortByTitle;
            toggleTitle.setGraphic(sortByTitle ? enabledIcon : null);
            if (sortByTitle) {
                sortByImdbRating = false;
                sortByGenre = false;
                toggleImdb.setGraphic(null);
                toggleGenres.setGraphic(null);
            }
            updateSearchResult(searchField.getText(), searchResultsMenu, searchField, genreComboBox, selectedGenres, imdbRatingField);
        });

        toggleImdb.setOnAction(e -> {
            sortByImdbRating = !sortByImdbRating;
            toggleImdb.setGraphic(sortByImdbRating ? enabledIcon : null);
            if (sortByImdbRating) {
                sortByTitle = false;
                sortByGenre = false;
                toggleTitle.setGraphic(null);
                toggleGenres.setGraphic(null);
            }
            updateSearchResult(searchField.getText(), searchResultsMenu, searchField, genreComboBox, selectedGenres, imdbRatingField);
        });

        toggleGenres.setOnAction(e -> {
            sortByGenre = !sortByGenre;
            toggleGenres.setGraphic(sortByGenre ? enabledIcon : null);
            if (sortByGenre) {
                sortByTitle = false;
                sortByImdbRating = false;
                toggleTitle.setGraphic(null);
                toggleImdb.setGraphic(null);
            }
            updateSearchResult(searchField.getText(), searchResultsMenu, searchField, genreComboBox, selectedGenres, imdbRatingField);
        });
    }

    public StackPane getSearchField() {
        return searchContainer;
    }

    public StackPane getImdbRatingField() {
        return imdbContainer;
    }

    public ComboBox<GenresModel> getGenreComboBox() {
        return genreComboBox;
    }

    public HBox getSelectedGenresDisplay() {
        return selectedGenresDisplay;
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
                    .map(GenresModel::idProperty)
                    .map(IntegerProperty::get)
                    .collect(Collectors.toSet());

            if (sortByGenre) {
                // Sort efter antal genre match
                movieStream = movieStream.sorted((movie1, movie2) -> {
                    long count1 = movie1.genreObservableList().stream()
                            .filter(genre -> selectedGenreIds.contains(genre.getTmdbId()))
                            .count();
                    long count2 = movie2.genreObservableList().stream()
                            .filter(genre -> selectedGenreIds.contains(genre.getTmdbId()))
                            .count();
                    return Long.compare(count2, count1);
                });
            } else {
                // Filter ved logisk OG
                movieStream = movieStream.filter(movie -> selectedGenreIds.stream()
                        .allMatch(id -> movie.genreObservableList().stream()
                                .anyMatch(genre -> genre.getTmdbId() == id)));
            }
        } else if (sortByGenre) {
            // Sort efter antal genre når ingen genre er valgt
            movieStream = movieStream.sorted((movie1, movie2) -> {
                int genreCount1 = movie1.genreObservableList().size();
                int genreCount2 = movie2.genreObservableList().size();
                return Integer.compare(genreCount2, genreCount1);
            });
        }

        // Filter på IMDb rating
        if (!imdbRatingField.getText().isEmpty()) {
            try {
                float minRating = Float.parseFloat(imdbRatingField.getText());
                movieStream = movieStream.filter(movie -> movie.imdbRatingProperty().get() >= minRating);
            } catch (NumberFormatException e) {

            }
        }

        // Sorter
        if (sortByTitle) {
            movieStream = movieStream.sorted(Comparator.comparing(movie -> movie.titleProperty().get()));
        }

        if (sortByImdbRating) {
            Comparator<MovieModel> comparator = Comparator.comparing(movie -> (double) movie.imdbRatingProperty().get());
            movieStream = movieStream.sorted(comparator.reversed());
        }

        List<MovieModel> movies = movieStream.limit(10).toList();

        if (movies.isEmpty() &&
                (!searchQuery.isEmpty() || !selectedGenres.isEmpty() || !imdbRatingField.getText().isEmpty())) {
            Label noResultLbl = new Label("Fandt ingen resultater");
            noResultLbl.setPrefWidth(270);
            CustomMenuItem customMenuItem = new CustomMenuItem(noResultLbl, false);
            searchResultsMenu.getItems().add(customMenuItem);
            searchResultsMenu.show(searchField, Side.BOTTOM, 10, 0);
            return;
        }

        for (MovieModel movie : movies) {
            String title = movie.titleProperty().get();
            String imdbRating = String.valueOf(movie.imdbRatingProperty().get());

            String genreNames = movie.genreObservableList().stream()
                    .map(genre -> model.genreModels().stream()
                            .filter(genresModel -> genresModel.idProperty().get() == genre.getTmdbId())
                            .findFirst()
                            .map(GenresModel::nameProperty)
                            .map(StringProperty::get)
                            .orElse(""))
                    .filter(genreName -> !genreName.isEmpty())
                    .sorted()
                    .collect(Collectors.joining(", "));

            Label titleLabel = new Label(title);
            titleLabel.getStyleClass().add("menu-item-title");

            FontIcon imdbIcon = IconWidgets.styledIcon(FontAwesomeBrands.IMDB, "imdb-icon");
            Label imdbLabel = new Label(imdbRating);
            imdbLabel.setStyle("-fx-text-fill: lightgrey");
            HBox imdbBox = new HBox(imdbIcon, imdbLabel);
            imdbBox.setAlignment(Pos.CENTER_LEFT);
            imdbBox.setSpacing(5);

            Label genreLabel = new Label(genreNames);
            genreLabel.setStyle("-fx-text-fill: lightgrey");


            VBox text = new VBox(titleLabel, imdbBox, genreLabel);
            text.setAlignment(Pos.TOP_LEFT);
            text.setSpacing(0);

            ImageView poster = ImageWidgets.boundRoundedImage(movie.posterPathProperty(), 50, 75, 10);
            poster.getStyleClass().add("menu-poster");

            HBox container = new HBox(poster, text);

            container.setSpacing(5);
            container.setAlignment(Pos.CENTER_LEFT);

            HBox.setHgrow(text, Priority.ALWAYS);
            container.setPrefWidth(270);



            CustomMenuItem customMenuItem = new CustomMenuItem(container, true);
            customMenuItem.setOnAction(e -> {
                handleMenuItemClick.accept(movie);
            });

            searchResultsMenu.getItems().add(customMenuItem);
        }

        searchResultsMenu.show(searchField, Side.BOTTOM, 10, 0);
    }

}