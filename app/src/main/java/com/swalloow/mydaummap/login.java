package com.swalloow.mydaummap;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class login extends AppCompatActivity {

    static CookieManager msCookieManager = new CookieManager();
    static String cookie;

    String email, password;
    String myJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

//        View v = new MyView(this);
//        setContentView(v);
    }

//    protected class MyView extends  View{
//        public MyView(Context context)
//        {
//            super(context);
//        }
//
//        public boolean onTouch(MotionEvent event)
//        {
//            if(event.getAction()==MotionEvent.ACTION_BUTTON_PRESS)
//            {
//                Toast.makeText(login.this,"로그인해주세요",Toast.LENGTH_LONG).show();
//                return true;
//            }
//            return false;
//        }
//    }

    public void onClickmain(View v)
    {
        email = ((EditText)(findViewById(R.id.login_editText_EM))).getText().toString();
        password = ((EditText)(findViewById(R.id.login_editText_PW))).getText().toString();

        if(password.length()<1 || email.length()<1)
            Toast.makeText(getBaseContext(), "모든 항목을 채워주세요", Toast.LENGTH_LONG).show();

        else{
            LoginTask myThread = new LoginTask();
            myThread.execute();
        }
    }

    public void onClicksignup(View v)
    {
        Intent intent = new Intent(getApplicationContext(),signup.class);
        startActivity(intent);
    }

    private class LoginTask extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... voids) {
            try{
                URL url = new URL("http://ec2-52-78-132-224.ap-northeast-2.compute.amazonaws.com:3000/auth/local/login");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setUseCaches(false);
                con.setRequestMethod("POST");
                con.setRequestProperty("content-type", "application/x-www-form-urlencoded");

                String param = "email="+ email + "&password=" + password;



                OutputStreamWriter outStream = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
                PrintWriter writer = new PrintWriter(outStream);
                writer.write(param);
                writer.flush();
                outStream.close();



                Log.v("cookie : ", "login전에 cookie 저장소의 사이즈!");
                if (msCookieManager.getCookieStore().getCookies().size() > 0) {
                    con.setRequestProperty("Cookie", TextUtils.join(";", msCookieManager.getCookieStore().getCookies()));
                }


                BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String json;


                while ((json = rd.readLine()) != null) {
                    sb.append(json + "\n");
                }

                Log.v("cookie : ", "login한 뒤 쿠키 받아옴!, " + con.getHeaderField("Set-Cookie"));
                Map<String, List<String>> headerFields = con.getHeaderFields();
                List<String> cookiesHeader = headerFields.get("Set-Cookie");

                if (cookiesHeader != null) {
                    for (String cookie : cookiesHeader) {
                        Log.v("cookie : ", cookie);
                        msCookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                        Log.v("cookie : ", "login한 뒤 쿠키 저장 완료!");
                    }
                }
                return sb.toString().trim();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String qResult){
            try{
                myJSON = qResult;
                JSONObject jsonObj = new JSONObject(myJSON);

                if (jsonObj.getString("code").equals("1")){
                    Toast.makeText(getBaseContext(), "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), main.class);
                    startActivity(i);
                } else Toast.makeText(getBaseContext(), "ID 또는 PW가 다릅니다.", Toast.LENGTH_SHORT).show();

            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
