package uom.edu.smnaggr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenManager;
import com.facebook.AccessTokenSource;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


public class FacebookLoggin extends AppCompatActivity {

    public static final String user_id="152620139971720";
    public static final String page_id="103223751666202";
    public static final String string_page_token="EAADNOPeW1AUBANFZBU4APKJ5sZBeChYQrM2bXgr2kScdCvONMf6DZA0KAmLQNLwFUuuDMI3Rfx8EbfAK6sxSplD0XWhjPOvplp0WxEgFZC5bxaCxEZA1OA7jFRmFB52xcdyodQLy8D6SZCS1xZCjmaw4mZBhUILKHoZAmizfh2lRKCgxTIHSFmRR9";
    private CallbackManager callBack;
    private FirebaseAuth mAuth;
    private ImageView profilePic;
    private LoginButton fbLogin;
    private FirebaseAuth.AuthStateListener authListener;
    private AccessTokenTracker accessTokenTracker;
    private static final String TAG = "FacebookAuthentication";
    private static final String LOG_TAG ="share";
    private static final int SELECT_PICTURE = 100;
    private ImageView icoGalleryfb;
    private TextInputEditText input;
    private CheckBox insta, facebook, twitter;
    private NewsAdapter newsAdapter;
    private PostAdapter postAdapter;
    private String stored_post_id;
    private String token1="mao";
    private String secret1="mao";
    private String confirmationText=null;


    private CallbackManager callbackManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        token1 = intent.getStringExtra("token");
        secret1 = intent.getStringExtra("secret");
        setContentView(R.layout.activity_facebook_loggin);

