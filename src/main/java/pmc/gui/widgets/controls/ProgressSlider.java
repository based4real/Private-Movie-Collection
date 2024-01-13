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
    private static final String CSS_PROPERTY_THUMB_RADIUS = "-thumb-radius";
    private static final String CSS_PROPERTY_TRACK_HEIGHT = "-track-height";

    private final DoubleProperty value = new SimpleDoubleProperty(0);
    private final BooleanProperty hover = new SimpleBooleanProperty(false);
    private final BooleanProperty dragging = new SimpleBooleanProperty(false);

    private EventHandler<MouseEvent> globalMouseReleaseHandler;

    private StyleableObjectProperty<Color> usedTrackColor;
    private StyleableObjectProperty<Color> usedTrackColorHover;
    private StyleableObjectProperty<Color> unusedTrackColor;
    private StyleableObjectProperty<Color> thumbColor;
    private StyleableObjectProperty<Number> thumbRadius;
    private StyleableObjectProperty<Number> trackHeight;


    public ProgressSlider() {
        this.getStyleClass().add("progress-slider");
        initializeStyleableProperties();
        this.minHeightProperty().bind(trackHeight);
        this.prefHeightProperty().bind(trackHeight);
        this.maxHeightProperty().bind(trackHeight);

        Rectangle usedTrack = new Rectangle();
        usedTrack.setWidth(0);
        usedTrack.heightProperty().bind(trackHeight);
        Rectangle unusedTrack = createUnusedTrack();
        Circle thumb = createThumb();
        Pane trackContainer = createTrackContainer(unusedTrack, usedTrack);

        setupEventHandlers(thumb);

        bindProperties(usedTrack, unusedTrack, thumb, trackContainer);

        this.getChildren().add(new StackPane(trackContainer, thumb));
    }

    private Rectangle createUnusedTrack() {
        Rectangle unusedTrack = new Rectangle();
        unusedTrack.heightProperty().bind(trackHeight);
        unusedTrack.widthProperty().bind(this.widthProperty());
        return unusedTrack;
    }

    private Circle createThumb() {
        Circle thumb = new Circle();
        thumb.radiusProperty().bind(thumbRadius);
        thumb.setManaged(false);
        thumb.setVisible(false);
        return thumb;
    }

    private Pane createTrackContainer(Rectangle unusedTrack, Rectangle usedTrack) {
        Pane results = new Pane();
        results.getChildren().addAll(unusedTrack, usedTrack);
        this.minHeightProperty().bind(trackHeight);
        this.prefHeightProperty().bind(trackHeight);
        this.maxHeightProperty().bind(trackHeight);
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
        thumbRadius = new SimpleStyleableObjectProperty<>(StyleableProperties.THUMB_RADIUS, this, "thumbRadius", 5);
        trackHeight = new SimpleStyleableObjectProperty<>(StyleableProperties.TRACK_HEIGHT, this, "trackHeight", 5);
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

        private static final CssMetaData<ProgressSlider, Number> THUMB_RADIUS =
                new CssMetaData<>(CSS_PROPERTY_THUMB_RADIUS, StyleConverter.getSizeConverter(), 5) {
                    @Override
                    public boolean isSettable(ProgressSlider slider) {
                        return !slider.thumbRadius.isBound();
                    }

                    @Override
                    public StyleableProperty<Number> getStyleableProperty(ProgressSlider slider) {
                        return slider.thumbRadius;
                    }
                };

        private static final CssMetaData<ProgressSlider, Number> TRACK_HEIGHT =
                new CssMetaData<>(CSS_PROPERTY_TRACK_HEIGHT, StyleConverter.getSizeConverter(), 5) {
                    @Override
                    public boolean isSettable(ProgressSlider slider) {
                        return !slider.trackHeight.isBound();
                    }

                    @Override
                    public StyleableProperty<Number> getStyleableProperty(ProgressSlider slider) {
                        return slider.trackHeight;
                    }
                };

        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static {
            final List<CssMetaData<? extends  Styleable, ?>> styleables = new ArrayList<>(Region.getClassCssMetaData());
            styleables.add(USED_TRACK_COLOR_HOVER);
            styleables.add(USED_TRACK_COLOR);
            styleables.add(UNUSED_TRACK_COLOR);
            styleables.add(THUMB_COLOR);
            styleables.add(THUMB_RADIUS);
            styleables.add(TRACK_HEIGHT);
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