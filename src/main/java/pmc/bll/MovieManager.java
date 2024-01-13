package pmc.bll;

import pmc.be.Movie;
import pmc.dal.database.common.IDAO;
import pmc.dal.database.daos.MovieDAO_DB;
import pmc.dal.exception.DataAccessException;
import pmc.utils.MovieException;

import java.util.List;

public class MovieManager {
    private final IDAO<Movie> dao;

    public MovieManager() throws MovieException {
        try {
            this.dao = new MovieDAO_DB();
        } catch (DataAccessException e) {
            throw new MovieException(e.getMessage());
        }
    }

    public List<Movie> getAllMovies() throws MovieException {
        try {
            return dao.getAll();
        } catch (DataAccessException e) {
            throw new MovieException("Kunne ikke hente filmene.\n" + e.getMessage());
        }
    }
}