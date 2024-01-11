package pmc.be.rest.tmdb;

public class TMDBVideoEntity {
    private String name;
    private String youtubeUrl;
    private String site;
    private int size;
    private String type;
    private boolean official;
    private String publishedAt;
    private String id;

    private static final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";

    public TMDBVideoEntity(String name, String youtubeUrl, String site,
                           int size, String type, boolean official,
                           String publishedAt, String id) {
        this.name = name;
        this.youtubeUrl = YOUTUBE_URL + youtubeUrl;
        this.site = site;
        this.size = size;
        this.type = type;
        this.official = official;
        this.publishedAt = publishedAt;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public String getSite() {
        return site;
    }

    public int getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    public boolean isOfficial() {
        return official;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "TMDBVideoEntity{" +
                "name='" + name + '\'' +
                ", youtubeUrl='" + youtubeUrl + '\'' +
                ", site='" + site + '\'' +
                ", size=" + size +
                ", type='" + type + '\'' +
                ", official=" + official +
                ", publishedAt='" + publishedAt + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}