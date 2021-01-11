package uom.edu.smnaggr;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

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

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Trends;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import static android.provider.Settings.System.getString;


public class FetchTweets extends AsyncTask<String, Void, List<TwitterEntry>> {

    private final String LOG_TAG = FetchTweets.class.getSimpleName();
    //1 is for global
    private String stringLocationID = "1";

    private String token,secret,search;

    public final String URL_ROOT_TWITTER_API = "https://api.twitter.com";
    public final String URL_SEARCH = URL_ROOT_TWITTER_API + "/1.1/search/tweets.json?q=";
    public final String URL_AUTHENTICATION = URL_ROOT_TWITTER_API + "/oauth2/token";

    public final String URL_TRENDING ="https://api.twitter.com/1.1/trends/place.json?id=1";



    private TwitterAdapter twitterAdapter;

    public FetchTweets(TwitterAdapter twitterAdapter
            ,String token, String secret, String search
    ) {
        this.twitterAdapter = twitterAdapter;
        this.token = token;
        this.secret =secret;
        this.search= search;
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

        try{
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey("2Sg4q1Ud5Aq43QP2KPo2ecW5G")
                    .setOAuthConsumerSecret("7JUsNIVKg26CLoRHBZsI9HJJAxcV0XFvvqCDD8phqY7YAmfX3p")
                    .setOAuthAccessToken(token)
                    .setOAuthAccessTokenSecret(secret);
            TwitterFactory tf = new TwitterFactory(cb.build());
            twitter4j.Twitter twitter = tf.getInstance();
            // The factory instance is re-useable and thread safe.
            //Twitter twitter = TwitterFactory.getSingleton();


            Query query = new Query(search);
            query.setCount(50);
            QueryResult result = twitter.search(query);

            int c=0;
            List<TwitterEntry> entries2 = new ArrayList<>();
            for (twitter4j.Status status : result.getTweets()) {
                TwitterEntry entry = new TwitterEntry();
                //listTweets.add("Status @" + status.getUser().getScreenName() + "\t:\t" + status.getText());
                System.out.println("Status@\t" + status.getUser().getScreenName() + "\t:\t" + status.getText());
                //Todo: anti gia toast, prepei na to valoyme na ta pernaei sto listview
                // exei sto lesson11 o xaikalhs paradeigma
                //Toast.makeText(TwitterTrends.this, "Status@\t" + status.getUser().getScreenName() + "\t:\t" + status.getText(), Toast.LENGTH_LONG).show();
                c++;
                entry.setName(status.getUser().getScreenName());
                entry.setQuery(status.getText());
                entries2.add(entry);
            }


            System.out.println("SIZE=== "+c);
            return entries2;
            /*
            ResponseList<Location> locations;
            locations = twitter.getAvailableTrends();
            System.out.println("Showing available trends");
            for (Location location : locations) {
                System.out.println(location.getName() + " (woeid:" + location.getWoeid() + ")");
            }
            */
          //  Trends trends = twitter.getPlaceTrends(963291);
        //    for (int i = 0; i < trends.getTrends().length; i++) {
       //         System.out.println(trends.getTrends()[i].getName());
       //     }
        }catch(Exception e){
            System.out.println(e);
        }


        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String baseUrl = "https://api.twitter.com/1.1/trends/place.json?id="+stringLocationID;

        try {
            URL url1 = new URL(baseUrl);

            urlConnection = (HttpURLConnection) url1.openConnection();
            urlConnection.setRequestMethod("GET");


           // JSONObject jsonObjectDocument = new JSONObject(jsonToken);
            String token1 = "bearer" + " "
                    + Base64.encodeToString(token.getBytes(),
                    Base64.NO_WRAP);

            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
            urlConnection.setRequestProperty("Authorization", token1);
            urlConnection.setRequestProperty("Authorization", token1);
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



}