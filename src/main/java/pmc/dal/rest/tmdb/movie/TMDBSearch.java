package pmc.dal.rest.tmdb.movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pmc.be.rest.tmdb.TMDBMovieEntity;
import pmc.bll.utils.JsonHelper;
import pmc.dal.rest.tmdb.TMDBConnector;
import pmc.dal.rest.tmdb.extra.TMDBLang;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpResponse;

public class TMDBSearch extends TMDBConnector {
    private String title;
    private TMDBMovieEntity movieFound;
    private TMDBLang lang = TMDBLang.ENGLISH;

    public TMDBSearch(String title) {
        this.title = title;
        searchQuery(title, lang);
    }

    public TMDBSearch(String title, TMDBLang lang) {
        this.title = title;
        this.lang = lang;
        searchQuery(title, lang);
    }

    public void searchQuery(String query, TMDBLang lang) {
        //List<TMDBMovie> movies = new ArrayList<>();
        try {
            String encQuery = URLEncoder.encode(query, "UTF-8");
            URI uri = new URI(super.getAPI() + "/search/movie?query=" + encQuery + lang.get());

            HttpResponse<String> response = super.getResponse(uri);
            JSONObject responseJson = new JSONObject(response.body());
            JSONArray results = responseJson.getJSONArray("results");

            /* Vi kan beholde dette hvis vi vil give mulighed for at vælge flere film men for nu tager vi bare den første.
            for (int i = 0; i < results.length(); i++) {
                JSONObject movieJson = results.getJSONObject(i);
                TMDBMovie movie = parseMovieJson(movieJson);

                System.out.println(movie.getTitle());
                movies.add(movie);
            }*/

            if (results.length() > 0) {
                JSONObject movieJson = results.getJSONObject(0);
                movieFound = parseMovieJson(movieJson, lang);
            }

        } catch (IOException | InterruptedException | URISyntaxException | JSONException e) {
            e.printStackTrace();
        }
    }

    private TMDBMovieEntity parseMovieJson(JSONObject movieJson, TMDBLang lang) throws JSONException {
        return new TMDBMovieEntity(
                movieJson.getString("overview"),
                movieJson.getString("original_title"),
                movieJson.getString("title"),
                JsonHelper.getInstance().jsonArrayToList(movieJson.getJSONArray("genre_ids")),
                movieJson.getString("poster_path"),
                movieJson.optString("backdrop_path", null),
                movieJson.getString("release_date"),
                movieJson.getInt("id"),
                lang
        );
    }

    public TMDBMovieEntity getResult() {
        return movieFound;
    }

}
