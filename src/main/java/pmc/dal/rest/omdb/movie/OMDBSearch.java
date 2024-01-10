package pmc.dal.rest.omdb.movie;

import org.json.JSONException;
import org.json.JSONObject;
import pmc.be.rest.omdb.OMDBMovieEntity;
import pmc.dal.rest.omdb.OMDBConnector;
import pmc.dal.rest.omdb.extra.OMDBSearchMethod;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpResponse;

public class OMDBSearch extends OMDBConnector {
    private OMDBMovieEntity movieFound;
    private String title;

    public OMDBSearch(String title, OMDBSearchMethod method) {
        this.title = title;
        searchQuery(title, method);
    }

    public void searchQuery(String query, OMDBSearchMethod method) {
        try {
            String encQuery = URLEncoder.encode(query, "UTF-8");

            HttpResponse<String> response = super.getResponse(method.get() + encQuery);
            JSONObject responseJson = new JSONObject(response.body());

            if (responseJson.has("Error"))
                return;

            movieFound = parseMovieJson(responseJson);

        } catch (IOException | InterruptedException | URISyntaxException | JSONException e) {
            e.printStackTrace();
        }
    }

    private OMDBMovieEntity parseMovieJson(JSONObject movieJson) throws JSONException {
        return new OMDBMovieEntity(
                movieJson.getString("Title"),
                movieJson.getString("Rated"),
                movieJson.getString("Released"),
                movieJson.getString("Director"),
                movieJson.getString("Country"),
                movieJson.getString("Awards"),
                movieJson.getString("BoxOffice"),
                movieJson.getString("imdbID"),
                movieJson.getString("imdbRating"),
                movieJson.getString("imdbVotes")
        );
    }

    public OMDBMovieEntity getResult() {
        return movieFound;
    }

}
