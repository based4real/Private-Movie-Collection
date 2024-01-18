package pmc.dal.rest.tmdb.movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pmc.be.rest.tmdb.TMDBMovieEntity;
import pmc.be.rest.tmdb.TMDBVideoEntity;
import pmc.dal.rest.tmdb.TMDBConnector;
import pmc.dal.rest.tmdb.extra.TMDBLang;
import pmc.utils.PMCException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class TMDBVideo extends TMDBConnector {
    private List<TMDBVideoEntity> videos = new ArrayList<>();
    private TMDBLang lang = TMDBLang.ENGLISH;

    public TMDBVideo(int id) throws PMCException {
        searchQuery(id, this.lang);
    }
    public TMDBVideo(int id, TMDBLang lang) throws PMCException {
        searchQuery(id, this.lang);
        this.lang = lang;
    }

    public TMDBVideo(TMDBMovieEntity tmdbMovie) throws PMCException {
        searchQuery(tmdbMovie.getID(), tmdbMovie.getLang());
    }

    public TMDBVideo(TMDBMovieEntity tmdbMovie, TMDBLang lang) throws PMCException {
        searchQuery(tmdbMovie.getID(), lang);
    }

    public void searchQuery(int movieID, TMDBLang lang) throws PMCException {
        videos.clear();
        try {
            URI uri = new URI(super.getAPI() + "/movie/" + movieID + "/videos?" + lang.get());
            JSONArray results = super.getJsonHelper().httpResponseToArray(super.getResponse(uri), "results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject creditJson = results.getJSONObject(i);
                TMDBVideoEntity video = parseJson(creditJson);
                videos.add(video);
            }
        } catch (URISyntaxException | JSONException e) {
            throw new PMCException("API: Kunne ikke søge efter videoer\n" + e.getMessage());
        }
    }

    private TMDBVideoEntity parseJson(JSONObject json) throws JSONException {
        return new TMDBVideoEntity(
                json.getString("name"),
                json.getString("key"),
                json.getString("site"),
                json.getInt("size"),
                json.getString("type"),
                json.getBoolean("official"),
                json.getString("published_at"),
                json.getString("id")
        );
    }

    public List<TMDBVideoEntity> getResult() {
        return videos;
    }

}