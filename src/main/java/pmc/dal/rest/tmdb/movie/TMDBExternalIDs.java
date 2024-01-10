package pmc.dal.rest.tmdb.movie;

import org.json.JSONException;
import org.json.JSONObject;
import pmc.be.rest.tmdb.TMDBExternalIDEntity;
import pmc.be.rest.tmdb.TMDBMovieEntity;
import pmc.dal.rest.tmdb.TMDBConnector;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpResponse;

public class TMDBExternalIDs extends TMDBConnector {
    private int movieID;
    private TMDBExternalIDEntity externalIDs;

    public TMDBExternalIDs(int id) {
        this.movieID = id;
        searchQuery(id);
    }

    public TMDBExternalIDs(TMDBMovieEntity movie) {
        this.movieID = movie.getID();
        searchQuery(movie.getID());
    }

    public void searchQuery(int id) {
        //List<TMDBMovie> movies = new ArrayList<>();
        try {
            String idtoString = Integer.toString(id);
            String encQuery = URLEncoder.encode(idtoString, "UTF-8");
            URI uri = new URI(super.getAPI() + "/movie/" + encQuery + "/external_ids");
            System.out.println(uri);
            HttpResponse<String> response = super.getResponse(uri);
            JSONObject responseJson = new JSONObject(response.body());

            if (responseJson.length() > 0) {
                externalIDs = parseMovieJson(responseJson);
            }

        } catch (IOException | InterruptedException | URISyntaxException | JSONException e) {
            e.printStackTrace();
        }
    }

    private TMDBExternalIDEntity parseMovieJson(JSONObject movieJson) throws JSONException {
        return new TMDBExternalIDEntity(
                movieJson.getInt("id"),
                movieJson.getString("imdb_id"),
                movieJson.getString("wikidata_id"),
                movieJson.getString("facebook_id"),
                movieJson.getString("instagram_id"),
                movieJson.getString("twitter_id")
                );
    }

    public TMDBExternalIDEntity getResult() {
        return externalIDs;
    }


}
