package pmc.gui.widgets.controls;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.css.*;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProgressSlider extends Region {
    private static final String CSS_PROPERTY_UNUSED_TRACK = "-unused-track-color";
    private static final String CSS_PROPERTY_USED_TRACK = "-used-track-color";
    private static final String CSS_PROPERTY_USED_TRACK_HOVER = "-used-track-color-hover";
    private static final String CSS_PROPERTY_THUMB = "-thumb-color";

    private final DoubleProperty value = new SimpleDoubleProperty(0);
    private final BooleanProperty hover = new SimpleBooleanProperty(false);
    private final BooleanProperty dragging = new SimpleBooleanProperty(false);

    private final double thumbRadius = 5;
    private final double trackHeight = 5;

    private EventHandler<MouseEvent> globalMouseReleaseHandler;

    private StyleableObjectProperty<Color> usedTrackColor;
    private StyleableObjectProperty<Color> usedTrackColorHover;
    private StyleableObjectProperty<Color> unusedTrackColor;
    private StyleableObjectProperty<Color> thumbColor;


    public ProgressSlider() {
        this.getStyleClass().add("progress-slider");
        this.setMinHeight(trackHeight);
        this.setPrefHeight(trackHeight);
        this.setMaxHeight(trackHeight);

        Rectangle usedTrack = new Rectangle(0, trackHeight);
        Rectangle unusedTrack = createUnusedTrack();
        Circle thumb = createThumb();
        Pane trackContainer = createTrackContainer(unusedTrack, usedTrack);

        setupEventHandlers(thumb);

        bindProperties(usedTrack, unusedTrack, thumb, trackContainer);

        this.getChildren().add(new StackPane(trackContainer, thumb));
    }

    private Rectangle createUnusedTrack() {
        Rectangle results = new Rectangle(0, trackHeight);
        results.widthProperty().bind(this.widthProperty());
        return results;
    }

    private Circle createThumb() {
        Circle results = new Circle(thumbRadius);
        results.setManaged(false);
        results.setVisible(false);
        return results;
    }

    private Pane createTrackContainer(Rectangle unusedTrack, Rectangle usedTrack) {
        Pane results = new Pane();
        results.getChildren().addAll(unusedTrack, usedTrack);
        results.setMinHeight(trackHeight);
        results.setPrefHeight(trackHeight);
        results.setMaxHeight(trackHeight);
        return results;
    }

    private void setupEventHandlers(Circle thumb) {
        this.globalMouseReleaseHandler = new EventHandler<>() {
            @Override
            public void handle(MouseEvent event) {
                dragging.set(false);
                if (!ProgressSlider.this.getBoundsInParent().contains(event.getSceneX(), event.getSceneY())) {
                    hover.set(false);
                    thumb.setVisible(false);
                }
                Scene scene = ProgressSlider.this.getScene();
                if (scene != null) {
                    scene.removeEventFilter(MouseEvent.MOUSE_RELEASED, this);
                }
            }
        };

        this.setOnMouseEntered(e -> {
            thumb.setVisible(true);
            hover.set(true);
        });

        this.setOnMouseExited(e -> {
            if (!dragging.get()) {
                thumb.setVisible(false);
                hover.set(false);
            }
        });

        this.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            dragging.set(true);
            Scene scene = ProgressSlider.this.getScene();
            if (scene != null) {
                scene.addEventFilter(MouseEvent.MOUSE_RELEASED, this.globalMouseReleaseHandler);
            }
        });

        this.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> dragging.set(false));

        this.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            double mouseX = e.getX();
            double newWidth = Math.max(0, Math.min(mouseX, this.getWidth()));
            value.set((newWidth / this.getWidth()) * 100);
            e.consume();
        });

        this.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (!dragging.get()) {
                double mouseX = e.getX();
                double newWidth = Math.max(0, Math.min(mouseX, this.getWidth()));
                value.set((newWidth / this.getWidth()) * 100);
            }
        });
    }

    private void bindProperties(Rectangle usedTrack, Rectangle unusedTrack, Circle thumb, Pane trackContainer) {
        initializeStyleableProperties();

        usedTrack.widthProperty().bind(this.widthProperty().multiply(value.divide(100)));
        thumb.centerXProperty().bind(this.widthProperty().multiply(value.divide(100)));
        thumb.centerYProperty().bind(trackContainer.heightProperty().divide(2));

        usedTrack.fillProperty()
                .bind(Bindings.when(hover.or(dragging))
                        .then(usedTrackColor)
                        .otherwise(usedTrackColorHover));

        unusedTrack.fillProperty().bind(unusedTrackColor);
        thumb.fillProperty().bind(thumbColor);
    }

    private void initializeStyleableProperties() {
        usedTrackColor = new SimpleStyleableObjectProperty<>(StyleableProperties.USED_TRACK_COLOR_HOVER, this, "usedTrackColorHover", Color.BLACK);
        usedTrackColorHover = new SimpleStyleableObjectProperty<>(StyleableProperties.USED_TRACK_COLOR, this, "usedTrackColor", Color.WHITE);
        unusedTrackColor = new SimpleStyleableObjectProperty<>(StyleableProperties.UNUSED_TRACK_COLOR, this, "unusedTrackColor", Color.CHOCOLATE);
        thumbColor = new SimpleStyleableObjectProperty<>(StyleableProperties.THUMB_COLOR, this, "thumbColor", Color.CRIMSON);
    }

    private static class StyleableProperties {
        private static final CssMetaData<ProgressSlider, Color> USED_TRACK_COLOR_HOVER =
                new CssMetaData<>(CSS_PROPERTY_USED_TRACK_HOVER, StyleConverter.getColorConverter(), Color.FIREBRICK) {
                    @Override
                    public boolean isSettable(ProgressSlider slider) {
                        return !slider.usedTrackColor.isBound();
                    }

                    @Override
                    public StyleableProperty<Color> getStyleableProperty(ProgressSlider slider) {
                        return slider.usedTrackColor;
                    }
                };

        private static final CssMetaData<ProgressSlider, Color> USED_TRACK_COLOR =
                new CssMetaData<>(CSS_PROPERTY_USED_TRACK, StyleConverter.getColorConverter(), Color.WHITE) {
                    @Override
                    public boolean isSettable(ProgressSlider slider) {
                        return !slider.usedTrackColorHover.isBound();
                    }

                    @Override
                    public StyleableProperty<Color> getStyleableProperty(ProgressSlider slider) {
                        return slider.usedTrackColorHover;
                    }
                };

        private static final CssMetaData<ProgressSlider, Color> UNUSED_TRACK_COLOR =
                new CssMetaData<>(CSS_PROPERTY_UNUSED_TRACK, StyleConverter.getColorConverter(), Color.GRAY) {
                    @Override
                    public boolean isSettable(ProgressSlider slider) {
                        return !slider.unusedTrackColor.isBound();
                    }

                    @Override
                    public StyleableProperty<Color> getStyleableProperty(ProgressSlider slider) {
                        return slider.unusedTrackColor;
                    }
                };

        private static final CssMetaData<ProgressSlider, Color> THUMB_COLOR =
                new CssMetaData<>(CSS_PROPERTY_THUMB, StyleConverter.getColorConverter(), Color.WHITE) {
                    @Override
                    public boolean isSettable(ProgressSlider slider) {
                        return !slider.thumbColor.isBound();
                    }

                    @Override
                    public StyleableProperty<Color> getStyleableProperty(ProgressSlider slider) {
                        return slider.thumbColor;
                    }
                };

        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static {
            final List<CssMetaData<? extends  Styleable, ?>> styleables = new ArrayList<>(Region.getClassCssMetaData());
            styleables.add(USED_TRACK_COLOR_HOVER);
            styleables.add(USED_TRACK_COLOR);
            styleables.add(UNUSED_TRACK_COLOR);
            styleables.add(THUMB_COLOR);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    public DoubleProperty valueProperty() {
        return value;
    }
}