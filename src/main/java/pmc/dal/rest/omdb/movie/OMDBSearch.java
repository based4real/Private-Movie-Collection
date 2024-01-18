package pmc.dal.rest.omdb.movie;

import org.json.JSONException;
import org.json.JSONObject;
import pmc.be.rest.omdb.OMDBMovieEntity;
import pmc.dal.rest.omdb.OMDBConnector;
import pmc.dal.rest.omdb.extra.OMDBSearchMethod;
import pmc.utils.PMCException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;

public class OMDBSearch extends OMDBConnector {
    private OMDBMovieEntity movieFound;
    private String title;

    public OMDBSearch(String title, OMDBSearchMethod method) throws PMCException {
        this.title = title;
        searchQuery(title, method);
    }

    public void searchQuery(String query, OMDBSearchMethod method) throws PMCException {
        try {
            String encQuery = URLEncoder.encode(query, "UTF-8");
            JSONObject responseJson = super.getJsonHelper().httpResponseToObject(super.getResponse(method.get() + encQuery));

            if (responseJson.has("Error"))
                return;

            movieFound = parseJson(responseJson);

        } catch (IOException | PMCException | JSONException e) {
            throw new PMCException("API: Kunne ikke søge efter OMDB film\n" + e.getMessage());
        }
    }

    private OMDBMovieEntity parseJson(JSONObject json) throws JSONException {
        return new OMDBMovieEntity(
                json.getString("Title"),
                json.getString("Rated"),
                json.getString("Released"),
                json.getString("Year"),
                json.getString("Runtime"),
                json.getString("Director"),
                json.getString("Country"),
                json.getString("Awards"),
                json.getString("BoxOffice"),
                json.getString("imdbID"),
                json.getString("imdbRating"),
                json.getString("imdbVotes")
        );
    }

    public OMDBMovieEntity getResult() {
        return movieFound;
    }

}
