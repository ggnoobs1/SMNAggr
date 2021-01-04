package uom.edu.smnaggr;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Trends;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


public class TwitterTrends extends AppCompatActivity {

    ArrayList<String> listTweets = new ArrayList<String>();
    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Intent intent = getIntent();
        //pairnei ta token/secret apo to login page
        setContentView(R.layout.activity_twitter_trends);
        //
        //ListView listView = findViewById(R.id.newsList);



    }

    private void fetchNews(){
        FetchUserPages fetchNewsTask = new FetchUserPages(newsAdapter);
        fetchNewsTask.execute();
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




}
