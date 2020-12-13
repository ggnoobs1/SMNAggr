package uom.edu.smnaggr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.TwitterAuthProvider;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import twitter4j.HashtagEntity;
import twitter4j.Query;
import twitter4j.Status;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


public class TwitterLoggin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TwitterLoginButton mTwitterBtn;
    private TextView TviewEmail;

    //extras
    private ImageView profilePic2;
    private TextInputEditText message;
    private ImageView icoGallery;
    private static final int SELECT_PICTURE = 100;
    //extras end


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //init
        Intent intent = getIntent();
        String value = intent.getStringExtra("key"); //if it's a string you stored.

        TwitterAuthConfig mTwitterAuthConfig = new TwitterAuthConfig(getString(R.string.twitter_consumer_key),
                getString(R.string.twitter_consumer_secret));
        TwitterConfig twitterConfig = new TwitterConfig.Builder(this)
                .twitterAuthConfig(mTwitterAuthConfig)
                .build();
        Twitter.initialize(twitterConfig);

        setContentView(R.layout.activity_twitter_loggin);
        mAuth = FirebaseAuth.getInstance();

        //extra on create


        message = findViewById(R.id.maoText);
        profilePic2 = findViewById(R.id.twitterPic);
        icoGallery = (ImageView) findViewById(R.id.icoGallery);
        findViewById(R.id.icoGallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        //end

        mTwitterBtn = findViewById(R.id.twitter_login_button);
        TviewEmail = findViewById(R.id.emailTxt);



        mTwitterBtn.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Toast.makeText(TwitterLoggin.this, "Signed in to twitter successful", Toast.LENGTH_LONG).show();
                signInToFirebaseWithTwitterSession(result.data);
                UpdateTwitterButton();
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(TwitterLoggin.this, "Login failed. No internet or No Twitter app found on your phone", Toast.LENGTH_LONG).show();
                UpdateTwitterButton();
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    updateUI(user);
                }
                else{
                    updateUI(null);
                }
            }
        };
        UpdateTwitterButton();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mTwitterBtn.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                // Get the url from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // Get the path from the Uri
                    icoGallery.setImageURI(null);
                    icoGallery.setImageURI(selectedImageUri);

                }
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI(currentUser);
        }
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void updateUI(FirebaseUser user) {
        Toast.makeText(TwitterLoggin.this, "You're logged in", Toast.LENGTH_LONG);
        if(user != null ){
            TviewEmail.setText(user.getDisplayName());
            Picasso.get().load(user.getPhotoUrl().toString()).into(profilePic2);
        }


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
                        Toast.makeText(TwitterLoggin.this, "Signed in firebase twitter successful", Toast.LENGTH_LONG).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                        if (!task.isSuccessful()){
                            Toast.makeText(TwitterLoggin.this, "Auth firebase twitter failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    //extra methods
    public void signOutTwitter(View view) {
        FirebaseAuth.getInstance().signOut();
        mTwitterBtn.setVisibility(View.VISIBLE);
    }

    public void shareTwitter(View view) {
        //
        String mao = String.valueOf(message.getText());
       // String tweetUrl = "https://twitter.com/intent/tweet?text="+mao+" &url="
       //         + "https://www.facebook.com/SMNAggr-103223751666202";
       // Uri uri = Uri.parse(tweetUrl);
       // startActivity(new Intent(Intent.ACTION_VIEW, uri));

        icoGallery.invalidate();
        BitmapDrawable drawable = (BitmapDrawable) icoGallery.getDrawable();
        Bitmap imagebitmap = drawable.getBitmap();


        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(
                Intent.EXTRA_TEXT,
                "Check this out, what do you think?"
                        + System.getProperty("line.separator")
                        + mao);

        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM,
                getImageUri(TwitterLoggin.this, imagebitmap));
        intent.setPackage("com.twitter.android");
        startActivity(intent);
        //
    }
    // Metatroph eikonase se binary
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(
                inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    // Dialegei mia eikona apo th syllogh
    void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }




}
