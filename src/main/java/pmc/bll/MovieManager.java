package pmc.bll;

import pmc.be.Movie;
import pmc.dal.database.IDAO;
import pmc.dal.database.MovieDAO_Mock;
import pmc.dal.database.MovieDAO_MockException;
import pmc.dal.database.MovieDAO_MockSlow;
import pmc.dal.exception.DataAccessException;
import pmc.utils.MovieException;

import java.util.List;

public class MovieManager {
    private final IDAO<Movie> dao;

    public MovieManager() {
        this.dao = new MovieDAO_MockException();
    }

    public List<Movie> getAllMovies() throws MovieException {
        try {
            return dao.getAll();
        } catch (DataAccessException e) {
            throw new MovieException("Kunne ikke hente Movie's.\n" + e.getMessage());
        }
    }
}