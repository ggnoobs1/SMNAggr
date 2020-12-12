package uom.edu.smnaggr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
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
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.FacebookType;
import com.restfb.types.Page;
import com.restfb.types.User;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import static com.restfb.Version.LATEST;
import static com.restfb.Version.VERSION_3_1;
import static com.restfb.Version.VERSION_3_2;


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
    private static final String LOG_TAG ="share";
    private static final int SELECT_PICTURE = 100;
    private ImageView icoGalleryfb;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String value = intent.getStringExtra("key"); //if it's a string you stored.


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

        Url = findViewById(R.id.urlText);
        userName = findViewById(R.id.nameText);
        profilePic = findViewById(R.id.profileView);
        fbLogin = findViewById(R.id.login_button);
        fbLogin.setReadPermissions("email", "public_profile","user_photos", "pages_show_list");
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
            userName.setText(user.getDisplayName());
            for(UserInfo profile : user.getProviderData()) {
                // check if the provider id matches "facebook.com"
                if(FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                    String facebookUserId = profile.getUid();
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


    public void onShareResult(View view){
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


        if (shareDialog.canShow(ShareLinkContent.class)) {

            ShareLinkContent linkContent = new ShareLinkContent.Builder()

                    .setContentUrl(Uri.parse("https://developers.facebook.com"))

                    //.setImageUrl(Uri.parse("android.resource://de.ginkoboy.flashcards/" + R.drawable.logo_flashcards_pro))
                   // .setImageUrl(Uri.parse("http://bagpiper-andy.de/bilder/dudelsack%20app.png"))
                    .build();
            icoGalleryfb.invalidate();
            BitmapDrawable drawable = (BitmapDrawable) icoGalleryfb.getDrawable();
            Bitmap imagebitmap = drawable.getBitmap();
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(imagebitmap)
                    .build();
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();

            shareDialog.show(content);
        }
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




}




