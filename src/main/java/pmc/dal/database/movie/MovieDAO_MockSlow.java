package pmc.dal.database;

import pmc.be.Movie;
import pmc.dal.exception.DataAccessException;

import java.util.List;
import java.util.Optional;

/**
 * Bruges til at simulere langsom database respons og teste at GUI'en er responsiv og ikke fryser.
 */
public class MovieDAO_MockSlow extends MovieDAO_Mock {
    private static final long DELAY = 2000;

    public MovieDAO_MockSlow() {
        super();
    }

    @Override
    public Optional<Movie> get(int id) throws DataAccessException {
        delay();
        return super.get(id);
    }

    @Override
    public List<Movie> getAll() throws DataAccessException {
        delay();
        return super.getAll();
    }

    @Override
    public Movie add(Movie movie) throws DataAccessException {
        delay();
        return super.add(movie);
    }

    @Override
    public boolean update(Movie original, Movie updatedData) throws DataAccessException {
        delay();
        return super.update(original, updatedData);
    }

    @Override
    public boolean delete(Movie movie) throws DataAccessException {
        delay();
        return super.delete(movie);
    }

    private void delay() {
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
