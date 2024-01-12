package pmc.gui.widgets.controls;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.kordamp.ikonli.material2.Material2MZ;
import pmc.gui.utils.Animations;
import pmc.gui.widgets.buttons.ButtonWidgets;
import pmc.gui.widgets.LabelWidgets;

import java.util.function.Function;

// todo: refactor
public class HorizontalPaginator<T> extends VBox {
    private final ObservableList<T> items;
    private final Function<T, Node> itemRenderer;
    private final HBox contentBox;
    private final double itemSpacing = 20;

    private double itemWidth = 0;

    public HorizontalPaginator(ObservableList<T> items, Function<T, Node> itemRenderer, String title) {
        this.items = items;
        this.itemRenderer = itemRenderer;

        this.contentBox = createContentBox();

        ScrollPane scrollPane = createScrollPane(contentBox);
        HBox navigationBox = createNavigationBox(scrollPane);
        HBox titleAndNavigation = createTitleAndNavigationBox(LabelWidgets.styledLabel(title, "hpage-title"), navigationBox);

        this.getChildren().addAll(titleAndNavigation, scrollPane);

        setupItemListener();
    }

    private HBox createContentBox() {
        HBox results = new HBox(itemSpacing);

        results.setAlignment(Pos.CENTER_LEFT);
        results.setStyle("-fx-background-color: #323232");
        results.setPadding(new Insets(10, 5, 10, 5));

        return results;
    }

    private ScrollPane createScrollPane(HBox contentBox) {
        ScrollPane results = new ScrollPane(contentBox);

        results.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        results.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        results.setFitToHeight(true);
        results.setFitToWidth(true);
        results.setStyle("-fx-background-color: transparent"); // bruges til at fjerne border på ScrollPane åbenbart
        results.setPadding(new Insets(0, 5, 0, 5));

        return results;
    }

    private HBox createTitleAndNavigationBox(Label titleLabel, HBox navigationBox) {
        HBox results = new HBox();

        HBox.setHgrow(navigationBox, Priority.ALWAYS);
        navigationBox.setAlignment(Pos.CENTER_RIGHT);

        results.setAlignment(Pos.CENTER_LEFT);
        results.setPadding(new Insets(10));
        results.getChildren().addAll(titleLabel, navigationBox);

        return results;
    }

    private void setupItemListener() {
        items.addListener((ListChangeListener.Change<? extends T> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(this::addItemToContentBox);
                }

                if (change.wasRemoved()) {
                    change.getRemoved().forEach(this::removeItemFromContentBox);
                }
            }
        });
    }

    private void addItemToContentBox(T item) {
        Node node = itemRenderer.apply(item);
        contentBox.getChildren().add(node);
        itemWidth = node.getBoundsInParent().getWidth(); // todo: kræver bedre løsning
    }

    private void removeItemFromContentBox(T item) {
        contentBox.getChildren().remove(itemRenderer.apply(item));
    }

    private HBox createNavigationBox(ScrollPane scrollPane) {
        HBox results = new HBox(10);

        Button prevButton = ButtonWidgets.actionIconButton(Material2MZ.NAVIGATE_BEFORE,"hpage-nav", event -> animateScroll(-1, scrollPane));
        Button nextButton = ButtonWidgets.actionIconButton(Material2MZ.NAVIGATE_NEXT,"hpage-nav", event -> animateScroll(1, scrollPane));

        results.getChildren().addAll(prevButton, nextButton);

        prevButton.setDisable(true);
        scrollPane.hvalueProperty().addListener((obs, ov, nv) -> {
            boolean atStart = nv.doubleValue() == 0;
            boolean atEnd = nv.doubleValue() == scrollPane.getHmax();
            prevButton.setDisable(atStart);
            nextButton.setDisable(atEnd);
        });

        return results;
    }

    private void animateScroll(int direction, ScrollPane scrollPane) {
        double viewportWidth = scrollPane.getViewportBounds().getWidth();
        int visibleItems = (int) Math.floor(viewportWidth / (itemWidth + itemSpacing));

        double totalContentWidth = items.size() * (itemWidth + itemSpacing);
        double maxScrollWidth = totalContentWidth - viewportWidth;

        double increment = (visibleItems * (itemWidth + itemSpacing)) / maxScrollWidth;
        double targetVal = Math.max(Math.min(scrollPane.getHvalue() + (increment * direction), 1.0), 0.0);

        Animations.animate(scrollPane.hvalueProperty(), targetVal, Duration.millis(500));
    }
}