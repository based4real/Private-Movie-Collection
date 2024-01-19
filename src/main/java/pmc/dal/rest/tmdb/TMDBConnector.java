package pmc.dal.rest.tmdb;

import org.json.JSONException;
import org.json.JSONObject;
import pmc.utils.ConfigSystem;
import pmc.utils.JsonHelper;
import pmc.utils.PMCException;

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

    protected HttpRequest getRequest(URI uri) throws PMCException {
        return HttpRequest.newBuilder()
                .uri(uri)
                .header("Authorization", "Bearer " + configSystem.getTMDBToken())
                .build();
    }

    protected HttpResponse<String> getResponse(URI uri) throws PMCException {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            return httpClient.send(getRequest(uri), HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new PMCException("API: HTTP response fejl\n" + e.getMessage());
        }
    }

    public boolean isValidToken() throws IOException, URISyntaxException, JSONException, PMCException {
        URI uri = new URI(getAPI() + "/authentication");
        JSONObject responseJson = getJsonHelper().httpResponseToObject(getResponse(uri));

        // {"status_message":"Success.","status_code":1,"success":true}
        // {"status_code": 7,"status_message": "Invalid API key: You must be granted a valid key.","success": false}
        return responseJson.getBoolean("success");
    }

    protected String getAPI() throws PMCException {
        return configSystem.getTMDBAPIUrl();
    }

    protected String getImageUrl() throws PMCException {
        return configSystem.getTMDBImageUrl();
    }

    protected JsonHelper getJsonHelper() {
        return JsonHelper.getInstance();
    }
}
