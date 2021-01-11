package uom.edu.smnaggr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FetchOauth2 extends AsyncTask<String, Void,String> {
    private final String LOG_TAG = FetchOauth2.class.getSimpleName();
    private String jsonString;

    public FetchOauth2(String jsonString){
        this.jsonString = jsonString;
    }

    private String getStringFromInputStream(String authJSONString) throws JSONException {
        final String token_type = "token_type";
        final String access_token = "access_token";
        JSONObject authString = new JSONObject(authJSONString);
        String resultType;
        //resultType = authString.getJSONObject().getString(token_type);
        String result="mao2";
        return result;
    }

    @Override
    protected String doInBackground(String... params){
        HttpURLConnection httpConnection = null;
        OutputStream outputStream = null;
        BufferedReader bufferedReader = null;
        StringBuilder response = null;
        String authJSONString = "empty";

        try {
            URL url = new URL("https://api.twitter.com/oauth2/token");
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("POST");
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);

            String accessCredential = R.string.twitter_consumer_key + ":"
                    + R.string.twitter_consumer_secret;
            String authorization = "Basic "
             + Base64.encodeToString(accessCredential.getBytes(),
                Base64.NO_WRAP);
            String param = "grant_type=client_credentials";

            httpConnection.addRequestProperty("Authorization", authorization);
            httpConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            httpConnection.connect();

            outputStream = httpConnection.getOutputStream();
            outputStream.write(param.getBytes());
            outputStream.flush();
            outputStream.close();
            // int statusCode = httpConnection.getResponseCode();
            // String reason =httpConnection.getResponseMessage();

            //
            StringBuilder buffer = new StringBuilder();
            String mao1="mao";
            if (httpConnection.getResponseCode() == 200) {
                InputStream inputStream = httpConnection.getInputStream();

                if (inputStream == null) {
                    return mao1;
                }
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
            }
            authJSONString = buffer.toString();

            //
/*
            bufferedReader = new BufferedReader(new InputStreamReader(
                    httpConnection.getInputStream()));
            String line;
            response = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }

            Log.d(LOG_TAG,
                    "POST response code: "
                            + String.valueOf(httpConnection.getResponseCode()));
            Log.d(LOG_TAG, "JSON response: " + response.toString());

 */

        } catch (Exception e) {
            Log.e(LOG_TAG, "POST error: " + Log.getStackTraceString(e));

        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }
        return authJSONString;

    }

    @Override
    protected void onPostExecute(String jsonString) {
        this.jsonString = jsonString;
    }
}