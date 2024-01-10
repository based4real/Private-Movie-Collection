package pmc.dal.rest.tmdb.movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pmc.be.rest.tmdb.TMDBGenreEntity;
import pmc.dal.rest.tmdb.TMDBConnector;
import pmc.dal.rest.tmdb.extra.TMDBLang;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class TMDBGenre extends TMDBConnector {
    private List<TMDBGenreEntity> genres = new ArrayList<>();
    private TMDBLang lang = TMDBLang.ENGLISH;

    public TMDBGenre() {
        searchQuery(this.lang);
    }

    public TMDBGenre(TMDBLang lang) {
        searchQuery(lang);
    }

    public void searchQuery(TMDBLang lang) {
        genres.clear();

        try {
            URI uri = new URI(super.getAPI() + "/genre/movie/list?" + lang.get());

            HttpResponse<String> response = super.getResponse(uri);
            JSONObject responseJson = new JSONObject(response.body());
            JSONArray results = responseJson.getJSONArray("genres");

            for (int i = 0; i < results.length(); i++) {
                JSONObject creditJson = results.getJSONObject(i);
                TMDBGenreEntity genre = parseJson(creditJson);
                genres.add(genre);
            }
        } catch (IOException | InterruptedException | URISyntaxException | JSONException e) {
            e.printStackTrace();
        }
    }

    private TMDBGenreEntity parseJson(JSONObject json) throws JSONException {
        return new TMDBGenreEntity(
                json.getInt("id"),
                json.getString("name")
        );
    }

    public List<TMDBGenreEntity> getResult() {
        return genres;
    }
}
