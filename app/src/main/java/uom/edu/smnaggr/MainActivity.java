package uom.edu.smnaggr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {


    //TODO: veltiwsh toy UI kai allagh twn contraints sta koympia sto MainActrivity


    //TODO: shmantiko na valoyme mesa sto arxeio values ta strings poy xrhsimopoioyme
    // paradeigma sto activity_main.xml ekei poy eixe
    // android:text="Go To Facebook Login"
    // exw valei
    // android:text="@string/FacebookLogin" />
    // kai sto values.xml exw ftiaksei ayto gia na pairnei to string apo ekei
    // <string name="FacebookLogin">Go To Facebook Login</string>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissionsVerification();


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
        Intent intent = new Intent(this, PostApi.class);
        startActivity(intent);
    }
    private void permissionsVerification (){
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };

        if(
                ContextCompat.checkSelfPermission(this.getApplicationContext(),permissions[0])== getPackageManager().PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this.getApplicationContext(),permissions[1])== getPackageManager().PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this.getApplicationContext(),permissions[2])== getPackageManager().PERMISSION_GRANTED
        ){
        }
        else{
            ActivityCompat.requestPermissions(MainActivity.this,permissions,1);
        }
    }


}
