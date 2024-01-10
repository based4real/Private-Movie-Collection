package pmc.dal.rest.tmdb.movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pmc.be.rest.tmdb.TMDBMovieEntity;
import pmc.be.rest.tmdb.TMDBVideoEntity;
import pmc.dal.rest.tmdb.TMDBConnector;
import pmc.dal.rest.tmdb.extra.TMDBLang;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class TMDBVideo extends TMDBConnector {
    private List<TMDBVideoEntity> videos = new ArrayList<>();
    private TMDBLang lang = TMDBLang.ENGLISH;

    public TMDBVideo(int id) {
        searchQuery(id, this.lang);
    }
    public TMDBVideo(int id, TMDBLang lang) {
        searchQuery(id, this.lang);
        this.lang = lang;
    }

    public TMDBVideo(TMDBMovieEntity tmdbMovie) {
        searchQuery(tmdbMovie.getID(), tmdbMovie.getLang());
    }

    public TMDBVideo(TMDBMovieEntity tmdbMovie, TMDBLang lang) {
        searchQuery(tmdbMovie.getID(), lang);
    }

    public void searchQuery(int movieID, TMDBLang lang) {
        videos.clear();
        try {
            System.out.println(movieID);
            URI uri = new URI(super.getAPI() + "/movie/" + movieID + "/videos?" + lang.get());

            HttpResponse<String> response = super.getResponse(uri);
            JSONObject responseJson = new JSONObject(response.body());
            JSONArray results = responseJson.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject creditJson = results.getJSONObject(i);
                TMDBVideoEntity video = parseGenreJson(creditJson);
                videos.add(video);
            }
        } catch (IOException | InterruptedException | URISyntaxException | JSONException e) {
            e.printStackTrace();
        }
    }

    private TMDBVideoEntity parseGenreJson(JSONObject movieJson) throws JSONException {
        return new TMDBVideoEntity(
                movieJson.getString("name"),
                movieJson.getString("key"),
                movieJson.getString("site"),
                movieJson.getInt("size"),
                movieJson.getString("type"),
                movieJson.getBoolean("official"),
                movieJson.getString("published_at"),
                movieJson.getString("id")
        );
    }

    public List<TMDBVideoEntity> getResult() {
        return videos;
    }

}