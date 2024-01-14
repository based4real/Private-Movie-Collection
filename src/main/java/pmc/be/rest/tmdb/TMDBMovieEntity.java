package pmc.be.rest.tmdb;

import pmc.be.rest.omdb.OMDBMovieEntity;
import pmc.dal.rest.omdb.extra.OMDBSearchMethod;
import pmc.dal.rest.omdb.movie.OMDBSearch;
import pmc.dal.rest.tmdb.extra.TMDBLang;
import pmc.dal.rest.tmdb.movie.TMDBCredit;
import pmc.dal.rest.tmdb.movie.TMDBExternalIDs;
import pmc.dal.rest.tmdb.movie.TMDBGenre;
import pmc.dal.rest.tmdb.movie.TMDBVideo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TMDBMovieEntity {
    private String overview, originalTitle, title;
    private List<Integer> genreIds;
    private String posterPath, backdropPath;
    private String releaseDate;
    private int id;
    private String language;
    private int runtime;

    private TMDBLang lang;

    private OMDBMovieEntity omdbMovieEntity;
    private TMDBExternalIDEntity externalIDs;
    private List<TMDBCreditEntity> credits;
    private List<TMDBGenreEntity> genres;
    private List<TMDBVideoEntity> videos;

    //Konstruktør til importering af film
    public TMDBMovieEntity(String overview, String originalTitle, String title,
                           List<Integer> genreIds, String posterPath, String backdropPath, String releaseDate,
                           int id, String language, TMDBLang lang) {
        this.overview = overview;
        this.originalTitle = originalTitle;
        this.title = title;
        this.genreIds = genreIds;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.releaseDate = releaseDate;
        this.id = id;
        this.language = language;
        this.lang = lang;
    }

    public TMDBMovieEntity(String overview, String originalTitle, String title,
                           List<Integer> genreIds, String posterPath, String backdropPath, String releaseDate,
                           int id, String language, int runtime, TMDBLang lang) {
        this.overview = overview;
        this.originalTitle = originalTitle;
        this.title = title;
        this.genreIds = genreIds;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.releaseDate = releaseDate;
        this.id = id;
        this.language = language;
        this.runtime = runtime;
        this.lang = lang;
    }

    private TMDBExternalIDEntity searchExternalIDs() {
        TMDBExternalIDs tmdbExternalIDs = new TMDBExternalIDs(this);
        return tmdbExternalIDs.getResult();
    }

    private List<TMDBCreditEntity> searchCredits() {
        TMDBCredit tmdbGetCredit = new TMDBCredit(this);
        return tmdbGetCredit.getResult();
    }

    private OMDBMovieEntity searchOmdb() {
        OMDBSearch omdbSearch = new OMDBSearch(getExternalIDs().getImdbID(), OMDBSearchMethod.IMDB);
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

    private List<TMDBVideoEntity> searchVideos() {
        TMDBVideo tmdbVideo = new TMDBVideo(this, TMDBLang.ENGLISH);
        return tmdbVideo.getResult();
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public TMDBExternalIDEntity getExternalIDs() {
        if (externalIDs == null)
            externalIDs = searchExternalIDs();

        return externalIDs;
    }

    public List<TMDBVideoEntity> getVideos() {
        if (videos == null)
            videos = searchVideos();

        return videos;
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

    public int getID() {
        return this.id;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }


    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public int getRuntime() {
        return runtime;
    }

    @Override
    public String toString() {
        return "TMDBMovieEntity{\n" +
                "    description='" + getDescription() + "',\n" +
                "    originalTitle='" + getOriginalTitle() + "',\n" +
                "    title='" + getTitle() + "',\n" +
                "    genreIds=" + getGenreIds() + ",\n" +
                "    posterPath='" + getPosterPath() + "',\n" +
                "    backdropPath='" + getBackdropPath() + "',\n" +
                "    releaseDate='" + getReleaseDate() + "',\n" +
                "    id=" + getID() + ",\n" +
                "    lang=" + getLang() + ",\n" +
                "    omdbMovieEntity=" + getOMDBMovie() + ",\n" +
                "    externalIDs=" + getExternalIDs() + ",\n" +
                "    credits=" + getCredits() + ",\n" +
                "    genres=" + getGenres() + ",\n" +
                "    videos=" + getVideos() + ",\n" +
                "}\n";
    }

}
