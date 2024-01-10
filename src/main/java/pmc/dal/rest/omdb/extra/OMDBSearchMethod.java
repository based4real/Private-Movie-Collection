package pmc.dal.rest.omdb.extra;

public enum OMDBSearchMethod {
    TITLE("&t="),
    IMDB("&i=");

    private final String queryString;

    OMDBSearchMethod(String queryString) {
        this.queryString = queryString;
    }

    public String get() {
        return queryString;
    }
}
