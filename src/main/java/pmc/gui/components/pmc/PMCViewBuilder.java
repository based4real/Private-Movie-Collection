package pmc.gui.components.pmc;

import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.util.Builder;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2OutlinedAL;
import pmc.gui.common.MovieModel;
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

        TextField searchField = new TextField();
        searchField.setPromptText("Søg...");

        ContextMenu searchResultsMenu = new ContextMenu();
        searchField.textProperty().addListener((obs, ov, nv) -> {
            updateSearchResult(nv, searchResultsMenu, searchField);
        });
        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DOWN && !searchResultsMenu.isShowing()) {
                searchResultsMenu.show(searchField, Side.BOTTOM, 0, 0);
            }
        });

        Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);

        Button addCategoryIcon = ButtonWidgets.actionIconButton(Material2AL.LIBRARY_ADD, "icon", e -> addCategoryClick.run());
        Button addMovieIcon = ButtonWidgets.actionIconButton(Material2AL.ADD_BOX, "icon", e -> addMovieClick.run());

        addMovieIcon.disableProperty().bind(model.copyingFileProperty());

        ProgressBar progressBar = new ProgressBar(0);
        progressBar.progressProperty().bind(model.fileProgressProperty());
        progressBar.visibleProperty().bind(model.copyingFileProperty());

        results.getChildren().addAll(menuIcon, pmc, searchField, space, progressBar, addCategoryIcon, addMovieIcon);
        results.setAlignment(Pos.CENTER_LEFT);

        return results;
    }

    private void updateSearchResult(String searchQuery, ContextMenu searchResultsMenu, TextField searchField) {
        searchResultsMenu.getItems().clear();

        if (searchQuery == null || searchQuery.isEmpty()) {
            searchResultsMenu.hide();
            return;
        }

        FilteredList<MovieModel> filteredList = new FilteredList<>(
                model.movieModels(),
                movie -> movie.titleProperty().get().toLowerCase().contains(searchQuery.toLowerCase())
        );

        for (MovieModel movie : filteredList) {
            MenuItem item = new MenuItem(movie.titleProperty().get());
            item.setOnAction(e -> {
                System.out.println("HEJ DU KLIKKEDE PÅ MIG");
            });
            searchResultsMenu.getItems().add(item);
        }

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
