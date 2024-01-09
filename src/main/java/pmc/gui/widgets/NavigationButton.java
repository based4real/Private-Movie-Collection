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
import pmc.gui.utils.Animations;

public class NavigationButton extends ToggleButton {
    private static final PseudoClass HOVER = PseudoClass.getPseudoClass("hover");
    private static final String CSS_CLASS_NAV_BUTTON = "nav-button";
    private static final String CSS_CLASS_ICON = "font-icon";
    private static final String CSS_CLASS_LABEL = "label";
    private static final String CSS_CLASS_INDICATOR = "indicator";

    public NavigationButton(Ikon iconCode, String text) {
        super();

        this.getStyleClass().add(CSS_CLASS_NAV_BUTTON);
        this.setMaxWidth(Double.MAX_VALUE);

        FontIcon icon = IconWidgets.styledIcon(iconCode, CSS_CLASS_NAV_BUTTON, CSS_CLASS_ICON);
        Label label = LabelWidgets.styledLabel(text, CSS_CLASS_NAV_BUTTON, CSS_CLASS_LABEL);
        Region indicator = createIndicator(icon);
        Region content = createContent(indicator, icon, label);

        this.setGraphic(content);

        setupAnimations();
    }

    private Region createIndicator(FontIcon icon) {
        Region results = new Region();

        results.getStyleClass().add(CSS_CLASS_INDICATOR);
        results.setMinWidth(1);
        results.setVisible(false);

        this.selectedProperty().addListener((obs, ov, nv) -> {
            results.setVisible(nv);
        });

        // Juster højden på indicator baseret på højden af ikonet.
        icon.layoutBoundsProperty().addListener((obs, ov, nv) -> results.setMinHeight(nv.getHeight() + 8));

        return results;
    }

    private Region createContent(Region indicator, FontIcon icon, Label label) {
        HBox results = new HBox(indicator, icon, label);

        results.setAlignment(Pos.CENTER_LEFT);
        results.setSpacing(10);

        return results;
    }

    private void setupAnimations() {
        // Custom hover animation
        this.hoverProperty().addListener((obs, ov, nv) -> {
            this.pseudoClassStateChanged(HOVER, nv);
            if (nv) Animations.fadeIn(this, Duration.millis(200), 0.8, 1.0);
        });

        // Deselect animation
        this.selectedProperty().addListener((obs, ov, nv) -> {
            if (!nv) Animations.fadeOut(this, Duration.millis(200), 0.8, 1.0);
        });
    }
}
