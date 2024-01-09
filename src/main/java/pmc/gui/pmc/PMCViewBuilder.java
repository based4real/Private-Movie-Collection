package pmc.gui.pmc;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import pmc.gui.widgets.ButtonWidgets;
import pmc.gui.widgets.IconWidgets;
import pmc.gui.widgets.LabelWidgets;

/**
 * Ansvarlig for at bygge brugergrænsefladen for det overordnede View i Private Movie Collection (PMC) applikationen.
 */
public class PMCViewBuilder implements Builder<Region> {
    private final PMCModel model;
    private final ResourceBundle labelsBundle;

    public PMCViewBuilder(PMCModel model) {
        this.model = model;
        this.labelsBundle = ResourceBundle.getBundle("bundles.labels", Locale.getDefault());
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
        results.setLeft(createSidebar());

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
        VBox results = new VBox();
        results.getStyleClass().add("sidebar");

        ToggleButton home = ButtonWidgets.navButton(Material2AL.HOME, labelsBundle.getString("home"));
        ToggleButton categories = ButtonWidgets.navButton(Material2AL.CATEGORY, labelsBundle.getString("categories"));

        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(home, categories);

        home.setSelected(true);

        // Bruges til at konsumere MOUSE_PRESSED event på ToggleButtons så en knap ikke kan blive deselected.
        EventHandler<MouseEvent> consumeMouseEvent = event -> {
            if (((ToggleButton) event.getSource()).isSelected()) {
                event.consume();
            }
        };

        // Bruges til at konsumere KEY_PRESSED event på ToggleButtons så en kan knap ikke kan
        // blive reselected/deselected ved at trykke space
        EventHandler<KeyEvent> consumeKeyPressEvent = event -> {
            if (((ToggleButton) event.getSource()).isSelected() && event.getCode() == KeyCode.SPACE) {
                event.consume();
            }
        };

        home.addEventFilter(MouseEvent.MOUSE_PRESSED, consumeMouseEvent);
        home.addEventFilter(KeyEvent.KEY_PRESSED, consumeKeyPressEvent);
        categories.addEventFilter(MouseEvent.MOUSE_PRESSED, consumeMouseEvent);
        categories.addEventFilter(KeyEvent.KEY_PRESSED, consumeKeyPressEvent);

        results.getChildren().addAll(home, categories);

        return results;
    }
}
