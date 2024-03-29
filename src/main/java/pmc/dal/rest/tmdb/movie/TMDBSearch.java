package pmc.dal.rest.tmdb.movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pmc.be.rest.tmdb.TMDBMovieEntity;
import pmc.utils.JsonHelper;
import pmc.dal.rest.tmdb.TMDBConnector;
import pmc.dal.rest.tmdb.extra.TMDBLang;
import pmc.utils.PMCException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class TMDBSearch extends TMDBConnector {
    private String title;
    private List<TMDBMovieEntity> moviesFound = new ArrayList<>();
    private TMDBLang lang = TMDBLang.ENGLISH;

    public TMDBSearch(String title) throws PMCException {
        this.title = title;
        searchQuery(title, lang);
    }

    public TMDBSearch(String title, TMDBLang lang) throws PMCException {
        this.title = title;
        this.lang = lang;
        searchQuery(title, lang);
    }

    public void searchQuery(String query, TMDBLang lang) throws PMCException {
        moviesFound.clear();
        try {
            String encQuery = URLEncoder.encode(query, "UTF-8");

            URI uri = new URI(super.getAPI() + "/search/movie?query=" + encQuery + "&" + lang.get());
            JSONArray results = super.getJsonHelper().httpResponseToArray(super.getResponse(uri), "results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject movieJson = results.getJSONObject(i);
                TMDBMovieEntity movie = parseJson(movieJson, lang);
                moviesFound.add(movie);
            }
        } catch (IOException | URISyntaxException | JSONException e) {
            throw new PMCException("API: Kunne ikke søge efter film\n" + e.getMessage());
        }
    }

    private TMDBMovieEntity parseJson(JSONObject json, TMDBLang lang) throws PMCException, JSONException {
        return new TMDBMovieEntity(
                json.getString("overview"),
                json.getString("original_title"),
                json.getString("title"),
                JsonHelper.getInstance().jsonArrayToList(json.getJSONArray("genre_ids")),
                json.getString("poster_path"),
                json.optString("backdrop_path", null),
                json.getString("release_date"),
                json.getInt("id"),
                json.getString("original_language"),
                lang
        );
    }

    public List<TMDBMovieEntity> getResult() {
        return moviesFound;
    }

}
