package pmc.bll;

import pmc.be.rest.tmdb.TMDBMovieEntity;
import pmc.dal.rest.tmdb.movie.TMDBMovie;

public class TMDBMovieManager {
    public TMDBMovieEntity getTMDBMovie(int tmdbId) {
        System.out.println("tmdbId: " + tmdbId);
        return new TMDBMovie(tmdbId).getResult();
    }
}
