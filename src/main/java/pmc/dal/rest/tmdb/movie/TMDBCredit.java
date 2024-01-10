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
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class TMDBCredit extends TMDBConnector {
    private int moveId;
    private TMDBLang lang = TMDBLang.ENGLISH;

    List<TMDBCreditEntity> credits = new ArrayList<>();

    public TMDBCredit(TMDBMovieEntity movie) {
        this.moveId = movie.getId();
        searchQuery(moveId, movie.getLang());
    }

    public TMDBCredit(int moveId, TMDBLang lang) {
        this.moveId = moveId;
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

            System.out.println(uri);

            HttpResponse<String> response = super.getResponse(uri);
            JSONObject responseJson = new JSONObject(response.body());
            JSONArray results = responseJson.getJSONArray("cast");

            for (int i = 0; i < results.length(); i++) {
                JSONObject creditJson = results.getJSONObject(i);
                TMDBCreditEntity credit = parseCreditJson(creditJson);
                credits.add(credit);
            }

        } catch (IOException | InterruptedException | URISyntaxException | JSONException e) {
            e.printStackTrace();
        }
    }

    private TMDBCreditEntity parseCreditJson(JSONObject movieJson) throws JSONException, IOException {
        String img = movieJson.getString("profile_path");

        return new TMDBCreditEntity(
                movieJson.getInt("gender"),
                movieJson.getInt("id"),
                movieJson.getString("known_for_department"),
                movieJson.getString("name"),
                movieJson.getString("original_name"),
                !img.equals("null") ? getImageUrl() + img : null,
                movieJson.getString("character"),
                movieJson.getInt("order")
        );
    }

    public List<TMDBCreditEntity> getResult() {
        return credits;
    }

}