        mAuth = FirebaseAuth.getInstance();
        FacebookSdk.getApplicationContext();
        //antistoixizw me layout.xml kai koympwnw to koympi
        icoGalleryfb = (ImageView) findViewById(R.id.icoGalleryfb);
        findViewById(R.id.icoGalleryfb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });
        //TODO: diagraphi twn list view sto facebookLoggin kai veltiwsh toy graphikoy perivallontos kai twn koympiwn


        newsAdapter = new NewsAdapter(this, R.layout.activity_news_adapter, new ArrayList<FBEntry>());
        postAdapter = new PostAdapter(this, R.layout.activity_post_adapter, new ArrayList<PostEntry>());

        insta = findViewById(R.id.checkbox_instagram);
        twitter = findViewById(R.id.checkbox_twitter);
        facebook = findViewById(R.id.checkbox_facebook);
        input  = findViewById(R.id.inputText);
        profilePic = findViewById(R.id.profileView);
        fbLogin = findViewById(R.id.login_button);
        fbLogin.setPermissions("email", "public_profile","user_photos", "pages_show_list");
        fbLogin.setPermissions("pages_read_engagement","pages_manage_posts");

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
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                // Get the url from data
                Uri selectedImageUri = data.getData();

                if (null != selectedImageUri) {
                    // Get the path from the Uri

                    icoGalleryfb.setImageURI(null);
                    icoGalleryfb.setImageURI(selectedImageUri);

                }
            }
        }
    }

    private void UpdateUI2 (FirebaseUser user){
        if(user != null ){
            for(UserInfo profile : user.getProviderData()) {
                // check if the provider id matches "facebook.com"
                if(FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                    String facebookUserId = profile.getUid();
                    String img = String.valueOf(user.getPhotoUrl()); // is = mAuth.getCurrentUser().getPhotoUrl().toString;
                    String newToken = "?height=1000&access_token=" + AccessToken.getCurrentAccessToken().getToken();
                    Picasso.get().load(img + newToken).into(profilePic);
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            UpdateUI2(currentUser);
        }

        mAuth.addAuthStateListener(authListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authListener != null){
           // mAuth.removeAuthStateListener(authListener);
        }
    }


    @SuppressLint("NewApi")
    public void onShareResult(View view){
        //pairnw to keimeno apo to textfield
        String mao = String.valueOf(input.getText());
        //ftiaxnw thn eikona bitmap
        icoGalleryfb.invalidate();
        BitmapDrawable drawable = (BitmapDrawable) icoGalleryfb.getDrawable();
        Bitmap imagebitmap = drawable.getBitmap();

        callbackManager = CallbackManager.Factory.create();
        final ShareDialog shareDialog = new ShareDialog(this);

        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Log.d(LOG_TAG, "success");
            }
            @Override
            public void onError(FacebookException error) {
                Log.d(LOG_TAG, "error");
            }
            @Override
            public void onCancel() {
                Log.d(LOG_TAG, "cancel");
            }
        });

        if (shareDialog.canShow(SharePhotoContent.class)) {

            //Ftiaxnw to hashtag, to opoio einai apla keimeno, alla den afhnei to fb na valw text alliws
            ShareHashtag shareHashTag = new ShareHashtag.Builder().setHashtag(mao).build();
            //ftiaxnw thn fwto se sharable content
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(imagebitmap)
                    .build();
            SharePhotoContent PhotoContent = new SharePhotoContent.Builder()
                    .setShareHashtag(shareHashTag)
                    .addPhoto(photo)
                    .build();

            //anoigw to share me thn epilegmenh fwtografia kai to periexomeno
            if(facebook.isChecked()) {
                ShareDialog.show(FacebookLoggin.this, PhotoContent);
            }
            if(insta.isChecked()) {
                createInstagramIntent("image/*");
            }
            if(twitter.isChecked()){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(
                        Intent.EXTRA_TEXT,
                        "savas: Ε ψΑξΤΕ τΟ ρε ΠαΙδΙΑ. GooGlE iS YoUr FriEnD"
                                + System.getProperty("line.separator")
                                + mao);

                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM,
                        getImageUri(FacebookLoggin.this, imagebitmap));
                intent.setPackage("com.twitter.android");
                startActivity(intent);
            }

        }
    }

    @SuppressLint("NewApi")
    private void createInstagramIntent(String type){


        BitmapDrawable drawable = (BitmapDrawable) icoGalleryfb.getDrawable();
        Bitmap imagebitmap = drawable.getBitmap();
        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(type);

        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, getImageUri(FacebookLoggin.this, imagebitmap));
        share.setPackage("com.instagram.android");
        // Broadcast the Intent.
        startActivity(Intent.createChooser(share, "Share to"));
       // share.setPackage("com.facebook.katana");
       // com.instagram.android
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(
                inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    /* Choose an image from Gallery */
    void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }


    public void goToPosts(View view) throws JSONException, IOException {
        System.out.println(AccessToken.getCurrentAccessToken().getToken());
        fetch();
    }
    @SuppressLint("NewApi")
    private void fetch(){
        FetchUserPages fetchNewsTask = new FetchUserPages(newsAdapter);
        fetchNewsTask.execute();
    }

    public void goToPostEntry(View view) throws JSONException, IOException{

        System.out.println(string_page_token);
        fetchPostID();
    }
    @SuppressLint("NewApi")
    private void fetchPostID(){
        PostFBApi fetchPostEntryTask = new PostFBApi(postAdapter);
        fetchPostEntryTask.execute();
    }

    public void postSDK(View view){
        final String post_id_value = "id";
        AccessToken newToken = new AccessToken(string_page_token,
                String.valueOf(R.string.facebook_app_id),
                String.valueOf(R.string.user_id),
                null,
                null,
                null,
                null,null,null,null);

        Bundle params = new Bundle();
        params.putString("message", "This_xD_message");
        /* make the API call */
        new GraphRequest(
                newToken,
                "/103223751666202/feed",
                params,
                HttpMethod.POST,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        /* handle the result */
                        try {
                            stored_post_id = response.getJSONObject().getString(post_id_value);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    public void postImageUrl(View view){

        BitmapDrawable drawable = (BitmapDrawable) icoGalleryfb.getDrawable();
        Bitmap imagebitmap = drawable.getBitmap();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        imagebitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] byteArray = bytes.toByteArray();
        String Messegario= String.valueOf(input.getText());


        AccessToken fbToken = new AccessToken(string_page_token,
                String.valueOf(R.string.facebook_app_id),
                String.valueOf(R.string.user_id),
                null,
                null,
                null,
                null,null,null,null);
        Bundle params = new Bundle();
        params.putString("message", Messegario);
        params.putByteArray("multipart/form-data",byteArray);
        /* make the API call */
        new GraphRequest(
                fbToken,
                "/103223751666202/photos",
                params,
                HttpMethod.POST,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        /* handle the result */
                    }
                }
        ).executeAsync();
        /*Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "Check this out, what do you think?" + System.getProperty("line.separator") );

        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, getImageUri(FacebookLoggin.this, imagebitmap));
        intent.setPackage("com.twitter.android");
        startActivity(intent);
        */

        //gia na xeris oti postares tin fotografia kai kani aftomato refresh
        Toast.makeText(FacebookLoggin.this, "image posted successfully ", Toast.LENGTH_LONG).show();

        finish();
        startActivity(getIntent());
    }

    public void postTwitter(View view){
        String postText = String.valueOf(input.getText());

        BitmapDrawable drawable = (BitmapDrawable) icoGalleryfb.getDrawable();
        Bitmap imagebitmap = drawable.getBitmap();

        if (postText==null){
            Toast.makeText(FacebookLoggin.this, "You have not entered text to post, please type something and try again", Toast.LENGTH_LONG).show();
        }
        else {
            if (token1=="mao"){
                Toast.makeText(FacebookLoggin.this, "You have not logged in with Twitter, please login and try again", Toast.LENGTH_LONG).show();
            }
            else {
                PostTwitterAsync postTask = new PostTwitterAsync(token1, secret1, confirmationText, postText,imagebitmap);
                postTask.execute();
            }
        }


    }


}




