package pmc.be;

public class Genre {
    private int tmdbId;

    public Genre(int tmdbId) {
        this.tmdbId = tmdbId;
    }

    public int getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
    }
}
