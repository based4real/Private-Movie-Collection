package pmc.dal.rest.tmdb.movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pmc.be.rest.tmdb.TMDBMovieEntity;
import pmc.dal.rest.tmdb.TMDBConnector;
import pmc.dal.rest.tmdb.extra.TMDBLang;
import pmc.utils.PMCException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class TMDBMovie extends TMDBConnector {
    private int id;
    private TMDBMovieEntity movieFound;
    private TMDBLang lang = TMDBLang.ENGLISH;

    public TMDBMovie(int id) throws PMCException {
        this.id= id;
        searchQuery(id, lang);
    }

    public TMDBMovie(int id, TMDBLang lang) throws PMCException {
        this.id = id;
        this.lang = lang;
        searchQuery(id, lang);
    }

    public void searchQuery(int id, TMDBLang lang) throws PMCException {
        try {
            String idToString = Integer.toString(id);
            String encQuery = URLEncoder.encode(idToString, "UTF-8");

            URI uri = new URI(super.getAPI() + "/movie/" + encQuery + "?" + lang.get());
            JSONObject results = super.getJsonHelper().httpResponseToObject(super.getResponse(uri));

            if (results.length() > 0)
                movieFound = parseJson(results, lang);

        } catch (IOException | URISyntaxException | JSONException e) {
            throw new PMCException("API: Kunne ikke lave præcis film søgning\n" + e.getMessage());
        }
    }

    private TMDBMovieEntity parseJson(JSONObject json, TMDBLang lang) throws JSONException {
        // Burde omskrives til noget smartere.. den outputter lidt anderledes
        JSONArray genres = json.getJSONArray("genres");
        List<Integer> genreIDs = new ArrayList<>();

        for (int i = 0; i < genres.length(); i++) {
            JSONObject genre = (JSONObject) genres.get(i);
            int id = genre.getInt("id");
            genreIDs.add(id);
        }

        return new TMDBMovieEntity(
                json.getString("overview"),
                json.getString("original_title"),
                json.getString("title"),
                genreIDs,
                json.getString("poster_path"),
                json.optString("backdrop_path", null),
                json.getString("release_date"),
                json.getInt("id"),
                json.getString("original_language"),
                json.getInt("runtime"),
                lang
        );
    }

    public TMDBMovieEntity getResult() {
        return movieFound;
    }
}
