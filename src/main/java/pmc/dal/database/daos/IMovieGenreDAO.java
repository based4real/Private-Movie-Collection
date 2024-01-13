package pmc.dal.database.daos;

import pmc.be.Genre;
import pmc.be.Movie;
import pmc.dal.database.common.IJunctionDAO;
import pmc.dal.exception.DataAccessException;

import java.util.List;

public interface IMovieGenreDAO extends IJunctionDAO<Movie, Genre> {
    List<Genre> getGenresForMovie(Movie movie) throws DataAccessException;
    List<Movie> getMoviesForGenre(Genre genre) throws DataAccessException;
}
