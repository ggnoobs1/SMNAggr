package uom.edu.smnaggr;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {


    private String value = "Mao Mao";
    private String value2 = "Oympa Loympa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void goToFBActivity(View view) {
        Intent intent = new Intent(this, FacebookLoggin.class);
        startActivity(intent);

    }

    public void goToTwitterActivity(View view) {
        Intent intent = new Intent(this, TwitterLoggin.class);
        startActivity(intent);
    }

    public void goToPost(View view) {
        Intent intent = new Intent(this, TwitterLoggin.class);
        startActivity(intent);
    }


}
