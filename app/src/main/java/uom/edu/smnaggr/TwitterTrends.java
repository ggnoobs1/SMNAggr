package uom.edu.smnaggr;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;



public class TwitterTrends extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String value = intent.getStringExtra("key"); //if it's a string you stored.
        setContentView(R.layout.activity_twitter_trends);


    }




}
