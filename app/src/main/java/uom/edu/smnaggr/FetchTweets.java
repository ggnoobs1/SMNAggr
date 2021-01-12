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

    private String token,secret,search;

    private TwitterAdapter twitterAdapter;

    public FetchTweets(TwitterAdapter twitterAdapter
            ,String token, String secret, String search
    ) {
        this.twitterAdapter = twitterAdapter;
        this.token = token;
        this.secret =secret;
        this.search= search;
    }


    @Override
    protected List<TwitterEntry> doInBackground(String... params) {
        List<TwitterEntry> entries2 = new ArrayList<>();
        try{
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey("2Sg4q1Ud5Aq43QP2KPo2ecW5G")
                    .setOAuthConsumerSecret("7JUsNIVKg26CLoRHBZsI9HJJAxcV0XFvvqCDD8phqY7YAmfX3p")
                    .setOAuthAccessToken(token)
                    .setOAuthAccessTokenSecret(secret);
            TwitterFactory tf = new TwitterFactory(cb.build());
            twitter4j.Twitter twitter = tf.getInstance();

            Query query = new Query(search);
            query.setCount(50);
            QueryResult result = twitter.search(query);

            for (twitter4j.Status status : result.getTweets()) {
                TwitterEntry entry = new TwitterEntry();
                entry.setUrl(String.valueOf(status.getURLEntities()));
                entry.setName(status.getUser().getScreenName());
                entry.setQuery(status.getText());
                entries2.add(entry);
            }

        }catch(Exception e){
            System.out.println(e);
        }
        return entries2;

    }

    @Override
    protected void onPostExecute(List<TwitterEntry> twitterEntries) {
        twitterAdapter.setTwitterEntries(twitterEntries);
    }



}