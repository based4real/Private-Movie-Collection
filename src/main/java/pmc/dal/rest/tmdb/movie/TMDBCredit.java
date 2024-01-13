package pmc.dal.rest.tmdb.movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pmc.be.rest.tmdb.TMDBCreditEntity;
import pmc.be.rest.tmdb.TMDBMovieEntity;
import pmc.dal.rest.tmdb.TMDBConnector;
import pmc.dal.rest.tmdb.extra.TMDBLang;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class TMDBCredit extends TMDBConnector {
    private int moveId;
    private TMDBLang lang = TMDBLang.ENGLISH;

    List<TMDBCreditEntity> credits = new ArrayList<>();

    public TMDBCredit(TMDBMovieEntity movie) {
        this.moveId = movie.getID();
        searchQuery(moveId, movie.getLang());
    }

    public TMDBCredit(TMDBMovieEntity movie, TMDBLang lang) {
        this.moveId = movie.getID();
        this.lang = lang;
        searchQuery(moveId, lang);
    }

    public TMDBCredit(int moveId, TMDBLang lang) {
        this.moveId = moveId;
        this.lang = lang;
        searchQuery(moveId, lang);
    }

    public TMDBCredit(int moveId) {
        this.moveId = moveId;
        searchQuery(moveId, lang);
    }

    public void searchQuery(int moveId, TMDBLang lang) {
        try {
            String idToString = Integer.toString(moveId);
            String encQuery = URLEncoder.encode(idToString, "UTF-8");

            URI uri = new URI(super.getAPI() + "/movie/" + encQuery + "/credits?" + lang.get());
            JSONArray results = super.getJsonHelper().httpResponseToArray(super.getResponse(uri), "cast");

            for (int i = 0; i < results.length(); i++) {
                JSONObject creditJson = results.getJSONObject(i);
                TMDBCreditEntity credit = parseJson(creditJson);
                credits.add(credit);
            }

        } catch (IOException | InterruptedException | URISyntaxException | JSONException e) {
            e.printStackTrace();
        }
    }

    private TMDBCreditEntity parseJson(JSONObject json) throws JSONException, IOException {
        String img = json.getString("profile_path");

        return new TMDBCreditEntity(
                json.getInt("gender"),
                json.getInt("id"),
                json.getString("known_for_department"),
                json.getString("name"),
                json.getString("original_name"),
                !img.equals("null") ? getImageUrl() + img : null,
                json.getString("character"),
                json.getInt("order")
        );
    }

    public List<TMDBCreditEntity> getResult() {
        return credits;
    }

}
