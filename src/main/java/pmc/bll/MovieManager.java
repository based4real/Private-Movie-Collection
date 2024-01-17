package pmc.bll;

import pmc.be.Category;
import pmc.be.Genre;
import pmc.be.Movie;
import pmc.dal.database.common.IDAO;
import pmc.dal.database.daos.*;
import pmc.dal.exception.DataAccessException;
import pmc.gui.components.dialog.addmovie.AddMovieData;
import pmc.utils.PMCException;

import java.util.List;
import java.util.Optional;

public class MovieManager {
    private final GenreManager genreManager;

    private final IDAO<Movie> dao;
    private final IMovieGenreDAO movieGenreDAO;
    private final ICatMovieDAO catMovieDAO;

    public MovieManager() throws PMCException {
        try {
            this.genreManager = new GenreManager();
            this.dao = new MovieDAO_DB();
            this.movieGenreDAO = new MovieGenreDAO_DB();
            this.catMovieDAO = new CatMovieDAO_DB();
        } catch (DataAccessException e) {
            throw new PMCException(e.getMessage());
        }
    }

    public Optional<Movie> getMovie(int id) throws PMCException {
        try {
            return dao.get(id);
        } catch (DataAccessException e) {
            throw new PMCException("Kunne ikke hente filmen.\n" + e.getMessage());
        }
    }

    public List<Movie> getAllMovies() throws PMCException {
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
            throw new PMCException("Kunne ikke hente filmene.\n" + e.getMessage());
        }
    }

    public Movie addMovie(Movie movie) throws PMCException {
        try {
            return dao.add(movie);
        } catch (DataAccessException e) {
            throw new PMCException("Kunne ikke tilføje filmen.\n" + e.getMessage());
        }
    }

    public Movie addMovieWithGenres(AddMovieData data) throws PMCException {
        try {
            // todo: skal nok ikke have AddMovieData her, men konverter den til BE oppe i PMCController i stedet
            Movie movie = new Movie(
                    data.tmdbId(),
                    data.imdbId(),
                    data.title(),
                    data.imdbRating(),
                    data.personalRating(),
                    data.fileName(),
                    data.posterPath(),
                    data.lastSeen());
            Movie addedMovie = dao.add(movie);

            List<Genre> genres = genreManager.addGenresById(data.genreIds());
            movie.setGenres(genres);

            for (Genre genre : genres) movieGenreDAO.addRelation(addedMovie, genre);

            return addedMovie;
        } catch (DataAccessException e) {
            throw new PMCException("Kunne ikke tilføje filmen.\n" + e.getMessage());
        }
    }

    public boolean updateMovie(Movie original, Movie updatedData) throws PMCException {
        try {
            return dao.update(original, updatedData);
        } catch (DataAccessException e) {
            throw new PMCException("Kunne ikke opdatere filmen.\n" + e.getMessage());
        }
    }

    public boolean deleteMovie(Movie movie) throws PMCException {
        try {
            return dao.delete(movie);
        } catch (DataAccessException e) {
            throw new PMCException("Kunne ikke slette filmen.\n" + e.getMessage());
        }
    }

    public List<Genre> getAllGenresForMovie(Movie movie) throws DataAccessException {
        return movieGenreDAO.getGenresForMovie(movie);
    }

    public List<Movie> getAllMoviesForGenre(Genre genre) throws PMCException {
        try {
            return movieGenreDAO.getMoviesForGenre(genre);
        } catch (DataAccessException e) {
            throw new PMCException("Kunne ikke hente film fra genre.\n" + e.getMessage());
        }
    }

    public List<Category> getAllCategoriesForMovie(Movie movie) throws PMCException {
        try {
            return catMovieDAO.getCategoriesForMovie(movie);
        } catch (DataAccessException e) {
            throw new PMCException("Kunne ikke hente alle kategorier fra film\n" + e.getMessage());
        }
    }

    public List<Movie> getMoviesForCategory(Category category) throws PMCException {
        try {
            return catMovieDAO.getMoviesForCategory(category);
        } catch (DataAccessException e) {
            throw new PMCException("Kunne ikke hente alle kategorier fra film\n" + e.getMessage());
        }
    }
}