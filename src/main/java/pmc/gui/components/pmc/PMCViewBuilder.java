package pmc.gui.components.pmc;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.util.Builder;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import pmc.gui.widgets.*;
import pmc.gui.widgets.controls.NavigationButton;
import pmc.gui.widgets.controls.NavigationGroup;

/**
 * Ansvarlig for at bygge brugergrænsefladen for det overordnede View i Private Movie Collection (PMC) applikationen.
 */
public class PMCViewBuilder implements Builder<Region> {
    private final PMCModel model;
    private final ResourceBundle labelsBundle;
    private final Region homeView;
    private final Region categoriesView;

    public PMCViewBuilder(PMCModel model, Region homeView, Region categoriesView) {
        this.model = model;
        this.labelsBundle = ResourceBundle.getBundle("bundles.labels", Locale.getDefault());
        this.homeView = homeView;
        this.categoriesView = categoriesView;
    }

    @Override
    public Region build() {
        BorderPane results = new BorderPane();
        results.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("/css/theme.css")).toExternalForm());
        //results.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("/css/debugTheme.css")).toExternalForm());
        results.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("/css/pmc.css")).toExternalForm());
        results.getStyleClass().add("main");

        Region topbar = createTopbar();
        Region sidebar = createSidebar();


        BorderPane.setMargin(topbar, new Insets(0, 0, 5, 0));
        BorderPane.setMargin(sidebar, new Insets(0, 5, 0, 0));

        results.setTop(topbar);
        results.setLeft(sidebar);
        results.setCenter(createContent());

        return results;
    }

    private Region createTopbar() {
        HBox results = new HBox();
        results.getStyleClass().add("topbar");

        FontIcon menuIcon = IconWidgets.styledIcon(Material2MZ.MENU, "icon");
        Label pmc = LabelWidgets.styledLabel("PMC", "logo");

        results.getChildren().addAll(menuIcon, pmc);
        results.setAlignment(Pos.CENTER_LEFT);

        return results;
    }

    private Region createSidebar() {
        NavigationGroup navGroup = new NavigationGroup("sidebar");

        NavigationButton home = new NavigationButton(Material2AL.HOME, labelsBundle.getString("home"));
        NavigationButton categories = new NavigationButton(Material2AL.CATEGORY, labelsBundle.getString("categories"));

        navGroup.add(home, model.homeSelectedProperty());
        navGroup.add(categories, model.categoriesSelectedProperty());

        home.setSelected(true);

        Region sidebar = navGroup.getView();
//        sidebar.setMinWidth(120);

        return sidebar;
    }

    private Region createContent() {
        homeView.visibleProperty().bind(model.homeSelectedProperty());
        categoriesView.visibleProperty().bind(model.categoriesSelectedProperty());

        return new StackPane(homeView, categoriesView);
    }
}
