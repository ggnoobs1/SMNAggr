package uom.edu.smnaggr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

//put extra kai na ta stelnoume sto text tou fb login
public class TwitterTrends extends AppCompatActivity {

    private TwitterAdapter twitterAdapter;
    private TextInputEditText searchField;
    private ListView listViewTrends;
    private String token1,secret1,search1,jsonString;
    //WOEID
    private int Athens = 946738,Thessaloniki =963291,Greece= 23424833,WW= 1 , Ottawa=3369, Montreal= 3534;


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

    public View view;
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.Greece:
                getTrendsAsync(view,Greece);
                return true;
            case R.id.Athens:
                getTrendsAsync(view,Athens);
                return true;
            case R.id.Thessaloniki:
                getTrendsAsync(view,Thessaloniki);
                return true;
            case R.id.Ottawa:
                getTrendsAsync(view,Ottawa);
                return true;
            case R.id.Montreal:
                getTrendsAsync(view,Montreal);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private int getCity(int aCity) {
        return aCity;
    }


    public void getTrendsAsync(View view, int city ){
        FetchTrends fetchTrendsTask = new FetchTrends(twitterAdapter,token1,secret1,getCity(city));
        fetchTrendsTask.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu,menu);
        return true;
    }



}
