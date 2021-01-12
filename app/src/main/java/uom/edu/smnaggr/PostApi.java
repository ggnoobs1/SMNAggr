package uom.edu.smnaggr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static uom.edu.smnaggr.FacebookLoggin.page_id;

public class PostApi extends AppCompatActivity {

    private String fbtoken ="EAADNOPeW1AUBAEOwTKTZAsZCP9tKJPOXkQ0tEd4anS2vScAxqW2gAUZBYVanPZCIHuVZAVhAPdeFJ0lZAwcs5kyiRjppAEsEG9XYi9VXQvSsoA1ZBrOFPfTG2Qzm30a0hAW4cOoWwtGXaMlsvD9dsbfUdWOYJWauRXBXFgGCmvLHeVlUQMR6mmBxDBtZACRkn5optQ8thBjYJqm0S13vfynwk1XZARRw7zgiAhTmk9WhIbgZDZD";
    private ListView listView;
    private NewsAdapter newsAdapter;

    //TODO: ayto logika paei gia diagraphi, alla skeftomai enan tropo na to aksiopoihsoyme

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_api);
        //getGraph();
        //Intent intent = getIntent();
        //fbtoken = intent.getParcelableExtra("fbtoken");
        listView = findViewById(R.id.newsListApi);
        newsAdapter = new NewsAdapter(this, R.layout.activity_news_adapter, new ArrayList<FBEntry>());
        listView.setAdapter(newsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id) {

                FBEntry newsEntry = newsAdapter.getNewsEntry(position);
                String url = newsEntry.getUrl();

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
        fetch();

    }



    public void makePost(View view) throws JSONException {
        String tokenissimo = null;
        tokenissimo = AccessToken.getCurrentAccessToken().getToken();
        System.out.println(tokenissimo);

        fetch();

    }
    private void fetch(){
        FetchUserPages fetchNewsTask = new FetchUserPages(newsAdapter);
        fetchNewsTask.execute();
    }


    public void makeApiPost(View view) throws JSONException {
        GraphRequest request = GraphRequest.newPostRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + page_id + "/feed",
                new JSONObject("{\"message\":\"Become a Facebook developer!\"}"),
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        // Insert your code here
                    }
                });
        request.executeAsync();
    }


}