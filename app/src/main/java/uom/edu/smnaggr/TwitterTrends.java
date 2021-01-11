package uom.edu.smnaggr;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
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


public class TwitterTrends extends AppCompatActivity {



    ArrayList<String> listTweets = new ArrayList<String>();
    private TwitterAdapter twitterAdapter;
    private ListView listViewTrends;
    private String token1,secret1,search1,jsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        token1 = intent.getStringExtra("token");
        secret1 = intent.getStringExtra("secret");
        search1 = intent.getStringExtra("search");
        //pairnei ta token/secret apo to login page

        setContentView(R.layout.activity_twitter_trends);
        //
        listViewTrends = findViewById(R.id.newsListTrends);
        twitterAdapter = new TwitterAdapter(this, R.layout.activity_tweet_adapter,new ArrayList<TwitterEntry>());
        listViewTrends.setAdapter(twitterAdapter);

        listViewTrends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id) {

                TwitterEntry twEntry = twitterAdapter.getTweetsEntry(position);
                String url = twEntry.getUrl();

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });


      //  twitterAdapter.setTwitterEntries(getTrends2(token1,secret1,search1));



    }

    public void goToFetch(View view) throws JSONException, IOException {

        System.out.println("eftasa edw");
        fetchTrends();
    }

    private void fetchTrends(){
        System.out.println("eftasa edw 2");
        FetchTweets fetchTweetsTask = new FetchTweets(twitterAdapter
                ,token1,secret1,search1
        );
        fetchTweetsTask.execute();
    }

    private void searchTweets(String token1,String secret1,String search){
        try{
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey(getString(R.string.twitter_consumer_key))
                    .setOAuthConsumerSecret(getString(R.string.twitter_consumer_secret))
                    .setOAuthAccessToken(token1)
                    .setOAuthAccessTokenSecret(secret1);
            TwitterFactory tf = new TwitterFactory(cb.build());
            twitter4j.Twitter twitter = tf.getInstance();
            // The factory instance is re-useable and thread safe.
            //Twitter twitter = TwitterFactory.getSingleton();


            Query query = new Query(search);
            query.setCount(50);
            QueryResult result = twitter.search(query);

            int c=0;
            for (Status status : result.getTweets()) {
                listTweets.add("Status @" + status.getUser().getScreenName() + "\t:\t" + status.getText());
                //System.out.println("Status@\t" + status.getUser().getScreenName() + "\t:\t" + status.getText());
                //Todo: anti gia toast, prepei na to valoyme na ta pernaei sto listview
                // exei sto lesson11 o xaikalhs paradeigma
                Toast.makeText(TwitterTrends.this, "Status@\t" + status.getUser().getScreenName() + "\t:\t" + status.getText(), Toast.LENGTH_LONG).show();
                c++;
            }


            System.out.println("SIZE=== "+c);
            System.out.println(listTweets);
            /*
            ResponseList<Location> locations;
            locations = twitter.getAvailableTrends();
            System.out.println("Showing available trends");
            for (Location location : locations) {
                System.out.println(location.getName() + " (woeid:" + location.getWoeid() + ")");
            }
            */
            Trends trends = twitter.getPlaceTrends(963291);
            for (int i = 0; i < trends.getTrends().length; i++) {
                System.out.println(trends.getTrends()[i].getName());
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }


    private List<TwitterEntry> getTrends2(String token1,String secret1,String search) {
        List<TwitterEntry> twitterEntries = new ArrayList<>();
        try{
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(getString(R.string.twitter_consumer_key))
                .setOAuthConsumerSecret(getString(R.string.twitter_consumer_secret))
                .setOAuthAccessToken(token1)
                .setOAuthAccessTokenSecret(secret1);
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter4j.Twitter twitter = tf.getInstance();
        // The factory instance is re-useable and thread safe.
        //Twitter twitter = TwitterFactory.getSingleton();


        Query query = new Query(search);
        query.setCount(50);
        QueryResult result = twitter.search(query);

        for (Status status : result.getTweets()) {
            TwitterEntry entry = new TwitterEntry();
            entry.setName(status.getUser().getScreenName());
            entry.setUrl(status.getText());
            //Log.v(LOG_TAG, "Tweets entry: " + entry);

            twitterEntries.add(entry);
        }
       // return twitterEntries;
        }catch(Exception e){
            System.out.println(e);
        }
        return twitterEntries;
    }

    public void goToAuth(View view){
        System.out.println("eftasa edw3");
        fetchAuth();
    }

    public void fetchAuth(){
        System.out.println("eftasa edw 4");
        FetchOauth2 fetchAuthTask = new FetchOauth2(jsonString);
        fetchAuthTask.execute();
    }



}
