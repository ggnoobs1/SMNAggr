package uom.edu.smnaggr;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import twitter4j.StatusUpdate;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class PostTwitterAsync extends AsyncTask<String, Void, String> {
    private final String LOG_TAG = PostTwitterAsync.class.getSimpleName();
    private String token1,secret1,confirmationText,postText;
    private Bitmap imagebitmap;

    public PostTwitterAsync(String token1,String secret1,String confirmationText, String postText, Bitmap imagebitmap){
        this.token1 = token1;
        this.secret1 = secret1;
        this.confirmationText = confirmationText;
        this.postText = postText;
        this.imagebitmap = imagebitmap;
    }

    @Override
    protected String doInBackground(String... params){
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("2Sg4q1Ud5Aq43QP2KPo2ecW5G")
                .setOAuthConsumerSecret("7JUsNIVKg26CLoRHBZsI9HJJAxcV0XFvvqCDD8phqY7YAmfX3p")
                .setOAuthAccessToken(token1)
                .setOAuthAccessTokenSecret(secret1);
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter4j.Twitter twitter = tf.getInstance();
        // twitter4j.Twitter twitter = TwitterFactory.getSingleton();
        twitter4j.StatusUpdate status = new StatusUpdate(postText);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        imagebitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] byteArray = bytes.toByteArray();
        ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
        status.setMedia("name",bis);
        try {
            twitter.updateStatus(status);
        } catch (twitter4j.TwitterException e) {
            e.printStackTrace();
        }
        confirmationText="Successfully updated the status to [" + postText + "].";
        return confirmationText;
    }

    @Override
    protected void onPostExecute(String text) {
        this.confirmationText = confirmationText ;
    }
}