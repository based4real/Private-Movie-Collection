package pmc.dal.rest.tmdb;

import org.json.JSONException;
import org.json.JSONObject;
import pmc.be.rest.omdb.OMDBMovieEntity;
import pmc.be.rest.tmdb.TMDBGenreEntity;
import pmc.be.rest.tmdb.TMDBMovieEntity;
import pmc.bll.utils.ConfigSystem;
import pmc.dal.rest.omdb.extra.OMDBSearchMethod;
import pmc.dal.rest.omdb.movie.OMDBSearch;
import pmc.dal.rest.tmdb.extra.TMDBLang;
import pmc.dal.rest.tmdb.movie.TMDBGenre;
import pmc.dal.rest.tmdb.movie.TMDBSearch;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TMDBConnector {
    private ConfigSystem configSystem;

    public TMDBConnector() {
        this.configSystem = ConfigSystem.getInstance();
    }

    protected HttpRequest getRequest(URI uri) throws IOException {
        return HttpRequest.newBuilder()
                .uri(uri)
                .header("Authorization", "Bearer " + configSystem.getTMDBToken())
                .build();
    }

    /**
     *
     * @param uri
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    protected HttpResponse<String> getResponse(URI uri) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        return httpClient.send(getRequest(uri), HttpResponse.BodyHandlers.ofString());
    }

    private boolean isValidToken() throws IOException, URISyntaxException, JSONException, InterruptedException {
        URI uri = new URI(getAPI() + "/authentication");

        HttpResponse<String> getResponse = getResponse(uri);
        JSONObject responseJson = new JSONObject(getResponse.body());

        // {"status_message":"Success.","status_code":1,"success":true}
        // {"status_code": 7,"status_message": "Invalid API key: You must be granted a valid key.","success": false}
        return responseJson.getBoolean("success");
    }

    public String getAPI() throws IOException {
        return configSystem.getTMDBAPIUrl();
    }

    public String getImageUrl() throws IOException {
        return configSystem.getTMDBImageUrl();
    }

    public String getPreferedLang() {
        return "&language=en";
    }

    public static void main(String[] args) throws IOException, JSONException, URISyntaxException, InterruptedException {
        TMDBSearch tmdbSearch = new TMDBSearch("green street hooligan", TMDBLang.DANISH);

        TMDBMovieEntity tmdbMovieEntity = tmdbSearch.getResult();
        OMDBMovieEntity omdbMovieEntity = tmdbMovieEntity.getOMDBMovie();

        System.out.println(tmdbMovieEntity.getTitle());
        System.out.println(tmdbMovieEntity.getDescription());

        System.out.println(omdbMovieEntity.getAwards());
        System.out.println(omdbMovieEntity.getImdbID());
        System.out.println(omdbMovieEntity.getImdbRating());

        //for (TMDBGenreEntity tmdbGenre : tmdbMovieEntity.getGenres()) {
        //     System.out.println(tmdbGenre.getID() + " " + tmdbGenre.getName());
        // }

        //for (TMDBCreditEntity tmdbCredit : tmdbMovieEntity.getCredits()) {
        //     System.out.println(tmdbCredit.getName() + " " + tmdbCredit.getCharacterName() + " " + tmdbCredit.getImage());
        //}

        TMDBGenre tmdbGenre = new TMDBGenre(TMDBLang.FRENCH);
        for (TMDBGenreEntity tmdbGenre1 : tmdbGenre.getResult())
            System.out.println(tmdbGenre1.getName());

        OMDBSearch omdbSearch = new OMDBSearch("tt0048058", OMDBSearchMethod.IMDB);
        System.out.println(omdbSearch.getResult().getTitle());
    }
}
