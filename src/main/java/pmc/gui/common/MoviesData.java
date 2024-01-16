package pmc.gui.common;

import javafx.collections.ObservableList;

public record MoviesData(String title, ObservableList<MovieModel> movies) { }
