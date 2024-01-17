package pmc.gui.common;

import java.util.function.Consumer;

public record MoviePosterActions(
        Consumer<MovieModel> info,
        Consumer<MovieModel> play,
        Consumer<MovieModel> delete,
        Consumer<MovieModel> edit) { }
