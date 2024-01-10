package pmc.dal.rest.omdb;

import pmc.bll.utils.ConfigSystem;

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

    protected HttpRequest getRequest(URI uri) throws IOException {
        return HttpRequest.newBuilder()
                .uri(uri)
                .build();
    }

    protected HttpResponse<String> getResponse(URI uri) throws IOException, InterruptedException, URISyntaxException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI baseUri = new URI(getAPI() + configSystem.getOMDBToken() + uri.toString());
        return httpClient.send(getRequest(baseUri), HttpResponse.BodyHandlers.ofString());
    }

    public String getAPI() throws IOException {
        return configSystem.getOMDBAPIUrl();
    }

}
