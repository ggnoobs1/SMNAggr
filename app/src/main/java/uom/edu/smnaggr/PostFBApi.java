package uom.edu.smnaggr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static uom.edu.smnaggr.FacebookLoggin.page_id;
import static uom.edu.smnaggr.FacebookLoggin.string_page_token;

public class PostFBApi extends AsyncTask<String, Void, List<PostEntry>> {

    private final String LOG_TAG = PostFBApi.class.getSimpleName();


    private PostAdapter postsAdapter;

    public PostFBApi(PostAdapter postsAdapter) {
        this.postsAdapter = postsAdapter;

    }

    private List<PostEntry> getPostFromJSON(String newsJSONString)
            throws JSONException {

        final String data_accounts = "data";
        final String post_id = "id";
       // final String category = "category";
     //   final String page_name = "name";
    //    final String urlKey = "url";
     //   final String imageUrlKey = "urlToImage";


        JSONObject newsString = new JSONObject(post_id);
        JSONArray newsArray = newsString.getJSONArray(post_id);
        PostEntry entry = new PostEntry();
        entry.setPost_id(newsString.getString(post_id));
        List<PostEntry> postsEntries = new ArrayList<>();
        postsEntries.add(entry);
     //   for (int i = 0; i < newsArray.length(); i++) {
     //       PostEntry entry = new PostEntry();
    //        JSONObject jsonEntry = (JSONObject) newsArray.get(i);
      //      entry.setPost_id(jsonEntry.getString(post_id));

      //      Log.v(LOG_TAG, "Post Entry: " + entry);

     //       postsEntries.add(entry);
   //     }
        return postsEntries;
    }


    @Override
    protected List<PostEntry> doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String baseUrl = "https://graph.facebook.com/v9.0/"
                +page_id
                +"/feed?message="
                +"den_mporeis_na_anevaseis_to_idio_keimeno_dyo_fores&access_token="
                + string_page_token;
        System.out.println(baseUrl);
        try {
            URL url1 = new URL(baseUrl);
            System.out.println(baseUrl);
            urlConnection = (HttpURLConnection) url1.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
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
            String newsJSONString = buffer.toString();
            Log.v(LOG_TAG, "News JSON String: " + newsJSONString);

            return getPostFromJSON(newsJSONString);

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
    protected void onPostExecute(List<PostEntry> postsEntries) {
        postsAdapter.setPostEntries(postsEntries);
    }
}