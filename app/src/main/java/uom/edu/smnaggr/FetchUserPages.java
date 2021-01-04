package uom.edu.smnaggr;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static uom.edu.smnaggr.FacebookLoggin.string_page_token;
import static uom.edu.smnaggr.FacebookLoggin.user_id;

public class FetchUserPages extends AsyncTask<String, Void, List<FBEntry>> {

    private final String LOG_TAG = FetchUserPages.class.getSimpleName();

    private NewsAdapter newsAdapter;

    public FetchUserPages(NewsAdapter newsAdapter) {
        this.newsAdapter = newsAdapter;

    }

    private List<FBEntry> getNewsFromJSON(String newsJSONString)
            throws JSONException{

        final String data_accounts = "data";
        final String token_json = "access_token";
        final String category = "category";
        final String page_name = "name";
        final String urlKey = "url";
        final String imageUrlKey = "urlToImage";


        JSONObject newsString = new JSONObject(newsJSONString);
        JSONArray newsArray = newsString.getJSONArray(data_accounts);

        List<FBEntry> newsEntries = new ArrayList<>();
        for (int i = 0; i < newsArray.length(); i++) {
            FBEntry entry = new FBEntry();
            JSONObject jsonEntry = (JSONObject) newsArray.get(i);
            entry.setToken(jsonEntry.getString(token_json));
            entry.setName(jsonEntry.getString(page_name));
            entry.setCategory(jsonEntry.getString(category));
            //entry.setUrl(jsonEntry.getString(urlKey));
           // entry.setUrlToImage(jsonEntry.getString(imageUrlKey));

            Log.v(LOG_TAG, "News entry: " + entry);

            newsEntries.add(entry);
        }
        return newsEntries;
    }


    @Override
    protected List<FBEntry> doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String baseUrl = "https://graph.facebook.com/v9.0/"
                +user_id
                +"/accounts?access_token="
                + string_page_token;

        try {
            URL url1 = new URL(baseUrl);
            urlConnection = (HttpURLConnection) url1.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
            urlConnection.connect();
            StringBuilder buffer = new StringBuilder();


            if (urlConnection.getResponseCode() == 200) {
                InputStream inputStream = urlConnection.getInputStream();

                if (inputStream == null) {
                    return new ArrayList<>();
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
            }
            if (buffer.length() == 0) {
                return new ArrayList<>();
            }
            String newsJSONString = buffer.toString();
            Log.v(LOG_TAG, "News JSON String: " + newsJSONString);

            return getNewsFromJSON(newsJSONString);

        } catch (Exception e) {
            Log.e(LOG_TAG, "Error ", e);
            return new ArrayList<>();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
    }



    @Override
    protected void onPostExecute(List<FBEntry> newsEntries) {
        newsAdapter.setNewsEntries(newsEntries);
    }
}