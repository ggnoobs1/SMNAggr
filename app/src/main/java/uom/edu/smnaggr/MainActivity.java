package uom.edu.smnaggr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Twitter twitter = TwitterFactory.getSingleton();
        //Status status = twitter.updateStatus();
        System.out.println("Successfully updated the status to [" + status.getText() + "].");

    }
}