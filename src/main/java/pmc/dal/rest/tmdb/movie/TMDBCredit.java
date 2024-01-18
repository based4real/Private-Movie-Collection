package pmc.dal.rest.tmdb.movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pmc.be.rest.tmdb.TMDBCreditEntity;
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

public class TMDBCredit extends TMDBConnector {
    private int moveId;
    private TMDBLang lang = TMDBLang.ENGLISH;

    List<TMDBCreditEntity> credits = new ArrayList<>();

    public TMDBCredit(TMDBMovieEntity movie) throws PMCException {
        this.moveId = movie.getID();

        searchQuery(moveId, movie.getLang());
    }

    public TMDBCredit(TMDBMovieEntity movie, TMDBLang lang) throws PMCException {
        this.moveId = movie.getID();
        this.lang = lang;
        searchQuery(moveId, lang);
    }

    public TMDBCredit(int moveId, TMDBLang lang) throws PMCException {
        this.moveId = moveId;
        this.lang = lang;
        searchQuery(moveId, lang);
    }

    public TMDBCredit(int moveId) throws PMCException {
        this.moveId = moveId;
        searchQuery(moveId, lang);
    }

    public void searchQuery(int moveId, TMDBLang lang) throws PMCException {
        try {
            String idToString = Integer.toString(moveId);
            String encQuery = URLEncoder.encode(idToString, "UTF-8");

            URI uri = new URI(super.getAPI() + "/movie/" + encQuery + "/credits?" + lang.get());
            JSONArray results = super.getJsonHelper().httpResponseToArray(super.getResponse(uri), "cast");
            JSONArray resultsCrew = super.getJsonHelper().httpResponseToArray(super.getResponse(uri), "crew");

            addResultsToArray(results);
            addResultsToArray(resultsCrew);

        } catch (IOException | PMCException | URISyntaxException | JSONException e) {
            throw new PMCException("API: Kunne ikke søge efter credits\n" + e.getMessage());
        }
    }

    private void addResultsToArray(JSONArray arr) throws JSONException, IOException, PMCException {
        for (int i = 0; i < arr.length(); i++) {
            JSONObject creditJson = arr.getJSONObject(i);
            TMDBCreditEntity credit = parseJson(creditJson);
            credits.add(credit);
        }
    }

    private TMDBCreditEntity parseJson(JSONObject json) throws JSONException, PMCException {
        String img = json.getString("profile_path");
        String character = json.has("character") ? json.getString("character") : "Crew";
        int order = json.has("order") ? json.getInt("order") : -1;

        return new TMDBCreditEntity(
                json.getInt("gender"),
                json.getInt("id"),
                json.getString("known_for_department"),
                json.getString("name"),
                json.getString("original_name"),
                !img.equals("null") ? getImageUrl() + img : null,
                character,
                order
        );
    }

    public List<TMDBCreditEntity> getResult() {
        return credits;
    }

}
