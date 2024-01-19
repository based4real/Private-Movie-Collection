package pmc.gui.components.pmc;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Builder;

import java.util.*;
import java.util.function.Consumer;

import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2OutlinedAL;
import pmc.gui.common.MovieModel;
import pmc.gui.widgets.*;
import pmc.gui.widgets.buttons.ButtonWidgets;
import pmc.gui.widgets.controls.NavigationGroup;

/**
 * Ansvarlig for at bygge brugergrænsefladen for det overordnede View i Private Movie Collection (PMC) applikationen.
 */
public class PMCViewBuilder implements Builder<Region> {
    private final PMCModel model;
    private final ViewHandler viewHandler;
    private final Consumer<MovieModel> handleMenuItemClick;
    private final Runnable addMovieClick;
    private final Runnable addCategoryClick;
    private final ResourceBundle labelsBundle;

    private final Region homeView;
    private final Region genresView;
    private final Region categoriesView;
    private final Region infoView;
    private final Region playbackView;
    private final Region moviesView;

    private Label pmc;

    public PMCViewBuilder(PMCModel model,
                          ViewHandler viewHandler,
                          Consumer<MovieModel> handleMenuItemClick,
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
        this.handleMenuItemClick = handleMenuItemClick;
        this.addMovieClick = addMovieClick;
        this.addCategoryClick = addCategoryClick;
        this.labelsBundle = ResourceBundle.getBundle("bundles.labels", new Locale("da", "DK"));
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
        model.activeViewProperty().addListener((obs, ov, nv) -> {
            if (nv.equals(ViewType.PLAYBACK)) {
                BorderPane.setMargin(center, new Insets(0));
            } else {
                BorderPane.setMargin(center, new Insets(0, 5, 5, 0));
            }
        });

        main.setTop(top);
        main.setLeft(left);
        main.setCenter(center);

        Pane overlay = new Pane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");

        overlay.visibleProperty().bind(model.isDialogOpenProperty());

        results.getChildren().addAll(main, overlay);
        StackPane.setAlignment(main, Pos.TOP_LEFT);

        Platform.runLater(pmc::requestFocus);

        Platform.runLater(pmc::requestFocus);
        model.activeViewProperty().addListener((obs, ov, nv) -> {
            pmc.requestFocus();
            Platform.runLater(pmc::requestFocus);
        });
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

        Button menuIcon = ButtonWidgets.actionIconButton(Material2MZ.MENU, "menu-icon", event -> {
            model.isCollapsedProperty().set(!model.isCollapsedProperty().get());
        });
        pmc = LabelWidgets.styledLabel("PMC", "logo");
        pmc.setWrapText(false);

        Region iconSearchFieldSpacer = new Region();
        iconSearchFieldSpacer.setPrefWidth(40);
        iconSearchFieldSpacer.setMinWidth(40);
        iconSearchFieldSpacer.setMaxWidth(40);

        FilterSortBox filterSortBox = new FilterSortBox(model, handleMenuItemClick);

        Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);

        Button addCategoryIcon = ButtonWidgets.actionIconButton(Material2AL.LIBRARY_ADD, "add-icon", e -> addCategoryClick.run());
        Button addMovieIcon = ButtonWidgets.actionIconButton(Material2AL.ADD_BOX, "add-icon", e -> addMovieClick.run());

        addMovieIcon.disableProperty().bind(model.copyingFileProperty());

        ProgressBar progressBar = new ProgressBar(0);
        progressBar.progressProperty().bind(model.fileProgressProperty());
        progressBar.visibleProperty().bind(model.copyingFileProperty());

        results.getChildren().addAll(menuIcon, pmc, iconSearchFieldSpacer, filterSortBox.getSearchField(), filterSortBox.getImdbRatingField(), filterSortBox.getGenreCheckComboBox(), space, progressBar, addCategoryIcon, addMovieIcon);
        results.setAlignment(Pos.CENTER_LEFT);

        return results;
    }

    private Region createSidebar() {
        NavigationGroup results = new NavigationGroup("sidebar", model.activeViewProperty());

        results.add(Material2OutlinedAL.HOME, labelsBundle.getString("home"), ViewType.HOME, model.isCollapsedProperty());
        results.add(Material2OutlinedAL.COLLECTIONS, labelsBundle.getString("genres"), ViewType.GENRES, model.isCollapsedProperty());
        results.add(Material2OutlinedAL.CATEGORY, labelsBundle.getString("categories"), ViewType.CATEGORIES, model.isCollapsedProperty());

        return results.getView();
    }
}