package pmc.bll;

import pmc.be.rest.tmdb.TMDBMovieEntity;
import pmc.dal.rest.tmdb.movie.TMDBMovie;
import pmc.dal.rest.tmdb.movie.TMDBSearch;
import pmc.utils.PMCException;

public class TMDBMovieManager {
    public TMDBMovieEntity getTMDBMovie(int tmdbId) throws PMCException {
        try {
            return new TMDBMovie(tmdbId).getResult();
        } catch (PMCException e) {
            throw new PMCException("Kunne ikke få TMDB film\n" + e.getMessage());
        }
    }

    public TMDBMovieEntity searchForMovie(String title) throws PMCException {
        try {
            return new TMDBSearch(title).getResult().getFirst();
        } catch (PMCException e) {
            throw new PMCException(e.getMessage());
        }
    }
}
