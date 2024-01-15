package pmc.bll;

import pmc.be.Genre;
import pmc.be.rest.tmdb.TMDBGenreEntity;
import pmc.dal.rest.tmdb.movie.TMDBGenre;

import java.util.List;
import java.util.stream.Collectors;

public class TMDBGenreManager {
    public List<TMDBGenreEntity> getGenreNameFromID(List<Genre> genres) {
        TMDBGenre tmdbGenre = new TMDBGenre();
        List<TMDBGenreEntity> allGenres = tmdbGenre.getResult();

        List<TMDBGenreEntity> match = allGenres.stream()
                .filter(genre -> genres.stream().anyMatch(idEntity -> idEntity.getTmdbId() == (genre.getID())))
                .collect(Collectors.toList());

        return match;
    }
}
