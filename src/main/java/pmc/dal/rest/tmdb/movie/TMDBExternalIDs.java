package pmc.dal.rest.tmdb.movie;

import org.json.JSONException;
import org.json.JSONObject;
import pmc.be.rest.tmdb.TMDBExternalIDEntity;
import pmc.be.rest.tmdb.TMDBMovieEntity;
import pmc.dal.rest.tmdb.TMDBConnector;
import pmc.utils.PMCException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

public class TMDBExternalIDs extends TMDBConnector {
    private int movieID;
    private TMDBExternalIDEntity externalIDs;

    public TMDBExternalIDs(int id) throws PMCException {
        this.movieID = id;
        searchQuery(id);
    }

    public TMDBExternalIDs(TMDBMovieEntity movie) throws PMCException {
        this.movieID = movie.getID();
        searchQuery(movie.getID());
    }

    public void searchQuery(int id) throws PMCException {
        try {
            String idtoString = Integer.toString(id);
            String encQuery = URLEncoder.encode(idtoString, "UTF-8");

            URI uri = new URI(super.getAPI() + "/movie/" + encQuery + "/external_ids");
            JSONObject result = super.getJsonHelper().httpResponseToObject(super.getResponse(uri));

            if (result.length() > 0) {
                externalIDs = parseJson(result);
            }

        } catch (IOException | URISyntaxException | JSONException e) {
            throw new PMCException("API: Kunne ikke søge efter external ids\n" + e.getMessage());
        }
    }

    private TMDBExternalIDEntity parseJson(JSONObject json) throws JSONException {
        return new TMDBExternalIDEntity(
                json.getInt("id"),
                json.getString("imdb_id"),
                json.getString("wikidata_id"),
                json.getString("facebook_id"),
                json.getString("instagram_id"),
                json.getString("twitter_id")
        );
    }

    public TMDBExternalIDEntity getResult() {
        return externalIDs;
    }
}
