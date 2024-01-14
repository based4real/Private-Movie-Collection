package pmc.dal.rest.tmdb;

import org.json.JSONException;
import org.json.JSONObject;
import pmc.be.rest.tmdb.TMDBMovieEntity;
import pmc.dal.rest.tmdb.movie.TMDBMovie;
import pmc.utils.ConfigSystem;
import pmc.utils.JsonHelper;
import pmc.dal.rest.tmdb.movie.TMDBSearch;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

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

    protected HttpResponse<String> getResponse(URI uri) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        return httpClient.send(getRequest(uri), HttpResponse.BodyHandlers.ofString());
    }

    private boolean isValidToken() throws IOException, URISyntaxException, JSONException, InterruptedException {
        URI uri = new URI(getAPI() + "/authentication");
        JSONObject responseJson = getJsonHelper().httpResponseToObject(getResponse(uri));

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

    protected JsonHelper getJsonHelper() {
        return JsonHelper.getInstance();
    }

    public static void main(String[] args) throws IOException, JSONException, URISyntaxException, InterruptedException {
/*        TMDBSearch tmdbSearch = new TMDBSearch("shutter island", TMDBLang.DANISH);

        TMDBConnector tmdbConnector = new TMDBConnector();
        System.out.println(tmdbConnector.isValidToken());

        OMDBConnector omdbConnector = new OMDBConnector();
        System.out.println(omdbConnector.isValidToken());

        TMDBMovieEntity tmdbMovieEntity = tmdbSearch.getResult().getFirst();
        OMDBMovieEntity omdbMovieEntity = tmdbMovieEntity.getOMDBMovie();

        TMDBMovie tmdbMovie = new TMDBMovie(3176);
        System.out.println(tmdbMovie.getResult().getTitle());

        System.out.println(tmdbMovieEntity.getTitle());
        System.out.println(tmdbMovieEntity.getDescription());

        System.out.println(omdbMovieEntity.getAwards());
        System.out.println(omdbMovieEntity.getImdbID());
        System.out.println(omdbMovieEntity.getImdbRating());

        System.out.println(tmdbMovieEntity.getExternalIDs().getImdbID());

        for (TMDBGenreEntity tmdbGenre : tmdbMovieEntity.getGenres()) {
             System.out.println(tmdbGenre.getID() + " " + tmdbGenre.getName());
         }

        //for (TMDBCreditEntity tmdbCredit : tmdbMovieEntity.getCredits()) {
        //     System.out.println(tmdbCredit.getName() + " " + tmdbCredit.getCharacterName() + " " + tmdbCredit.getImage());
        //}

        for (TMDBVideoEntity tmdbVideo1 : tmdbMovieEntity.getVideos())
            System.out.println(tmdbVideo1.getYoutubeUrl());

        //  TMDBGenre tmdbGenre = new TMDBGenre(TMDBLang.FRENCH);
        //   for (TMDBGenreEntity tmdbGenre1 : tmdbGenre.getResult())
        //      System.out.println(tmdbGenre1.getName());

       //  OMDBSearch omdbSearch = new OMDBSearch("tt0048058", OMDBSearchMethod.IMDB);
        //  System.out.println(omdbSearch.getResult().getTitle());*/


//        System.out.println(first);

        List<String> titles = List.of(
                "Shutter Island",
                "Battle Royale",
                "Druk",
                "Shawshank",
                "Oppenheimer",
                "barbie",
                "far til fire",
                "james bond",
                "fast and furious",
                "harakiri",
                "Rojo Amanecer",
                "Kho Gaye Hum Kahan");

        for(String title : titles) {
            TMDBMovieEntity first = new TMDBSearch(title).getResult().getFirst();

            System.out.println("tmdb id: " + first.getID());
            System.out.println("imdb id: " + first.getExternalIDs().getImdbID());
            System.out.println("title: " + first.getOriginalTitle());
            System.out.println("imdb rating: " + first.getOMDBMovie().getRuntime());
            System.out.println("runtime: " + first.getRuntime());
            System.out.println("posterPath: " + first.getPosterPath());
            System.out.println("genres: " + first.getGenreIds());
            System.out.println("\n");
        }


//        System.out.println(new TMDBSearch("a", lang).getResult());
    }
}
