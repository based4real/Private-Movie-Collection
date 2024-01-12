package pmc.gui.components.pmc;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Builder;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2OutlinedAL;
import pmc.gui.common.MovieModel;
import pmc.gui.components.dialog.DialogBuilder;
import pmc.gui.components.dialog.addmovie.AddMovieController;
import pmc.gui.widgets.*;
import pmc.gui.widgets.buttons.ButtonWidgets;
import pmc.gui.widgets.controls.NavigationGroup;
import pmc.gui.widgets.icons.IconWidgets;

/**
 * Ansvarlig for at bygge brugergrænsefladen for det overordnede View i Private Movie Collection (PMC) applikationen.
 */
public class PMCViewBuilder implements Builder<Region> {
    private final PMCModel model;
    private final ViewHandler viewHandler;
    private final Consumer<MovieModel> responseHandler;
    private final ResourceBundle labelsBundle;

    private final Region homeView;
    private final Region genresView;
    private final Region categoriesView;
    private final Region infoView;
    private final Region playbackView;

    public PMCViewBuilder(PMCModel model,
                          ViewHandler viewHandler,
                          Consumer<MovieModel> responseHandler,
                          Region homeView,
                          Region genresView,
                          Region categoriesView,
                          Region infoView,
                          Region playbackView) {
        this.model = model;
        this.viewHandler = viewHandler;
        this.responseHandler = responseHandler;
        this.labelsBundle = ResourceBundle.getBundle("bundles.labels", Locale.getDefault());
        this.homeView = homeView;
        this.genresView = genresView;
        this.categoriesView = categoriesView;
        this.infoView = infoView;
        this.playbackView = playbackView;
    }

    @Override
    public Region build() {
        BorderPane results = new BorderPane();
        results.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("/css/theme.css")).toExternalForm());
        //results.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("/css/debugTheme.css")).toExternalForm());
        results.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("/css/pmc.css")).toExternalForm());
        results.getStyleClass().add("main");

        Region top = createTop();
        Region left = createLeft();

        BorderPane.setMargin(top, new Insets(0, 5, 5, 5));
        BorderPane.setMargin(left, new Insets(5, 5, 5, 5));

        results.setTop(top);
        results.setLeft(left);
        results.setCenter(createCenter());

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

        infoView.managedProperty().bind(model.activeViewProperty().isEqualTo(ViewType.INFO));

        return new StackPane(homeView, genresView, categoriesView, infoView, playbackView);
    }

    private Region createTopbar() {
        HBox results = new HBox();
        results.getStyleClass().add("topbar");

        FontIcon menuIcon = IconWidgets.styledIcon(Material2MZ.MENU, "icon");
        Label pmc = LabelWidgets.styledLabel("PMC", "logo");

        Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);

        Button addMovieIcon = ButtonWidgets.actionIconButton(Material2AL.ADD_BOX, "icon", e -> showAddMovieDialog(responseHandler));

        results.getChildren().addAll(menuIcon, pmc, space, addMovieIcon);
        results.setAlignment(Pos.CENTER_LEFT);

        return results;
    }


    private Region createSidebar() {
        NavigationGroup results = new NavigationGroup("sidebar", model.activeViewProperty());

        results.add(Material2OutlinedAL.HOME, labelsBundle.getString("home"), ViewType.HOME);
        results.add(Material2OutlinedAL.COLLECTIONS, labelsBundle.getString("genres"), ViewType.GENRES);
        results.add(Material2OutlinedAL.CATEGORY, labelsBundle.getString("categories"), ViewType.CATEGORIES);

        return results.getView();
    }

    private void showAddMovieDialog(Consumer<MovieModel> responseHandler) {
        Dialog<MovieModel> dialog = new DialogBuilder<>(new AddMovieController())
                .withTitle("Tilføj film")
                .addButtonTypes(ButtonType.CANCEL, ButtonType.OK)
                .build();

        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.getStyleClass().add("ok-button");

        Button cancelButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.getStyleClass().add("cancel-button");

        dialog.getDialogPane().getScene().getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/css/theme.css")).toExternalForm()
        );
        dialog.getDialogPane().getScene().getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/css/pmc.css")).toExternalForm()
        );

        dialog.getDialogPane().setPrefSize(600, 400);

        dialog.showAndWait().ifPresent(responseHandler);
    }
}
