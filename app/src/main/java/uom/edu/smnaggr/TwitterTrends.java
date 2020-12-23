package uom.edu.smnaggr;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


public class TwitterTrends extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String value = intent.getStringExtra("key"); //if it's a string you stored.
        setContentView(R.layout.activity_twitter_trends);
        getMostTrendingTweets();

    }

    private void getMostTrendingTweets(){
        try{
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey(getString(R.string.twitter_consumer_key))
                    .setOAuthConsumerSecret(getString(R.string.twitter_consumer_secret))
                    .setOAuthAccessToken("**************************************************")
                    .setOAuthAccessTokenSecret("******************************************");
            TwitterFactory tf = new TwitterFactory(cb.build());
            Twitter twitter = tf.getInstance();
            // The factory instance is re-useable and thread safe.
            //Twitter twitter = TwitterFactory.getSingleton();
            Query query = new Query("#google");
            query.setCount(50);
            QueryResult result = twitter.search(query);
            int c=0;
            for (Status status : result.getTweets()) {
                System.out.println("Status@\t" + status.getUser().getScreenName() + "\t:\t" + status.getText());
                c+=1;
            }
            System.out.println("SIZE=== "+c);
        }catch(Exception e){
            System.out.println(e);
        }
    }




}
