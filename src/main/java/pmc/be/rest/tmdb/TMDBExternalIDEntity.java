package pmc.be.rest.tmdb;

public class TMDBExternalIDEntity {
    private int id;
    private String imdbID;
    private String wikidataID;
    private String facebookID;
    private String instagramID;
    private String twitterID;

    public TMDBExternalIDEntity(int id, String imdbID, String wikidataID,
                                String facebookID, String instagramID, String twitterID) {
        this.id = id;
        this.imdbID = imdbID;
        this.wikidataID = wikidataID;
        this.facebookID = facebookID;
        this.instagramID = instagramID;
        this.twitterID = twitterID;
    }

    public int getID() {
        return id;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getWikidataID() {
        return wikidataID;
    }

    public String getFacebookID() {
        return facebookID;
    }

    public String getInstagramID() {
        return instagramID;
    }

    public String getTwitterID() {
        return twitterID;
    }

    @Override
    public String toString() {
        return "TMDBExternalIDEntity{" +
                "id=" + id +
                ", imdbID='" + imdbID + '\'' +
                ", wikidataID='" + wikidataID + '\'' +
                ", facebookID='" + facebookID + '\'' +
                ", instagramID='" + instagramID + '\'' +
                ", twitterID='" + twitterID + '\'' +
                '}';
    }
}
