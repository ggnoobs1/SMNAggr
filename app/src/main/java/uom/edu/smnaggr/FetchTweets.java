package uom.edu.smnaggr;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class FetchTweets extends AsyncTask<String, Void, List<TwitterEntry>> {

    private final String LOG_TAG = FetchTweets.class.getSimpleName();
    //1 is for global
    private String stringLocationID = "1";

    private String token,secret;

    public final String URL_ROOT_TWITTER_API = "https://api.twitter.com";
    public final String URL_SEARCH = URL_ROOT_TWITTER_API + "/1.1/search/tweets.json?q=";
    public final String URL_AUTHENTICATION = URL_ROOT_TWITTER_API + "/oauth2/token";

    public final String URL_TRENDING ="https://api.twitter.com/1.1/trends/place.json?id=1";



    private TwitterAdapter twitterAdapter;

    public FetchTweets(TwitterAdapter twitterAdapter
            ,String token, String secret
    ) {
        this.twitterAdapter = twitterAdapter;
        this.token = token;
        this.secret =secret;
    }

    private List<TwitterEntry> getTrendsFromJSON(String trendsJSONString)
            throws JSONException{

        final String trends = "trends";
        final String name = "name";
        final String url = "url";
        final String query = "query";
        final String tweet_volume = "tweet_volume";


        JSONObject tweetsString = new JSONObject(trendsJSONString);
        JSONArray tweetsArray = tweetsString.getJSONArray(trends);

        List<TwitterEntry> twitterEntries = new ArrayList<>();
        for (int i = 0; i < tweetsArray.length(); i++) {
            TwitterEntry entry = new TwitterEntry();
            JSONObject jsonEntry = (JSONObject) tweetsArray.get(i);
            entry.setName(jsonEntry.getString(name));
            entry.setUrl(jsonEntry.getString(url));
            entry.setQuery(jsonEntry.getString(query));
            entry.setTweet_volume(jsonEntry.getString(tweet_volume));

            Log.v(LOG_TAG, "Tweets entry: " + entry);

            twitterEntries.add(entry);
        }
        return twitterEntries;
    }


    @Override
    protected List<TwitterEntry> doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String baseUrl = "https://api.twitter.com/1.1/trends/place.json?id="+stringLocationID;

        System.out.println(baseUrl);
        try {
            URL url1 = new URL(baseUrl);
            System.out.println(baseUrl);

            urlConnection = (HttpURLConnection) url1.openConnection();
            urlConnection.setRequestMethod("GET");

            String jsonString = appAuthentication();
            JSONObject jsonObjectDocument = new JSONObject(jsonString);
            String token = jsonObjectDocument.getString("token_type") + " "
                    + jsonObjectDocument.getString("access_token");

            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
            urlConnection.setRequestProperty("Authorization", token);
        //    urlConnection.setRequestProperty("Authorization", token);
            urlConnection.setRequestProperty("Content-Type",
                    "application/json");
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
            String trendsJSONString = buffer.toString();
            Log.v(LOG_TAG, "News JSON String: " + trendsJSONString);

            return getTrendsFromJSON(trendsJSONString);

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
    protected void onPostExecute(List<TwitterEntry> twitterEntries) {
        twitterAdapter.setTwitterEntries(twitterEntries);
    }

    public String appAuthentication() {

        HttpURLConnection httpConnection = null;
        OutputStream outputStream = null;
        BufferedReader bufferedReader = null;
        StringBuilder response = null;

        try {
            URL url = new URL(URL_AUTHENTICATION);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("POST");
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);

            String accessCredential = R.string.twitter_consumer_key + ":"
                    + R.string.twitter_consumer_secret;
            String authorization = "Basic "
                    + Base64.encodeToString(accessCredential.getBytes(),
                    Base64.NO_WRAP);
            String param = "grant_type=client_credentials";

            httpConnection.addRequestProperty("Authorization", authorization);
            httpConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            httpConnection.connect();

            outputStream = httpConnection.getOutputStream();
            outputStream.write(param.getBytes());
            outputStream.flush();
            outputStream.close();
            // int statusCode = httpConnection.getResponseCode();
            // String reason =httpConnection.getResponseMessage();

            bufferedReader = new BufferedReader(new InputStreamReader(
                    httpConnection.getInputStream()));
            String line;
            response = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }

            Log.d(LOG_TAG,
                    "POST response code: "
                            + String.valueOf(httpConnection.getResponseCode()));
            Log.d(LOG_TAG, "JSON response: " + response.toString());

        } catch (Exception e) {
            Log.e(LOG_TAG, "POST error: " + Log.getStackTraceString(e));

        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }
        return response.toString();
    }


}