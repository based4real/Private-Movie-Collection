package pmc.dal.database.daos;

import pmc.be.Category;
import pmc.be.Movie;
import pmc.dal.database.common.IJunctionDAO;
import pmc.dal.exception.DataAccessException;

import java.util.List;

public interface ICatMovieDAO extends IJunctionDAO<Category, Movie> {
    List<Movie> getMoviesForCategory(Category category) throws DataAccessException;
    List<Category> getCategoriesForMovie(Movie movie) throws DataAccessException;
}