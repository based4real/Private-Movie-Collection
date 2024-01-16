package pmc.bll;

import pmc.be.Category;
import pmc.be.Genre;
import pmc.be.Movie;
import pmc.dal.database.common.IDAO;
import pmc.dal.database.daos.*;
import pmc.dal.exception.DataAccessException;
import pmc.utils.MovieException;

import java.util.List;
import java.util.Optional;

public class MovieManager {
    private final IDAO<Movie> dao;
    private final IMovieGenreDAO movieGenreDAO;
    private final ICatMovieDAO catMovieDAO;

    public MovieManager() throws MovieException {
        try {
            this.dao = new MovieDAO_DB();
            this.movieGenreDAO = new MovieGenreDAO_DB();
            this.catMovieDAO = new CatMovieDAO_DB();
        } catch (DataAccessException e) {
            throw new MovieException(e.getMessage());
        }
    }

    public Optional<Movie> getMovie(int id) throws MovieException {
        try {
            return dao.get(id);
        } catch (DataAccessException e) {
            throw new MovieException("Kunne ikke hente filmen.\n" + e.getMessage());
        }
    }

    public List<Movie> getAllMovies() throws MovieException {
        try {
            List<Movie> movies = dao.getAll();
            for (Movie movie : movies) {
                List<Genre> genres = movieGenreDAO.getGenresForMovie(movie);
                List<Category> categories = catMovieDAO.getCategoriesForMovie(movie);
                movie.setGenres(genres);
                movie.setCategories(categories);
            }
            return movies;
        } catch (DataAccessException e) {
            throw new MovieException("Kunne ikke hente filmene.\n" + e.getMessage());
        }
    }

    public Movie addMovie(Movie movie) throws MovieException {
        try {
            return dao.add(movie);
        } catch (DataAccessException e) {
            throw new MovieException("Kunne ikke tilføje filmen.\n" + e.getMessage());
        }
    }

    public boolean updateMovie(Movie original, Movie updatedData) throws MovieException {
        try {
            return dao.update(original, updatedData);
        } catch (DataAccessException e) {
            throw new MovieException("Kunne ikke opdatere filmen.\n" + e.getMessage());
        }
    }

    public boolean deleteMovie(Movie movie) throws MovieException {
        try {
            return dao.delete(movie);
        } catch (DataAccessException e) {
            throw new MovieException("Kunne ikke slette filmen.\n" + e.getMessage());
        }
    }

    public List<Genre> getAllGenresForMovie(Movie movie) throws DataAccessException {
        return movieGenreDAO.getGenresForMovie(movie);
    }

    public List<Movie> getAllMoviesForGenre(Genre genre) throws MovieException {
        try {
            return movieGenreDAO.getMoviesForGenre(genre);
        } catch (DataAccessException e) {
            throw new MovieException("Kunne ikke hente film fra genre.\n" + e.getMessage());
        }
    }

    public List<Category> getAllCategoriesForMovie(Movie movie) throws DataAccessException {
        return catMovieDAO.getCategoriesForMovie(movie);
    }
}