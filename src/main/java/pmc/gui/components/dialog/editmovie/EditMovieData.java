package pmc.gui.components.dialog.editmovie;

import pmc.be.Movie;
import pmc.gui.common.MovieModel;

public record EditMovieData(MovieModel movieModel, int categoryId) {
}