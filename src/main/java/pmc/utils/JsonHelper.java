package pmc.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class JsonHelper {
    private static JsonHelper instance;

    public static JsonHelper getInstance() {
        if (instance == null)
            instance = new JsonHelper();

        return instance;
    }

    public List<Integer> jsonArrayToList(JSONArray jsonArray) throws JSONException {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++)
            list.add(jsonArray.getInt(i));

        return list;
    }

    public JSONObject httpResponseToObject(HttpResponse<String> response) throws JSONException {
        return new JSONObject(response.body());
    }

    public JSONArray httpResponseToArray(HttpResponse<String> response, String array) throws JSONException {
        JSONObject responseJson = new JSONObject(response.body());
        return responseJson.has(array) ? responseJson.getJSONArray(array) : new JSONArray();
    }
}
