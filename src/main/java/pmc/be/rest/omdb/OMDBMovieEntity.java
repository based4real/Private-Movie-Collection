package pmc.be.rest.omdb;

public class OMDBMovieEntity {
    private String title;
    private String rated;
    private String released;
    private String director;
    private String country;
    private String awards;
    private String boxOffice;

    private String imdbID, imdbRating, imdbVotes;

    public OMDBMovieEntity(String title, String rated, String released,
                           String director, String country, String awards,
                           String boxOffice, String imdbID, String imdbRating,
                           String imdbVotes) {
        this.title = title;
        this.rated = rated;
        this.released = released;
        this.director = director;
        this.country = country;
        this.awards = awards;
        this.boxOffice = boxOffice;
        this.imdbID = imdbID;
        this.imdbRating = imdbRating;
        this.imdbVotes = imdbVotes;
    }

    public String getTitle() {
        return title;
    }

    public String getRated() {
        return rated;
    }

    public String getReleased() {
        return released;
    }

    public String getDirector() {
        return director;
    }

    public String getCountry() {
        return country;
    }

    public String getAwards() {
        return awards;
    }

    public String getBoxOffice() {
        return boxOffice;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public String getImdbVotes() {
        return imdbVotes;
    }

    @Override
    public String toString() {
        return "OMDBMovieEntity{" +
                "title='" + title + '\'' +
                ", rated='" + rated + '\'' +
                ", released='" + released + '\'' +
                ", director='" + director + '\'' +
                ", country='" + country + '\'' +
                ", awards='" + awards + '\'' +
                ", boxOffice='" + boxOffice + '\'' +
                ", imdbID='" + imdbID + '\'' +
                ", imdbRating='" + imdbRating + '\'' +
                ", imdbVotes='" + imdbVotes + '\'' +
                '}';
    }
}
