package pmc.be;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Movie {
    private int id;
    private int tmdbId;
    private String imdbId;
    private String title;
    private float imdbRating;
    private int personalRating;
    private String filePath;
    private String posterPath;
    private LocalDateTime lastSeen;

    private List<Genre> genres;
    private List<Category> categories;

    public Movie(int id, int tmdbId, String imdbId, String title, float imdbRating, int personalRating, String filePath, String posterPath, LocalDateTime lastSeen) {
        this.id = id;
        this.tmdbId = tmdbId;
        this.imdbId = imdbId;
        this.title = title;
        this.imdbRating = imdbRating;
        this.personalRating = personalRating;
        this.filePath = filePath;
        this.posterPath = posterPath;
        this.lastSeen = lastSeen;
        this.genres = new ArrayList<>();
        this.categories = new ArrayList<>();
    }

    public Movie(int tmdbId, String imdbId, String title, float imdbRating, int personalRating, String filePath, String posterPath, LocalDateTime lastSeen) {
        this(-1, tmdbId, imdbId, title, imdbRating, personalRating, filePath, posterPath, lastSeen);
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

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
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

    public int getPersonalRating() {
        return personalRating;
    }

    public void setPersonalRating(int personalRating) {
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

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getCategories() { return categories; }

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
