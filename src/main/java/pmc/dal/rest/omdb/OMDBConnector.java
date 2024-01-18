package pmc.dal.rest.omdb;

import org.json.JSONException;
import org.json.JSONObject;
import pmc.utils.ConfigSystem;
import pmc.utils.JsonHelper;
import pmc.dal.rest.omdb.extra.OMDBSearchMethod;
import pmc.utils.PMCException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class OMDBConnector {
    private ConfigSystem configSystem;

    public OMDBConnector() {
        this.configSystem = ConfigSystem.getInstance();
    }

    protected HttpRequest getRequest(URI uri) {
        return HttpRequest.newBuilder()
                .uri(uri)
                .build();
    }

    protected HttpResponse<String> getResponse(String uri) throws PMCException {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            URI baseUri = new URI(getAPI() + configSystem.getOMDBToken() + uri);
            return httpClient.send(getRequest(baseUri), HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException | URISyntaxException | PMCException e) {
            throw new PMCException("API: HTTP response fejl\n" + e.getMessage());
        }
    }

    public boolean isValidToken() throws JSONException, PMCException {
        // Dum måde at tjekke på.. de har ingen "auth" tjeks ifølge dokumentation
        JSONObject responseJson = getJsonHelper().httpResponseToObject(getResponse(OMDBSearchMethod.TITLE.get()));

        // {"Response": "False","Error": "Incorrect IMDb ID."} - valid
        // {"Response": "False","Error": "Invalid API key!"}   - ikke valid
        return !responseJson.getString("Error").equals("Invalid API key!");
    }

    protected JsonHelper getJsonHelper() {
        return JsonHelper.getInstance();
    }

    public String getAPI() throws PMCException {
        return configSystem.getOMDBAPIUrl();
    }

}
