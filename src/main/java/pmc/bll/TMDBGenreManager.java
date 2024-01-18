package pmc.bll;

import pmc.be.Genre;
import pmc.be.rest.tmdb.TMDBGenreEntity;
import pmc.dal.rest.tmdb.movie.TMDBGenre;
import pmc.utils.PMCException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TMDBGenreManager {
    private TMDBGenre tmdbGenre;
    private List<TMDBGenreEntity> cachedGenres;

    public TMDBGenreManager() throws PMCException {
        try {
            tmdbGenre = new TMDBGenre();
        } catch (PMCException e) {
            throw new PMCException("Kunne ikke få TMDBGenre");
        }
    }

    public List<TMDBGenreEntity> getAllGenres() {
        if (cachedGenres == null)
            cachedGenres = tmdbGenre.getResult();

        return cachedGenres;
    }

    public TMDBGenreEntity getTMDBFromGenre(Genre genre) {
        TMDBGenreEntity matchingObject = getAllGenres().stream()
                .filter(genres -> genres.getID() == genre.getTmdbId())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Ingen fundne genre"));
        return matchingObject;
    }
    public List<TMDBGenreEntity> getGenreNameFromID(List<Genre> genres) {
        return getAllGenres().stream()
                .filter(genre -> genres.stream().anyMatch(idEntity -> idEntity.getTmdbId() == (genre.getID())))
                .collect(Collectors.toList());
    }
}
