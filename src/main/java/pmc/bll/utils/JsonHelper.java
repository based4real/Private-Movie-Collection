package pmc.bll.utils;

import org.json.JSONArray;
import org.json.JSONException;

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
}
