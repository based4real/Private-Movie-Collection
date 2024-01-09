package pmc.gui.widgets;

import javafx.animation.FadeTransition;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

public class ButtonWidgets {

    public static ToggleButton navButton(Ikon iconCode, String text) {
        ToggleButton results = new ToggleButton();
        results.getStyleClass().add("nav-button");
        results.setMaxWidth(Double.MAX_VALUE);

        FontIcon icon = new FontIcon(iconCode);
        icon.getStyleClass().addAll("nav-button", "font-icon");

        Label label = new Label(text);
        label.getStyleClass().addAll("nav-button", "label");

        Region indicator = new Region();
        indicator.getStyleClass().add("indicator");
        indicator.setMinWidth(1);
        indicator.setVisible(false);

        // Juster højden på indicator baseret på højden af ikonet.
        icon.layoutBoundsProperty().addListener((obs, ov, nv) -> indicator.setMinHeight(nv.getHeight() + 8));

        HBox hbox = new HBox(indicator, icon, label);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setSpacing(10);

        results.setGraphic(hbox);

        results.selectedProperty().addListener((obs, ov, nv) -> {
            indicator.setVisible(nv);
        });

        PseudoClass hover = PseudoClass.getPseudoClass("hover");

        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), hbox);
        fadeIn.setToValue(1.0);
        fadeIn.setFromValue(0.8);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), hbox);
        fadeOut.setToValue(1.0);
        fadeOut.setFromValue(0.8);

        results.hoverProperty().addListener((obs, ov, nv) -> {
            results.pseudoClassStateChanged(hover, nv);
            if (nv) {
                fadeIn.playFromStart();
            }
        });

        results.selectedProperty().addListener((obs, ov, nv) -> {
            if (!nv) {
                fadeOut.playFromStart();
            }
        });

        return results;
    }
}
