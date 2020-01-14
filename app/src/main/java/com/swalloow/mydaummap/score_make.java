package com.swalloow.mydaummap;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;

public class score_make extends AppCompatActivity {

    // 골프장코스이름을 입력받는 부분
    String golfcourse_name = "";                                                                 //골프장이름
    ArrayList<String> course_name = new ArrayList<String>();                                     //코스이름 2개
    ArrayList<String> par_cnt = new ArrayList<String>();                                          //par라디오버튼값 0~18

    Intent intent;
    EditText editText;
    EditText editText1;
    EditText editText2;
    String[] radioID = new String[10];
    String[] radioID2 = new String[10];
    int[] resid = new int[10];
    int[] resid2 = new int[10];
    private RadioGroup[] rg = new RadioGroup[10];
    private RadioGroup[] rg2 = new RadioGroup[10];
    private RadioButton[] rb = new RadioButton[10];
    private RadioButton[] rb2 = new RadioButton[10];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_make);
        intent = getIntent();
        editText = (EditText)findViewById(R.id.editname);

        editText1 = (EditText)findViewById(R.id.corse1);
        editText2 = (EditText)findViewById(R.id.corse2);
        //1번쨰 라디오버튼
        for(int i=0;i<10;i++)
        {
            radioID[i] = "dialog"+i;
            resid[i] = getResources().getIdentifier(radioID[i],"id",getCallingActivity().getPackageName());
            rg[i] = (RadioGroup)findViewById(resid[i]);

        }
        //2번쨰 라디오버튼
        for(int j=0;j<10;j++)
        {
            radioID2[j] = "dialog_"+j;
            resid2[j] = getResources().getIdentifier(radioID2[j],"id",getCallingActivity().getPackageName());
            rg2[j] = (RadioGroup)findViewById(resid2[j]);
        }



        Button button = (Button)findViewById(R.id.buttonscore);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(score_make.this, score_main.class);
                for (int i = 0; i < 9; i++) {
                    rb[i] = (RadioButton) findViewById(rg[i].getCheckedRadioButtonId());
                }

                for (int i = 0; i < 9; i++)
                {
                    rb2[i] = (RadioButton)findViewById(rg2[i].getCheckedRadioButtonId());
                }

                golfcourse_name = editText.getText().toString();

                Log.i("make golfcourse_name",""+golfcourse_name);
                course_name.add(editText1.getText().toString());
                course_name.add(editText2.getText().toString());
                intent.putStringArrayListExtra("coursename",course_name);
                //라디오버튼 1번
                for(int i=0;i<9;i++)
                {
                    par_cnt.add(rb[i].getText().toString());
                }
                Log.i("par_cnt",""+par_cnt);

                //라디오버튼 2번
                for(int i=0;i<9;i++)
                {
                    par_cnt.add(rb2[i].getText().toString());
                }

                Log.i("par_cnt", "" + par_cnt);
                intent.putStringArrayListExtra("test", par_cnt);

                insertGolfcourseTask myTask = new insertGolfcourseTask();
                myTask.execute();

                intent.putExtra("info",golfcourse_name);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        Button buttonadd = (Button) findViewById(R.id.buttonchu);
        buttonadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inflate();
            }
        });
    }

    private void inflate()
    {
        LinearLayout contentsLayout = (LinearLayout)findViewById(R.id.contentsLayout);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.score_makemore,contentsLayout);
    }

    private class insertGolfcourseTask extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... voids) {
            try {
                //v1 URL url = new URL("http://203.230.100.124/test2.php");

                Log.v("score_make: ", "score_make에 요청을 만들기 시작");
                URL url = new URL("http://ec2-52-78-132-224.ap-northeast-2.compute.amazonaws.com:3000/golfcourses");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                Log.v("score_make : ", "score_make에서 cookie 저장소 사이즈, " + login.msCookieManager.getCookieStore().getCookies().size());
                if (login.msCookieManager.getCookieStore().getCookies().size() > 0) {
                    Log.v("score_make: ", "score_make에서 cookie 장착!");
                    con.setRequestProperty("Cookie", TextUtils.join(";", login.msCookieManager.getCookieStore().getCookies()));
                }
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setUseCaches(false);
                con.setRequestMethod("POST");
                con.setRequestProperty("content-type", "application/x-www-form-urlencoded");



                String param = "golfcourse_name=" + golfcourse_name;

                for (int i = 0; i < course_name.size(); i ++) {
                    param += "&course_name=" + course_name.get(i);
                }

                for (int j = 0; j < par_cnt.size(); j ++) {
                    param += "&par_cnt=" + par_cnt.get(j);
                }

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
                JSONObject jsonObj = new JSONObject(qResult);

                Toast.makeText(getBaseContext(), jsonObj.getString("result"), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), score_user.class);
                startActivity(i);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
