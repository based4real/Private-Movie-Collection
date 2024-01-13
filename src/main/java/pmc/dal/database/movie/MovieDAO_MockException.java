package pmc.dal.database;

import pmc.be.Movie;
import pmc.dal.exception.DataAccessException;

import java.util.List;
import java.util.Optional;

public class MovieDAO_MockException implements IDAO<Movie> {
    @Override
    public Optional<Movie> get(int id) throws DataAccessException {
        throw new DataAccessException("(Test) Kan ikke forbinde til databasen");
    }

    @Override
    public List<Movie> getAll() throws DataAccessException {
        throw new DataAccessException("(Test) Kan ikke hente filmene");
    }

    @Override
    public Movie add(Movie movie) throws DataAccessException {
        throw new DataAccessException("(Test) Kan ikke tilføje filmen");
    }

    @Override
    public boolean update(Movie original, Movie updatedData) throws DataAccessException {
        throw new DataAccessException("(Test) Kan ikke opdatere filmen");
    }

    @Override
    public boolean delete(Movie movie) throws DataAccessException {
        throw new DataAccessException("(Test) Kan ikke slette filmen");
    }
}