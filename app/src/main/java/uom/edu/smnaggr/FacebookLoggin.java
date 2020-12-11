package uom.edu.smnaggr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Picasso;

import uom.edu.smnaggr.databinding.ActivityFacebookLogginBinding;

public class FacebookLoggin extends AppCompatActivity {


    private CallbackManager callBack;
    private FirebaseAuth mAuth;
    private TextView userName;
    private ImageView profilePic;
    private LoginButton fbLogin;
    private TextView Url;
    private FirebaseAuth.AuthStateListener authListener;
    private AccessTokenTracker accessTokenTracker;
    private static final String TAG = "FacebookAuthentication";
    private Uri photoUrl;
    private String photoUrlstr;
    private String facebookUserId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String value = intent.getStringExtra("key"); //if it's a string you stored.

        setContentView(R.layout.activity_facebook_loggin);
        FacebookSdk.sdkInitialize(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        FacebookSdk.getApplicationContext();
        //antistoixizw me layout.xml kai koympwnw to koympi

        Url = findViewById(R.id.urlText);
        userName = findViewById(R.id.nameText);
        profilePic = findViewById(R.id.profileView);
        fbLogin = findViewById(R.id.login_button);
        fbLogin.setReadPermissions("email", "public_profile");
        //
        callBack = CallbackManager.Factory.create();

        fbLogin.registerCallback(callBack , new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess" + loginResult);
                Toast.makeText(FacebookLoggin.this, "Signed in to facebook successful", Toast.LENGTH_LONG).show();
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "onError" + error );
                Toast.makeText(FacebookLoggin.this, "Signed in to facebook failed", Toast.LENGTH_LONG).show();
            }
        });

        authListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    UpdateUI2(user);
                }
                else{
                    UpdateUI2(null);
                }
            }
        };
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if(currentAccessToken == null){
                    mAuth.signOut();
                }
            }
        };

    }
    private void handleFacebookToken(AccessToken token){
        Log.d(TAG , "HandleFacebookToken" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "Sign in with credential: Successful" );
                Toast.makeText(FacebookLoggin.this, "Sign in with credential: Successful.", Toast.LENGTH_SHORT).show();
                FirebaseUser user = mAuth.getCurrentUser();

                UpdateUI2(user);

                if (!task.isSuccessful()){
                    Toast.makeText(FacebookLoggin.this, "Firebase Auth failed", Toast.LENGTH_LONG).show();
                }


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        callBack.onActivityResult(requestCode, resultCode, data);
    }

    private void UpdateUI2 (FirebaseUser user){
        if(user != null ){
            userName.setText(user.getDisplayName());
            for(UserInfo profile : user.getProviderData()) {
                // check if the provider id matches "facebook.com"
                if(FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                    facebookUserId = profile.getUid();
                }
            }

            String img = String.valueOf(user.getPhotoUrl()); // is = mAuth.getCurrentUser().getPhotoUrl().toString;
            String newToken = "?height=1000&access_token=" + AccessToken.getCurrentAccessToken().getToken();
            Picasso.get().load(img + newToken).into(profilePic);
            Url.setText(img + newToken);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            UpdateUI2(currentUser);
            userName.setText(currentUser.getDisplayName());
        }
        mAuth.addAuthStateListener(authListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authListener != null){
            mAuth.removeAuthStateListener(authListener);
        }
    }



}