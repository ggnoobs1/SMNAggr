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
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class FacebookLoggin extends AppCompatActivity {


    private CallbackManager callBack;
    private FirebaseAuth auth;
    private TextView userName;
    private ImageView profilePic;
    private LoginButton fbLogin;
    private TextView Url;
    private FirebaseAuth.AuthStateListener authListener;
    private AccessTokenTracker accessTokenTracker;
    private static final String TAG = "FacebookAuthentication";
    private Uri photoUrl;
    private String photoUrlstr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String value = intent.getStringExtra("key"); //if it's a string you stored.

        // OAuthProvider.Builder provider = OAuthProvider.newBuilder("twitter.com");

/*
        //This code must be entering before the setContentView to make the twitter login work...
        TwitterAuthConfig mTwitterAuthConfig = new TwitterAuthConfig(getString(R.string.twitter_consumer_key),
                getString(R.string.twitter_consumer_secret));
        TwitterConfig twitterConfig = new TwitterConfig.Builder(this)
                .twitterAuthConfig(mTwitterAuthConfig)
                .build();
        Twitter.initialize(twitterConfig);
*/
        setContentView(R.layout.activity_facebook_loggin);
        //meta to setContentView bazw to firebase gia to facebook
        //
        //
        // FacebookSdk.sdkInitialize(getApplicationContext());
        // AppEventsLogger.activateApp(this);
        auth = FirebaseAuth.getInstance();
        FacebookSdk.getApplicationContext();
        //antistoixizw me layout.xml kai koympwnw to koympi
        Url = findViewById(R.id.urlText);
        userName = findViewById(R.id.nameText);
        profilePic = findViewById(R.id.profileView);
        fbLogin = findViewById(R.id.login_button);
        fbLogin.setReadPermissions("email", "public_profile","user_friends","user_birthday");
        //
        callBack = CallbackManager.Factory.create();

        fbLogin.registerCallback(callBack , new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess" + loginResult);
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "onError" + error );

            }
        });

        authListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    UpdateUI(user);
                }
                else{
                    UpdateUI(null);
                }
            }
        };
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if(currentAccessToken == null){
                    auth.signOut();
                }
            }
        };

    }
    private void handleFacebookToken(AccessToken token){
        Log.d(TAG , "HandleFacebookToken" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "Sign in with credential: Successful" );
                    FirebaseUser user = auth.getCurrentUser();
                    UpdateUI(user);
                }
                else {
                    Log.d(TAG, "Sign in with credential: Failed to login" + task.getException() );
                    Toast.makeText(FacebookLoggin.this , "Authentication Failed", Toast.LENGTH_SHORT );
                    UpdateUI(null);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callBack.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void UpdateUI (FirebaseUser user){
        if(user != null ){
            userName.setText(user.getDisplayName());
            if (user.getPhotoUrl() != null ){
                photoUrl = user.getPhotoUrl();
                photoUrlstr = photoUrl.toString();
               // photoUrlstr = photoUrlstr + "?height=500";
                Url.setText(photoUrlstr);
                Picasso.get().load(photoUrlstr).into(profilePic);
            }
            else {
                //userName.setText("");
                profilePic.setImageResource(R.drawable.com_facebook_tooltip_black_xout);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            UpdateUI(currentUser);
        }
        auth.addAuthStateListener(authListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authListener != null){
            auth.removeAuthStateListener(authListener);
        }
    }



}