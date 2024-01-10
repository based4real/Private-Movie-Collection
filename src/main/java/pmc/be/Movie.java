package pmc.be;

import java.time.LocalDateTime;

public class Movie {
    private int id;
    private int tmdbId;
    private String title;
    private float imdbRating;
    private float personalRating;
    private String filePath;
    private String posterPath;
    private LocalDateTime lastSeen;

    public Movie(int id, int tmdbId, String title, float imdbRating, float personalRating, String filePath, String posterPath, LocalDateTime lastSeen) {
        this.id = id;
        this.tmdbId = tmdbId;
        this.title = title;
        this.imdbRating = imdbRating;
        this.personalRating = personalRating;
        this.filePath = filePath;
        this.posterPath = posterPath;
        this.lastSeen = lastSeen;
    }

    public Movie(int tmdbId, String title, float imdbRating, float personalRating, String filePath, String posterPath, LocalDateTime lastSeen) {
        this(-1, tmdbId, title, imdbRating, personalRating, filePath, posterPath, lastSeen);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(float imdbRating) {
        this.imdbRating = imdbRating;
    }

    public float getPersonalRating() {
        return personalRating;
    }

    public void setPersonalRating(float personalRating) {
        this.personalRating = personalRating;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public LocalDateTime getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(LocalDateTime lastSeen) {
        this.lastSeen = lastSeen;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", tmdbId=" + tmdbId +
                ", title='" + title + '\'' +
                ", imdbRating=" + imdbRating +
                ", personalRating=" + personalRating +
                ", filePath='" + filePath + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", lastSeen=" + lastSeen +
                '}';
    }
}