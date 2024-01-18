package pmc.dal.rest.tmdb.movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pmc.be.rest.tmdb.TMDBGenreEntity;
import pmc.dal.rest.tmdb.TMDBConnector;
import pmc.dal.rest.tmdb.extra.TMDBLang;
import pmc.utils.PMCException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class TMDBGenre extends TMDBConnector {
    private List<TMDBGenreEntity> genres = new ArrayList<>();
    private TMDBLang lang = TMDBLang.ENGLISH;

    public TMDBGenre() throws PMCException {
        searchQuery(this.lang);
    }

    public TMDBGenre(TMDBLang lang) throws PMCException {
        searchQuery(lang);
    }

    public void searchQuery(TMDBLang lang) throws PMCException {
        genres.clear();

        try {
            URI uri = new URI(super.getAPI() + "/genre/movie/list?" + lang.get());
            JSONArray results = super.getJsonHelper().httpResponseToArray(super.getResponse(uri), "genres");

            for (int i = 0; i < results.length(); i++) {
                JSONObject creditJson = results.getJSONObject(i);
                TMDBGenreEntity genre = parseJson(creditJson);
                genres.add(genre);
            }
        } catch (URISyntaxException | JSONException e) {
            throw new PMCException("API: Kunne ikke søge efter genre\n" + e.getMessage());
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
