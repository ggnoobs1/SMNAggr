package uom.edu.smnaggr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Location;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class FetchTrends extends AsyncTask<String, Void, List<TwitterEntry>> {
    private String token1,secret1;
    private TwitterAdapter twitterAdapter;
    private int Woeid;

    public FetchTrends(TwitterAdapter twitterAdapter,String token1,String secret1,int Woeid){
        this.token1 = token1;
        this.secret1 = secret1;
        this.twitterAdapter = twitterAdapter;
        this.Woeid = Woeid;
    }


    @Override
    protected List<TwitterEntry> doInBackground(String... params){
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("2Sg4q1Ud5Aq43QP2KPo2ecW5G")
                .setOAuthConsumerSecret("7JUsNIVKg26CLoRHBZsI9HJJAxcV0XFvvqCDD8phqY7YAmfX3p")
                .setOAuthAccessToken(token1)
                .setOAuthAccessTokenSecret(secret1);
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter4j.Twitter twitter = tf.getInstance();


        twitter4j.Trends trends = null;
        try {
            trends = twitter.getPlaceTrends(Woeid);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        List<TwitterEntry> trendsList = new ArrayList<>();
        for (twitter4j.Trend tr : trends.getTrends()){
            TwitterEntry entry = new TwitterEntry();
            entry.setName(tr.getName());
            entry.setQuery(tr.getQuery());
            entry.setUrl(tr.getURL());
            entry.setTweet_volume(String.valueOf(tr.getTweetVolume()));
            trendsList.add(entry);
        }
        return trendsList;

    }


    @Override
    protected void onPostExecute(List<TwitterEntry> twitterEntries) {
        twitterAdapter.setTwitterEntries(twitterEntries);
    }

}