package pmc.be.rest;

import pmc.dal.rest.extra.TMDBLang;
import pmc.dal.rest.movie.TMDBCredit;
import pmc.dal.rest.movie.TMDBGenre;

import java.util.List;
import java.util.stream.Collectors;

public class TMDBMovieEntity {
    private String overview, originalTitle, title;
    private List<Integer> genreIds;
    private String posterPath, backdropPath;
    private String releaseDate;
    private int id;

    private TMDBLang lang;

    //private OMDBMovieEntity omdbMovieEntity;
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

        //omdbMovieEntity = searchOmdb();
        credits = searchCredits();
        genres = searchGenre();
    }

    private List<TMDBCreditEntity> searchCredits() {
        TMDBCredit tmdbGetCredit = new TMDBCredit(this);
        return tmdbGetCredit.getResult();
    }

   // private OMDBMovieEntity searchOmdb() {
   //     OMDBSearch omdbSearch = new OMDBSearch(this.originalTitle, OMDBSearchMethod.TITLE);
   //     return omdbSearch.getResult();
   // }

    public List<TMDBGenreEntity> searchGenre() {
        TMDBGenre tmdbGenre = new TMDBGenre();
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
        return credits;
    }

    public List<TMDBGenreEntity> getGenres() {
        return genres;
    }

    public String getDescription() {
        return overview;
    }

   // public OMDBMovieEntity getOMDBMovie() {
   //     return omdbMovieEntity;
  //  }

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
