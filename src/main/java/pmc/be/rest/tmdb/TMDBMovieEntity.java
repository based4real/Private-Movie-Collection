package pmc.be.rest.tmdb;

import pmc.be.rest.omdb.OMDBMovieEntity;
import pmc.dal.rest.omdb.extra.OMDBSearchMethod;
import pmc.dal.rest.omdb.movie.OMDBSearch;
import pmc.dal.rest.tmdb.extra.TMDBLang;
import pmc.dal.rest.tmdb.movie.TMDBCredit;
import pmc.dal.rest.tmdb.movie.TMDBGenre;

import java.util.List;
import java.util.stream.Collectors;

public class TMDBMovieEntity {
    private String overview, originalTitle, title;
    private List<Integer> genreIds;
    private String posterPath, backdropPath;
    private String releaseDate;
    private int id;

    private TMDBLang lang;

    private OMDBMovieEntity omdbMovieEntity;
    private List<TMDBCreditEntity> credits;
    private List<TMDBGenreEntity> genres;

    public TMDBMovieEntity(String overview, String originalTitle, String title,
                           List<Integer> genreIds, String posterPath, String backdropPath, String releaseDate,
                           int id, TMDBLang lang) {
        this.overview = overview;
        this.originalTitle = originalTitle;
        this.title = title;
        this.genreIds = genreIds;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.releaseDate = releaseDate;
        this.id = id;
        this.lang = lang;

        genres = searchGenre();
    }

    private List<TMDBCreditEntity> searchCredits() {
        TMDBCredit tmdbGetCredit = new TMDBCredit(this);
        return tmdbGetCredit.getResult();
    }

    private OMDBMovieEntity searchOmdb() {
        OMDBSearch omdbSearch = new OMDBSearch(this.originalTitle, OMDBSearchMethod.TITLE);
        return omdbSearch.getResult();
    }

    public List<TMDBGenreEntity> searchGenre() {
        TMDBGenre tmdbGenre = new TMDBGenre(lang);
        List<TMDBGenreEntity> allGenres = tmdbGenre.getResult();

        List<TMDBGenreEntity> match = allGenres.stream()
                .filter(genre -> genreIds.contains(genre.getID()))
                .collect(Collectors.toList());

        return match;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public List<TMDBCreditEntity> getCredits() {
        if (credits == null)
            credits = searchCredits();

        return credits;
    }

    public List<TMDBGenreEntity> getGenres() {
        if (genres == null)
            genres = searchGenre();

        return genres;
    }

    public String getDescription() {
        return overview;
    }

    public OMDBMovieEntity getOMDBMovie() {
        if (omdbMovieEntity == null)
            omdbMovieEntity = searchOmdb();

        return omdbMovieEntity;
    }

    public TMDBLang getLang() {
        return lang;
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

}
