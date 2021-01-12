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

import com.google.android.material.textfield.TextInputEditText;

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

import twitter4j.Location;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Trends;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


public class TwitterTrends extends AppCompatActivity {

    private TwitterAdapter twitterAdapter;
    private TextInputEditText searchField;
    private ListView listViewTrends;
    private String token1,secret1,search1,jsonString;
    //WOEID
    private int Athens = 946738,Thessaloniki =963291,Greece= 23424833,WW= 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        token1 = intent.getStringExtra("token");
        secret1 = intent.getStringExtra("secret");
        setContentView(R.layout.activity_twitter_trends);
        listViewTrends = findViewById(R.id.newsListTrends);
        searchField =findViewById(R.id.searchTrending);
        searchField.setText("Search");
        twitterAdapter = new TwitterAdapter(this, R.layout.activity_tweet_adapter,new ArrayList<TwitterEntry>());
        listViewTrends.setAdapter(twitterAdapter);

        listViewTrends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TwitterEntry twEntry = twitterAdapter.getTweetsEntry(position);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(twEntry.getUrl())));
            }
        });


    }

    public void goToFetch(View view)  {
        if (searchField.getText().equals("Search") || searchField.getText()==null){
            Toast.makeText(TwitterTrends.this, "You have not typed anything to search, please type something and try again", Toast.LENGTH_LONG).show();
        }
        else {
            FetchTweets fetchTweetsTask = new FetchTweets(twitterAdapter, token1, secret1, String.valueOf(searchField.getText()));
            fetchTweetsTask.execute();
        }
    }

    public void getTrendsAsync(View view){
        System.out.println("eftasa edw 999");
        FetchTrends fetchTrendsTask = new FetchTrends(twitterAdapter,token1,secret1,Thessaloniki);
        fetchTrendsTask.execute();
    }





}
