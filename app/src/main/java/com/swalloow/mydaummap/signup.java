package com.swalloow.mydaummap;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class signup extends AppCompatActivity {

    String email, password, name, phone, password_repeat;
    String myJSON;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "가입을 환영합니다", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(getApplicationContext(), login.class);
//                startActivity(intent);
                password = ((EditText) (findViewById(R.id.editText_PW))).getText().toString();
                password_repeat = ((EditText) (findViewById(R.id.editText_RPW))).getText().toString();
                email = ((EditText) (findViewById(R.id.editText_EM))).getText().toString();
                name = ((EditText) (findViewById(R.id.editText_NAME))).getText().toString();
                phone = ((EditText) (findViewById(R.id.editText_PHON))).getText().toString();

                if (email.length() < 1 || password.length() < 1 || name.length() < 1 || phone.length() < 1)
                    Toast.makeText(getBaseContext(), "모든 항목을 채워주세요", Toast.LENGTH_LONG).show();
                else if (!password.equals(password_repeat)) {
                    Toast.makeText(getBaseContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_LONG).show();
                } else { // 여기에 기술한다.
                    JoinTask myThread = new JoinTask();
                    myThread.execute();
                }
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "signup Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.swalloow.mydaummap/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "signup Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.swalloow.mydaummap/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private class JoinTask extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... voids) {
            try {
                //v1 URL url = new URL("http://203.230.100.124/test2.php");

                URL url = new URL("http://ec2-52-78-132-224.ap-northeast-2.compute.amazonaws.com:3000/users");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setUseCaches(false);
                con.setRequestMethod("POST");
                con.setRequestProperty("content-type", "application/x-www-form-urlencoded");

                String param = "email=" + email + "&password=" + password + "&name=" + name + "&phone=" + phone;

                OutputStreamWriter outStream = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
                PrintWriter writer = new PrintWriter(outStream);
                writer.write(param);
                writer.flush();
                outStream.close();

                BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String json;

                while ((json = rd.readLine()) != null) {
                    sb.append(json + "\n");
                }
                return sb.toString().trim();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String qResult) {
            try {
                myJSON = qResult;
                JSONObject jsonObj = new JSONObject(myJSON);

                if (jsonObj.getString("code").equals("1")) {
                    Toast.makeText(getBaseContext(), jsonObj.getString("result"), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), login.class);
                    startActivity(i);
                } else if (jsonObj.getString("result").equals("3")) {
                    Toast.makeText(getBaseContext(), jsonObj.getString("result"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
