package uom.edu.smnaggr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
//import uom.edu.smnaggr.databinding.ActivityMainBinding;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
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
import com.squareup.picasso.Picasso;

import static com.squareup.picasso.Picasso.*;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TwitterLoginButton mTwitterBtn;
    private ProgressBar mIndeterminateProgressBar;

    //apo edw kai katw einai gia to fb
    private CallbackManager callBack;
    private FirebaseAuth auth;
    private TextView userName;
    private ImageView profilePic;
    private LoginButton fbLogin;
    private TextView Url;
    private FirebaseAuth.AuthStateListener authListener;
    private AccessTokenTracker accessTokenTracker;
    private static final String TAG ="FacebookAuthentication";
    private Uri photoUrl;
    private String photoUrlstr;
    //edw teleiwnoyn oi metavlhtes gia to fb


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        setContentView(R.layout.activity_main);
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
                    Log.d(TAG, "Sign in with credential: Succesful" );
                    FirebaseUser user = auth.getCurrentUser();
                    UpdateUI(user);
                }
                else {
                    Log.d(TAG, "Sign in with credential: Failed to login" + task.getException() );
                    Toast.makeText(MainActivity.this , "Authentication Failed", Toast.LENGTH_SHORT );
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
                photoUrlstr = photoUrlstr + "?height=500";
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

    /*
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mTwitterBtn = findViewById(R.id.twitter_login_button);
        mIndeterminateProgressBar = findViewById(R.id.indeterminateProgressBar);

        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                if (firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                }
            }
        };

        UpdateTwitterButton();

        mTwitterBtn.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Toast.makeText(MainActivity.this, "Signed in to twitter successful", Toast.LENGTH_LONG).show();
                signInToFirebaseWithTwitterSession(result.data);
                mTwitterBtn.setVisibility(View.VISIBLE);
                mIndeterminateProgressBar.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(MainActivity.this, "Login failed. No internet or No Twitter app found on your phone", Toast.LENGTH_LONG).show();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                mIndeterminateProgressBar.setVisibility(View.GONE);
                UpdateTwitterButton();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mTwitterBtn.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI();
        }
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void updateUI() {
        Toast.makeText(MainActivity.this, "You're logged in", Toast.LENGTH_LONG);
        //Sending user to new screen after successful login
        Intent mainActivity = new Intent(MainActivity.this, MainActivity.class);
        startActivity(mainActivity);
        finish();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mAuth.removeAuthStateListener(mAuthListener);
    }
    private void UpdateTwitterButton(){
        if (TwitterCore.getInstance().getSessionManager().getActiveSession() == null){
            mTwitterBtn.setVisibility(View.VISIBLE);
        }
        else{
            mTwitterBtn.setVisibility(View.GONE);
        }
    }

    private void signInToFirebaseWithTwitterSession(TwitterSession session){
        AuthCredential credential = TwitterAuthProvider.getCredential(session.getAuthToken().token,
                session.getAuthToken().secret);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(MainActivity.this, "Signed in firebase twitter successful", Toast.LENGTH_LONG).show();
                        if (!task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Auth firebase twitter failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}
*/