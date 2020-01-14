package com.swalloow.mydaummap;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
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

public class score_main extends AppCompatActivity {

    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<String> arrayList;
    List<String> test = new ArrayList<>();
    List<String> test2 = new ArrayList<>();
    ArrayList<String> course_name = new ArrayList<String>();
    String golfcourse_name;
    JSONObject result_JSON;
    Intent intent;
    Button button;
    String info;

    // 골프장 이름들
    ArrayList<String> golfcourseTitle = new ArrayList<String>();
    ArrayList<String> golfcourseDate = new ArrayList<String>();

    static String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_main);

        arrayList = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(score_main.this,android.R.layout.simple_list_item_1, arrayList);

        listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);

        Log.v("test입니다", "getScoreTask 수행");
        getScoreTask getScoreTask = new getScoreTask();
        getScoreTask.execute();



        button = (Button)findViewById(R.id.buttonadd);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(score_main.this, score_make.class);
                startActivityForResult(intent, 0);
            }
        });
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position == id)
//                {
//                    intent = new Intent(score_main.this, score_user.class);
//                    intent.putExtra("name",info);
//                    intent.putExtra("coursename",course_name);
//                    intent.putStringArrayListExtra("test", (ArrayList<String>) test);
//                    intent.putStringArrayListExtra("test2", (ArrayList<String>) test2);
//                    startActivity(intent);
//                }
//                else
//                {
//                    Toast.makeText(getApplicationContext(),"골프장이름을 클릭해주세요.",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });



    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //arrayList.add(data.getStringExtra("info").toString());
        course_name = data.getStringArrayListExtra("coursename");
        golfcourse_name = data.getStringExtra("info");
        test = data.getStringArrayListExtra("test");
        test2 = data.getStringArrayListExtra("test2");
        adapter.notifyDataSetChanged();
        super.onActivityResult(requestCode, resultCode, data);
    }


    // 데이터 요청해서 스트링으로 다 받아오는 함수
    public String downloadInfo(String path) {
        URL url = null;
        HttpURLConnection con = null;
        BufferedReader in = null;
        String resultStr = "";
        String str = "";

        try {
            Log.v("cookie : ", "score_main 들어왔음여");
            url = new URL(path);
            con = (HttpURLConnection) url.openConnection();
            Log.v("cookie : ", "score_main에서 con만듬여");
            Log.v("cookie : ", "score_main에서 cookie 저장소 사이즈, " + login.msCookieManager.getCookieStore().getCookies().size());

            if (login.msCookieManager.getCookieStore().getCookies().size() > 0) {
                Log.v("cookie : ", "쿠키장착!");
                con.setRequestProperty("Cookie", TextUtils.join(";", login.msCookieManager.getCookieStore().getCookies()));
//                con.setRequestProperty("Cookie", login.cookie);
                Log.v("cookie : ",  TextUtils.join(";", login.msCookieManager.getCookieStore().getCookies()));
            }


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

    private class getScoreTask extends AsyncTask<Void, Void, Integer> {
        // 데이터 받아오는 부분
        protected Integer doInBackground(Void... urls) {
            Log.v("test입니다", "doInbackground 수행");
            try {
                Log.v("test입니다", "try문 진입");
                result_JSON = new JSONObject(downloadInfo("http://ec2-52-78-132-224.ap-northeast-2.compute.amazonaws.com:3000/golfcourses/me"));

                if (result_JSON.getString("code").equals("1")) {
                    // result값 (json객체배열임)을 jsonArray로 받는다.
                    JSONArray result_JSON_array = result_JSON.getJSONArray("result");
                    Log.v("test입니다", "반복문 수행 전");

                    for(int i=0; i<result_JSON_array.length(); i++) {
                        Log.v("test입니다", "반복문 수행 " + i);
                        golfcourseTitle.add(result_JSON_array.getJSONObject(i).getString("name"));
                        golfcourseDate.add(result_JSON_array.getJSONObject(i).getString("play_dtime"));
                    }
                    Log.v("test입니다 : ", golfcourseTitle.toString());
                    Log.v("test입니다 : ", golfcourseDate.toString());
                }
            } catch (Exception e) {
                    e.printStackTrace();
            }
            return new Integer(0);
        }

        // 받아온 데이터로 액티비티를 꾸리는 부분
        protected void onPostExecute(Integer result) {
            Log.v("test입니다 : ", golfcourseTitle.toString());
            Log.v("test입니다 : ", golfcourseDate.toString());
            Log.v("test입니다", "onPostExecute 수행");
            Log.i("main golfcourseall",""+golfcourse_name);
            for(int i = 0 ;i<golfcourseTitle.size();i++)
            {
                arrayList.add(golfcourseTitle.get(i));
            }
            Log.i("test last",""+golfcourseTitle.get(0));

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    int pos = (int) parent.getAdapter().getItem(position);
                    if (position == id) {
                        intent = new Intent(score_main.this, score_user.class);
                        intent.putExtra("name", golfcourseTitle.get(position));
                        Log.i("main golfcourse_name",""+golfcourseTitle.get(position));
                        intent.putExtra("coursename", course_name);
                        intent.putStringArrayListExtra("test", (ArrayList<String>) test);
                        intent.putStringArrayListExtra("test2", (ArrayList<String>) test2);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "골프장이름을 클릭해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            });



            // 리스트뷰로 화면 꾸리는 부분의 코드를 넣어주면 됨
            // 사용되는 데이터는 위에서 받아온 golfcourseTitle, golfcourseDate (ArrayList 형식임)
        }
    }
}