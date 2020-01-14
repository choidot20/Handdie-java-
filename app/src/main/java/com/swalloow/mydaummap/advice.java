package com.swalloow.mydaummap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class advice extends AppCompatActivity {
    Intent intent;
    String field_name, field_content, club_type, club_number, club_name, club_content, club_distance;
    static int major, minor;
    int distance; // 홀부터 비콘까지의 거리

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advice);

        Log.i("String","major:"+major);
        Log.i("String1","minor:"+minor);

        // 안드로이드 2.3.3 버전부터 사용해야 할 쓰레드의 정책과 관련한 StrictMode를 지정한다.
        // 규약에는 디스크 쓰기/읽기 작업, 네트워크 사용과 같이 메인 쓰레드에서 언제 끝날지 모르는 작업을 대기한다는 것은 문제가있다.
        // 즉, 상황에 따라 언제 끝날지 모르는 작업 등을 위해 무한정 대기 상태에 놓이지 않게 쓰레드 정책을 설정하는 것
        //StrictMode의 모든 규약을 허용한 후 적용
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

       GetAdviceTask GetAdviceTask = new GetAdviceTask();
       GetAdviceTask.execute();
    }

    // 날씨정보를 얻기위한 함수
    public String downloadInfo(String path) {

        URL url = null;
        HttpURLConnection con = null;
        BufferedReader in = null;
        String resultStr = "";
        String str = "";

        try {
            url = new URL(path);
            con = (HttpURLConnection) url.openConnection();
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            while ((str = in.readLine()) != null) {
                resultStr += str;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultStr;
    }


   private class GetAdviceTask extends AsyncTask<Double, Integer,  Bitmap[]> {
        //LinkedList result = new LinkedList();
        Bitmap[] bitmap = new Bitmap[4];


        protected  Bitmap[] doInBackground(Double... url) {

            try {
                distance = 180;

                String param = "?beacon_minor=" + minor + "&beacon_major=" + major + "&distance=" + distance;
                Log.i("minor", minor + "");
                Log.i("major", major + "");
                Log.i("distance", distance + "");



                JSONObject result_JSON = new JSONObject(downloadInfo("http://ec2-52-78-132-224.ap-northeast-2.compute.amazonaws.com:3000/advices" + param));
                Log.i("result_JSON", result_JSON.toString()); // 다시 돌려봐

                field_name = result_JSON.getJSONObject("result").getString("field_name");
                field_content = result_JSON.getJSONObject("result").getString("field_content");
                club_type = result_JSON.getJSONObject("result").getString("club_type");
                club_number = result_JSON.getJSONObject("result").getString("club_number");
                club_name = result_JSON.getJSONObject("result").getString("club_name");
                club_content = result_JSON.getJSONObject("result").getString("club_content");
                club_distance = result_JSON.getJSONObject("result").getString("club_distance");



                Log.i("check","check"+field_content);
                Log.i("check","check"+club_number);
                Log.i("check","check"+club_type);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap[] result) {
            // 여기다가 set text하면 된다 민범아
            ImageView fairway, green, hazard, rough, sand, tea, wood, iron, putter;
            TextView field_result, field_nm, club_numb, club_result, club_ty;

            fairway = (ImageView)findViewById(R.id.field_fairway);
            green = (ImageView)findViewById(R.id.field_green);
            hazard= (ImageView)findViewById(R.id.field_hazard);
            rough = (ImageView)findViewById(R.id.field_rough);
            sand = (ImageView)findViewById(R.id.field_sand);
            tea = (ImageView)findViewById(R.id.field_tea);

            wood = (ImageView)findViewById(R.id.club_wood);
            iron = (ImageView)findViewById(R.id.club_iron);
            putter = (ImageView)findViewById(R.id.club_putter);

            field_result = (TextView)findViewById(R.id.field_result);
            field_nm = (TextView)findViewById(R.id.field_name);
            club_numb = (TextView)findViewById(R.id.club_number);
            club_result = (TextView)findViewById(R.id.club_result);
            club_ty = (TextView)findViewById(R.id.club_type);

            Log.i("check","check"+field_content);
            Log.i("check","check"+club_number);
            Log.i("check","check"+club_type);
            Log.i("check","check"+club_content);
            field_result.setText(field_content);
            field_nm.setText(field_name);
            club_numb.setText(club_name);
            club_ty.setText("(" + club_type + " " + club_number + ")");
            club_result.setText(club_content);

            if(field_name.equals("Green")){
                fairway.setVisibility(View.INVISIBLE);
                green.setVisibility(View.VISIBLE);
                hazard.setVisibility(View.INVISIBLE);
                rough.setVisibility(View.INVISIBLE);
                sand.setVisibility(View.INVISIBLE);
                tea.setVisibility(View.INVISIBLE);
            }
            else if(field_name.equals("Hazard")){
                fairway.setVisibility(View.INVISIBLE);
                green.setVisibility(View.INVISIBLE);
                hazard.setVisibility(View.VISIBLE);
                rough.setVisibility(View.INVISIBLE);
                sand.setVisibility(View.INVISIBLE);
                tea.setVisibility(View.INVISIBLE);
            }
            else if(field_name.equals("Rough")){
                fairway.setVisibility(View.INVISIBLE);
                green.setVisibility(View.INVISIBLE);
                hazard.setVisibility(View.INVISIBLE);
                rough.setVisibility(View.VISIBLE);
                sand.setVisibility(View.INVISIBLE);
                tea.setVisibility(View.INVISIBLE);
            }
            else if(field_name.equals("Bunker")){
                fairway.setVisibility(View.INVISIBLE);
                green.setVisibility(View.INVISIBLE);
                hazard.setVisibility(View.INVISIBLE);
                rough.setVisibility(View.INVISIBLE);
                sand.setVisibility(View.VISIBLE);
                tea.setVisibility(View.INVISIBLE);
            }
            else if(field_name.equals("Teeing area")){
                fairway.setVisibility(View.INVISIBLE);
                green.setVisibility(View.INVISIBLE);
                hazard.setVisibility(View.INVISIBLE);
                rough.setVisibility(View.INVISIBLE);
                sand.setVisibility(View.INVISIBLE);
                tea.setVisibility(View.VISIBLE);
            }
            else {
                fairway.setVisibility(View.VISIBLE);
                green.setVisibility(View.INVISIBLE);
                hazard.setVisibility(View.INVISIBLE);
                rough.setVisibility(View.INVISIBLE);
                sand.setVisibility(View.INVISIBLE);
                tea.setVisibility(View.INVISIBLE);
            }

            if(club_type.equals("Iron")){
                wood.setVisibility(View.INVISIBLE);
                iron.setVisibility(View.VISIBLE);
                putter.setVisibility(View.INVISIBLE);
            }
            else if(club_type.equals("putter")){
                wood.setVisibility(View.INVISIBLE);
                iron.setVisibility(View.INVISIBLE);
                putter.setVisibility(View.VISIBLE);
            }
            else{
                wood.setVisibility(View.VISIBLE);
                iron.setVisibility(View.INVISIBLE);
                putter.setVisibility(View.INVISIBLE);
            }
        }
    }
}
