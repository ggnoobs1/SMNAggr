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
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


public class TwitterTrends extends AppCompatActivity {

    ArrayList<String> listTweets = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Intent intent = getIntent();
        //pairnei ta token/secret apo to login page
        String token1 = intent.getStringExtra("token"); //if it's a string you stored.
        String secret1 = intent.getStringExtra("secret");
        String search = intent.getStringExtra("search");
        setContentView(R.layout.activity_twitter_trends);
        searchTweets(token1,secret1,search);
        //
        ListView listView = findViewById(R.id.newsList);
        

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
                listTweets.add("Status@\t" + status.getUser().getScreenName() + "\t:\t" + status.getText());
                System.out.println("Status@\t" + status.getUser().getScreenName() + "\t:\t" + status.getText());
                //Todo: anti gia toast, prepei na to valoyme na ta pernaei sto listview
                // exei sto lesson11 o xaikalhs paradeigma
                Toast.makeText(TwitterTrends.this, "Status@\t" + status.getUser().getScreenName() + "\t:\t" + status.getText(), Toast.LENGTH_LONG).show();
                c++;
            }
            System.out.println("SIZE=== "+c);
        }catch(Exception e){
            System.out.println(e);
        }
    }




}
