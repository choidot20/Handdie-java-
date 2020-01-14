package com.swalloow.mydaummap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class score_user extends AppCompatActivity {

    // 인텐트로 넘어온 골프장 이름을 담는 변수
    Intent a = new Intent();

    String golfcourse_name;

    ArrayList <String> course_name = new ArrayList <String> ();
    ArrayList <String> par_cnt = new ArrayList <String> ();
    ArrayList <String> score = new ArrayList <String> ();

    int[] shot = new int[18];
    int[] shot2 = new int[18];
    int[] putt = new int[18];
    int[] putt2 = new int[18];
    int[] putshot = new int[18];                                ///put값이 들어있는 배열
    int[] shotput = new int[18];                                //shot값이 들어있는 배열
    int[] number = new int[18];
    int[] number2 = new int[18];
    int[] fa = new int[10];
    int[] fa2 = new int[10];
    int[] fsum = new int[9];
    int[] fsum2 = new int[9];
    int total;
    int total2;
    int ffsum=0;
    int ffsum2=0;
    TextView textView;
    TextView textView1;
    TextView textView2;
    TextView texttotal;
    TextView texttotal2;
    String[] shotstring = new String[18];
    String[] shotstring2 = new String[18];
    String[] puttstring = new String[18];
    String[] puttstring2 = new String[18];
    String[] finalsum = new String[18];
    String[] finalsum2 = new String[18];
    TextView textsum;
    TextView textsum2;
    Button bu1;
    Button bu2;
    Button bu3;
    Button bu4;
    Button bu5;
    Button bu6;
    Button bu7;
    Button bu8;
    Button bu9;
    Button bu21;
    Button bu22;
    Button bu23;
    Button bu24;
    Button bu25;
    Button bu26;
    Button bu27;
    Button bu28;
    Button bu29;


    int text=0;
    int text2=0;
    Intent intent;
    Intent intent2;
    Button buttonadd;
    String title;
    private TextView[] par = new TextView[10];
    private TextView[] par2 = new TextView[10];
    String[] coursenamesplit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_user);
        buttonadd = (Button) findViewById(R.id.buttonsave);
        textView = (TextView) findViewById(R.id.texttitle);
        textView1 = (TextView) findViewById(R.id.textfirst);
        textView2 = (TextView) findViewById(R.id.textsd);
        texttotal = (TextView)findViewById(R.id.total);
        texttotal2 = (TextView)findViewById(R.id.total2);
        bu1 = (Button)findViewById(R.id.bu1);
        bu2 = (Button)findViewById(R.id.bu2);
        bu3 = (Button)findViewById(R.id.bu3);
        bu4 = (Button)findViewById(R.id.bu4);
        bu5 = (Button)findViewById(R.id.bu5);
        bu6 = (Button)findViewById(R.id.bu6);
        bu7 = (Button)findViewById(R.id.bu7);
        bu8 = (Button)findViewById(R.id.bu8);
        bu9 = (Button)findViewById(R.id.bu9);
        bu21 = (Button)findViewById(R.id.bu21);
        bu22 = (Button)findViewById(R.id.bu22);
        bu23 = (Button)findViewById(R.id.bu23);
        bu24 = (Button)findViewById(R.id.bu24);
        bu25 = (Button)findViewById(R.id.bu25);
        bu26 = (Button)findViewById(R.id.bu26);
        bu27 = (Button)findViewById(R.id.bu27);
        bu28 = (Button)findViewById(R.id.bu28);
        bu29 = (Button)findViewById(R.id.bu29);
        textsum = (TextView)findViewById(R.id.textsum);
        textsum2 = (TextView)findViewById(R.id.textsum2);

        int id=R.id.PAR1;
        int id2=R.id.PART1;
        //text 1번째 리스너
        for(int i=0;i<10;i++) {
            par[i] = (TextView)findViewById(id+i);
        }
        //text 2번째 리스너
        for(int j=0;j<9;j++)
        {
            par2[j] = (TextView)findViewById(id2+j);
        }

        intent2 = getIntent();
        golfcourse_name = intent2.getStringExtra("name");
        try {
            golfcourse_name = URLEncoder.encode(golfcourse_name, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        getScoreTask myThread = new getScoreTask();
        myThread.execute();

        score_main.name = title;
        buttonadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                intent = new Intent(score_user.this,score_main.class);
//                intent.putExtra("info",golfcourse_name);
//                startActivity(intent);


                InsertScoreTask task = new InsertScoreTask();
                task.execute();
            }
        });
    }

    public String downloadInfo(String path) {
        Log.v("score_user", "downloadInfo 들어옴");
        URL url = null;
        HttpURLConnection con = null;
        BufferedReader in = null;
        String resultStr = "";
        String str = "";

        try {
            Log.v("score_user", "요청 만들기 시작");
            url = new URL(path);
            con = (HttpURLConnection) url.openConnection();
            Log.v("score_user", "쿠키 사이즈 ," + login.msCookieManager.getCookieStore().getCookies().size());
            if (login.msCookieManager.getCookieStore().getCookies().size() > 0) {
                Log.v("score_user", "쿠키장착!");
                con.setRequestProperty("Cookie", TextUtils.join(";", login.msCookieManager.getCookieStore().getCookies()));
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


    private class getScoreTask extends AsyncTask<String, Void, Integer> {
        // 데이터 받아오는 부분
        protected Integer doInBackground(String... urls) {
            try {
                Log.v("score_user", "입력받은 골프 코스 이름, " + golfcourse_name);
                JSONObject result_JSON = new JSONObject(downloadInfo("http://ec2-52-78-132-224.ap-northeast-2.compute.amazonaws.com:3000/scoreboards/me?golfcourse_name=" + golfcourse_name));

                Log.v("score_user", "정보 받기 전");
                if (result_JSON.getString("code").equals("1")) {
                    // result값 (json객체배열임)을 jsonArray로 받는다.
                    JSONObject result_JSON_object = result_JSON.getJSONObject("result");
                    Log.v("score_user", "정보 받기 전");
                    Log.v("score_user", "정보 받아오는 중");

                    //나눔
                    String course_name_temp = result_JSON_object.getString("course_name");
                    coursenamesplit = course_name_temp.split("\"");
                    for(int i = 1;i < coursenamesplit.length; i+=2) {
                        course_name.add(coursenamesplit[i]);
                    }

//                    String pluse[] = course_name.get(1).split("\\[");bg
//                    Log.i("pluse",""+pluse[0]+" "+pluse[1]);
                    //나눔
                    course_name_temp = result_JSON_object.getString("par_cnt");
                    coursenamesplit = course_name_temp.split(",");
                    for(int i = 0;i < coursenamesplit.length; i++) {
                        par_cnt.add(coursenamesplit[i]);
                    }



                    //나눔
                    String temp = result_JSON_object.getString("score");
                    course_name_temp = temp.substring(temp.indexOf("[") + 1, temp.lastIndexOf("]"));
                    Log.v("final", course_name_temp);

                    coursenamesplit = course_name_temp.split(",");
                    for(int i = 0;i < coursenamesplit.length; i++) {
                        score.add(coursenamesplit[i]);
                        Log.i("user score", "" + score.get(i));
                    }
                    for(int i = 0;i<9;i++)
                    {
                        fsum[i] = Integer.valueOf(score.get(i));
                    }

                    for(int i = 9;i<18;i++)
                    {
                        fsum2[i-9] = Integer.valueOf(score.get(i));
                    }

                    Log.i("user course_name", "" + course_name.get(0));
                    Log.i("user par_cnt", "" + par_cnt.get(1));



                    Log.v("score_user", "정보 모두 받아옴");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new Integer(0);
        }

        // 받아온 데이터로 액티비티를 꾸리는 부분
        protected void onPostExecute(Integer result) {
            // 리스트뷰로 화면 꾸리는 부분의 코드를 넣어주면 됨
            // 사용되는 데이터는 위에서 받아온 golfcourseTitle, golfcourseDate (ArrayList 형식임)
            textView.setText(golfcourse_name);                     //골프장이름
            textView1.setText(course_name.get(0));                 //코스이름
            textView2.setText(course_name.get(1));



            par[0].setText(String.valueOf(par_cnt.get(0).charAt(1)));
            for(int i = 1;i<9;i++)
            {
                par[i].setText(par_cnt.get(i));
            }
            par2[8].setText(String.valueOf(par_cnt.get(coursenamesplit.length - 1).charAt(0)));
            for(int j = 9 ; j<17;j++)
            {
                par2[j-9].setText(par_cnt.get(j));
            }

            fa[0] = (int)par_cnt.get(0).charAt(1)-48;
            for(int i=1;i<9;i++)
            {
                fa[i] = Integer.parseInt(par_cnt.get(i));
            }
            Log.i("user fa",""+fa[1]+fa[2]);
            for(int i=0;i<9;i++)
            {
                total = total + fa[i];
            }
            texttotal.setText(String.valueOf(total));           //par합산


//            2번째 스코어카드 par값
            fa2[8] = (int)par_cnt.get(coursenamesplit.length - 1).charAt(0)-48;
            for(int i=9;i<17;i++)
            {
                fa2[i-9] = Integer.parseInt(par_cnt.get(i));
            }
            for(int i=0;i<9;i++)
            {
                total2 = total2 + fa2[i];
            }

            for(int i = 0 ;i<9;i++)
            {
                ffsum = ffsum+fsum[i];
            }
            for(int i = 0;i<9;i++)
            {
                ffsum2 = ffsum2+fsum2[i];
            }

            texttotal2.setText(String.valueOf(total2));

            bu1.setText(score.get(0));
            bu2.setText(score.get(1));
            bu3.setText(score.get(2));
            bu4.setText(score.get(3));
            bu5.setText(score.get(4));
            bu6.setText(score.get(5));
            bu7.setText(score.get(6));
            bu8.setText(score.get(7));
            bu9.setText(score.get(8));

            bu21.setText(score.get(9));
            bu22.setText(score.get(10));
            bu23.setText(score.get(11));
            bu24.setText(score.get(12));
            bu25.setText(score.get(13));
            bu26.setText(score.get(14));
            bu27.setText(score.get(15));
            bu28.setText(score.get(16));
            bu29.setText(score.get(17));

            textsum.setText(String.valueOf(ffsum));
            textsum2.setText(String.valueOf(ffsum2));

        }
    }

    private class InsertScoreTask extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... voids) {
            try {
                //v1 URL url = new URL("http://203.230.100.124/test2.php");

                Log.v("score_make: ", "score_make에 요청을 만들기 시작");
                URL url = new URL("http://ec2-52-78-132-224.ap-northeast-2.compute.amazonaws.com:3000/scoreboards");
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

                for (int j = 0; j < putshot.length; j ++) {
                    param += "&putting_cnt=" + putshot[j];
                }

                for (int j = 0; j < shotput.length; j ++) {
                    param += "&shot_cnt=" + shotput[j];
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
                intent = new Intent(score_user.this,score_main.class);
                intent.putExtra("info",golfcourse_name);
                startActivity(intent);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void mOnClick(View v)
    {
        final TextView textsum = (TextView)findViewById(R.id.textsum);
        switch (v.getId())
        {
            case R.id.bu1:
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.score_usermake, null);
                Button button = (Button)dialogView.findViewById(R.id.plus1);
                Button button1 = (Button)dialogView.findViewById(R.id.minus1);
                Button button2 = (Button)dialogView.findViewById(R.id.plus2);
                Button button3 = (Button)dialogView.findViewById(R.id.minus2);

                final TextView textView = (TextView)dialogView.findViewById(R.id.shottext);
                final TextView textView1 = (TextView)dialogView.findViewById(R.id.putttext);
                final TextView textView2 = (TextView)dialogView.findViewById(R.id.sum);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(dialogView);

                textView.setText(shotstring[0]);
                textView1.setText(puttstring[0]);
                textView2.setText(finalsum[0]);



                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bu1.setText(finalsum[0]);
                        textsum.setText(String.valueOf(text));
                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text+=1;
                        shot[0] += 1;
                        shotstring[0] = String.valueOf(shot[0]);
                        textView.setText(shotstring[0]);
                        number[0] = shot[0] + putt[0];
                        finalsum[0] = String.valueOf(number[0]);
                        textView2.setText(finalsum[0]);
                        shotput[0] = shot[0];
                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text-=1;
                        shot[0]-=1;
                        shotstring[0] = String.valueOf(shot[0]);
                        textView.setText(shotstring[0]);
                        number[0] = shot[0]+putt[0];
                        finalsum[0] = String.valueOf(number[0]);
                        textView2.setText(finalsum[0]);
                        shotput[0] = shot[0];
                    }
                });

                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text+=1;
                        putt[0]+=1;
                        puttstring[0] = String.valueOf(putt[0]);
                        textView1.setText(puttstring[0]);
                        number[0] = shot[0]+putt[0];
                        finalsum[0] = String.valueOf(number[0]);
                        textView2.setText(finalsum[0]);
                        putshot[0] = putt[0];
                    }
                });

                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text-=1;
                        putt[0]-=1;
                        puttstring[0] = String.valueOf(putt[0]);
                        textView1.setText(puttstring[0]);
                        number[0] = shot[0]+putt[0];
                        finalsum[0] = String.valueOf(number[0]);
                        textView2.setText(finalsum[0]);
                        putshot[0] = putt[0];
                    }
                });
                builder.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();

                dialog.setCanceledOnTouchOutside(true);

                dialog.show();
                break;

            //1번튼완료

            case R.id.bu2:
                LayoutInflater inflater1 = getLayoutInflater();
                final View dialogView1 = inflater1.inflate(R.layout.score_usermake, null);
                button = (Button) dialogView1.findViewById(R.id.plus1);
                button1 = (Button)dialogView1.findViewById(R.id.minus1);
                button2 = (Button)dialogView1.findViewById(R.id.plus2);
                button3 = (Button)dialogView1.findViewById(R.id.minus2);

                final TextView textview20 = (TextView)dialogView1.findViewById(R.id.shottext);
                final TextView textview21 = (TextView)dialogView1.findViewById(R.id.putttext);
                final TextView textView22 = (TextView)dialogView1.findViewById(R.id.sum);

                textview20.setText(shotstring[1]);
                textview21.setText(puttstring[1]);
                textView22.setText(finalsum[1]);
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setView(dialogView1);
                builder1.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bu2.setText(finalsum[1]);
                        textsum.setText(String.valueOf(text));
                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text+=1;
                        shot[1] += 1;
                        shotstring[1] = String.valueOf(shot[1]);
                        textview20.setText(shotstring[1]);
                        number[1] = shot[1] + putt[1];
                        finalsum[1] = String.valueOf(number[1]);
                        textView22.setText(finalsum[1]);
                        shotput[1] = shot[1];
                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text-=1;
                        shot[1]-=1;
                        shotstring[1] = String.valueOf(shot[1]);
                        textview20.setText(shotstring[1]);
                        number[1] = shot[1]+putt[1];
                        finalsum[1] = String.valueOf(number[1]);
                        textView22.setText(finalsum[1]);
                        shotput[1] = shot[1];
                    }
                });

                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text+=1;
                        putt[1] += 1;
                        puttstring[1] = String.valueOf(putt[1]);
                        textview21.setText(puttstring[1]);
                        number[1] = shot[1] + putt[1];
                        finalsum[1] = String.valueOf(number[1]);
                        textView22.setText(finalsum[1]);
                        putshot[1] = putt[1];
                    }
                });

                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text-=1;
                        putt[1] -= 1;
                        puttstring[1] = String.valueOf(putt[1]);
                        textview21.setText(puttstring[1]);
                        number[1] = shot[1] + putt[1];
                        finalsum[1] = String.valueOf(number[1]);
                        textView22.setText(finalsum[1]);
                        putshot[1] = putt[1];
                    }
                });


                builder1.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog1 = builder1.create();

                dialog1.setCanceledOnTouchOutside(true);

                dialog1.show();
                break;

            //2버튼완료
            case R.id.bu3:
                LayoutInflater inflater2 = getLayoutInflater();
                final View dialogView2 = inflater2.inflate(R.layout.score_usermake, null);
                button = (Button) dialogView2.findViewById(R.id.plus1);
                button1 = (Button)dialogView2.findViewById(R.id.minus1);
                button2 = (Button)dialogView2.findViewById(R.id.plus2);
                button3 = (Button)dialogView2.findViewById(R.id.minus2);

                final TextView textview30 = (TextView)dialogView2.findViewById(R.id.shottext);
                final TextView textview31 = (TextView)dialogView2.findViewById(R.id.putttext);
                final TextView textView32 = (TextView)dialogView2.findViewById(R.id.sum);

                final Button bu3 = (Button)findViewById(R.id.bu3);
                textview30.setText(shotstring[2]);
                textview31.setText(puttstring[2]);
                textView32.setText(finalsum[2]);

                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setView(dialogView2);
                builder2.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bu3.setText(finalsum[2]);
                        textsum.setText(String.valueOf(text));
                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text+=1;
                        shot[2] += 1;
                        shotstring[2] = String.valueOf(shot[2]);
                        textview30.setText(shotstring[2]);
                        number[2] = shot[2] + putt[2];
                        finalsum[2] = String.valueOf(number[2]);
                        textView32.setText(finalsum[2]);
                        shotput[2] = shot[2];
                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text-=1;
                        shot[2] -= 1;
                        shotstring[2] = String.valueOf(shot[2]);
                        textview30.setText(shotstring[2]);
                        number[2] = shot[2] + putt[2];
                        finalsum[2] = String.valueOf(number[2]);
                        textView32.setText(finalsum[2]);
                        shotput[2] = shot[2];
                    }
                });

                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text+=1;
                        putt[2] += 1;
                        puttstring[2] = String.valueOf(putt[2]);
                        textview31.setText(puttstring[2]);
                        number[2] = shot[2] + putt[2];
                        finalsum[2] = String.valueOf(number[2]);
                        textView32.setText(finalsum[2]);
                        putshot[2] = putt[2];
                    }
                });

                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text-=1;
                        putt[2] -= 1;
                        puttstring[2] = String.valueOf(putt[2]);
                        textview31.setText(puttstring[2]);
                        number[2] = shot[2] + putt[2];
                        finalsum[2] = String.valueOf(number[2]);
                        textView32.setText(finalsum[2]);
                        putshot[2] = putt[2];
                    }
                });

                builder2.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog2 = builder2.create();

                dialog2.setCanceledOnTouchOutside(true);

                dialog2.show();
                break;

            case R.id.bu4:
                LayoutInflater inflater3 = getLayoutInflater();
                final View dialogView3 = inflater3.inflate(R.layout.score_usermake, null);

                button = (Button) dialogView3.findViewById(R.id.plus1);
                button1 = (Button)dialogView3.findViewById(R.id.minus1);
                button2 = (Button)dialogView3.findViewById(R.id.plus2);
                button3 = (Button)dialogView3.findViewById(R.id.minus2);

                final TextView textview40 = (TextView)dialogView3.findViewById(R.id.shottext);
                final TextView textview41 = (TextView)dialogView3.findViewById(R.id.putttext);
                final TextView textView42 = (TextView)dialogView3.findViewById(R.id.sum);

                final Button bu4 = (Button)findViewById(R.id.bu4);
                textview40.setText(shotstring[3]);
                textview41.setText(puttstring[3]);
                textView42.setText(finalsum[3]);

                AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
                builder3.setView(dialogView3);
                builder3.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bu4.setText(finalsum[3]);
                        textsum.setText(String.valueOf(text));

                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text+=1;
                        shot[3] += 1;
                        shotstring[3] = String.valueOf(shot[3]);
                        textview40.setText(shotstring[3]);
                        number[3] = shot[3] + putt[3];
                        finalsum[3] = String.valueOf(number[3]);
                        textView42.setText(finalsum[3]);
                        shotput[3] = shot[3];
                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text-=1;
                        shot[3] -= 1;
                        shotstring[3] = String.valueOf(shot[3]);
                        textview40.setText(shotstring[3]);
                        number[3] = shot[3] + putt[3];
                        finalsum[3] = String.valueOf(number[3]);
                        textView42.setText(finalsum[3]);
                        shotput[3] = shot[3];
                    }
                });

                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text+=1;
                        putt[3] += 1;
                        puttstring[3] = String.valueOf(putt[3]);
                        textview41.setText(puttstring[3]);
                        number[3] = shot[3] + putt[3];
                        finalsum[3] = String.valueOf(number[3]);
                        textView42.setText(finalsum[3]);
                        putshot[3] = putt[3];
                    }
                });

                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text-=1;
                        putt[3] -= 1;
                        puttstring[3] = String.valueOf(putt[3]);
                        textview41.setText(puttstring[3]);
                        number[3] = shot[3] + putt[3];
                        finalsum[3] = String.valueOf(number[3]);
                        textView42.setText(finalsum[3]);
                        putshot[3] = putt[3];
                    }
                });


                builder3.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog3 = builder3.create();

                dialog3.setCanceledOnTouchOutside(true);

                dialog3.show();
                break;

            case R.id.bu5:
                LayoutInflater inflater4 = getLayoutInflater();
                final View dialogView4 = inflater4.inflate(R.layout.score_usermake, null);

                button = (Button) dialogView4.findViewById(R.id.plus1);
                button1 = (Button)dialogView4.findViewById(R.id.minus1);
                button2 = (Button)dialogView4.findViewById(R.id.plus2);
                button3 = (Button)dialogView4.findViewById(R.id.minus2);

                final TextView textview50 = (TextView)dialogView4.findViewById(R.id.shottext);
                final TextView textview51 = (TextView)dialogView4.findViewById(R.id.putttext);
                final TextView textView52 = (TextView)dialogView4.findViewById(R.id.sum);

                final Button bu5 = (Button)findViewById(R.id.bu5);
                textview50.setText(shotstring[4]);
                textview51.setText(puttstring[4]);
                textView52.setText(finalsum[4]);

                AlertDialog.Builder builder4 = new AlertDialog.Builder(this);
                builder4.setView(dialogView4);
                builder4.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bu5.setText(finalsum[4]);
                        textsum.setText(String.valueOf(text));
                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text+=1;
                        shot[4] += 1;
                        shotstring[4] = String.valueOf(shot[4]);
                        textview50.setText(shotstring[4]);
                        number[4] = shot[4] + putt[4];
                        finalsum[4] = String.valueOf(number[4]);
                        textView52.setText(finalsum[4]);
                        shotput[4] = shot[4];
                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text-=1;
                        shot[4] -= 1;
                        shotstring[4] = String.valueOf(shot[4]);
                        textview50.setText(shotstring[4]);
                        number[4] = shot[4] + putt[4];
                        finalsum[4] = String.valueOf(number[4]);
                        textView52.setText(finalsum[4]);
                        shotput[4] = shot[4];
                    }
                });

                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text+=1;
                        putt[4] += 1;
                        puttstring[4] = String.valueOf(putt[4]);
                        textview51.setText(puttstring[4]);
                        number[4] = shot[4] + putt[4];
                        finalsum[4] = String.valueOf(number[4]);
                        textView52.setText(finalsum[4]);
                        putshot[4] = putt[4];
                    }
                });

                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text-=1;
                        putt[4] -= 1;
                        puttstring[4] = String.valueOf(putt[4]);
                        textview51.setText(puttstring[4]);
                        number[4] = shot[4] + putt[4];
                        finalsum[4] = String.valueOf(number[4]);
                        textView52.setText(finalsum[4]);
                        putshot[4] = putt[4];
                    }
                });

                builder4.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog4 = builder4.create();

                dialog4.setCanceledOnTouchOutside(true);

                dialog4.show();
                break;

            case R.id.bu6:
                LayoutInflater inflater5 = getLayoutInflater();
                final View dialogView5 = inflater5.inflate(R.layout.score_usermake, null);

                button = (Button) dialogView5.findViewById(R.id.plus1);
                button1 = (Button)dialogView5.findViewById(R.id.minus1);
                button2 = (Button)dialogView5.findViewById(R.id.plus2);
                button3 = (Button)dialogView5.findViewById(R.id.minus2);

                final TextView textview60 = (TextView)dialogView5.findViewById(R.id.shottext);
                final TextView textview61 = (TextView)dialogView5.findViewById(R.id.putttext);
                final TextView textView62 = (TextView)dialogView5.findViewById(R.id.sum);

                final Button bu6 = (Button)findViewById(R.id.bu6);
                textview60.setText(shotstring[5]);
                textview61.setText(puttstring[5]);
                textView62.setText(finalsum[5]);

                AlertDialog.Builder builder5 = new AlertDialog.Builder(this);
                builder5.setView(dialogView5);
                builder5.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bu6.setText(finalsum[5]);
                        textsum.setText(String.valueOf(text));
                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text+=1;
                        shot[5] += 1;
                        shotstring[5] = String.valueOf(shot[5]);
                        textview60.setText(shotstring[5]);
                        number[5] = shot[5] + putt[5];
                        finalsum[5] = String.valueOf(number[5]);
                        textView62.setText(finalsum[5]);
                        shotput[5] = shot[5];
                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text-=1;
                        shot[5] -= 1;
                        shotstring[5] = String.valueOf(shot[5]);
                        textview60.setText(shotstring[5]);
                        number[5] = shot[5] + putt[5];
                        finalsum[5] = String.valueOf(number[5]);
                        textView62.setText(finalsum[5]);
                        shotput[5] = shot[5];
                    }
                });

                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text+=1;
                        putt[5] += 1;
                        puttstring[5] = String.valueOf(putt[5]);
                        textview61.setText(puttstring[5]);
                        number[5] = shot[5] + putt[5];
                        finalsum[5] = String.valueOf(number[5]);
                        textView62.setText(finalsum[5]);
                        putshot[5] = putt[5];
                    }
                });

                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text-=1;
                        putt[5] -= 1;
                        puttstring[5] = String.valueOf(putt[5]);
                        textview61.setText(puttstring[5]);
                        number[5] = shot[5] + putt[5];
                        finalsum[5] = String.valueOf(number[5]);
                        textView62.setText(finalsum[5]);
                        putshot[5] = putt[5];
                    }
                });

                builder5.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog5 = builder5.create();

                dialog5.setCanceledOnTouchOutside(true);

                dialog5.show();
                break;


            case R.id.bu7:
                LayoutInflater inflater6 = getLayoutInflater();
                final View dialogView6 = inflater6.inflate(R.layout.score_usermake, null);

                button = (Button) dialogView6.findViewById(R.id.plus1);
                button1 = (Button)dialogView6.findViewById(R.id.minus1);
                button2 = (Button)dialogView6.findViewById(R.id.plus2);
                button3 = (Button)dialogView6.findViewById(R.id.minus2);

                final TextView textview70 = (TextView)dialogView6.findViewById(R.id.shottext);
                final TextView textview71 = (TextView)dialogView6.findViewById(R.id.putttext);
                final TextView textView72 = (TextView)dialogView6.findViewById(R.id.sum);

                final Button bu7 = (Button)findViewById(R.id.bu7);
                textview70.setText(shotstring[6]);
                textview71.setText(puttstring[6]);
                textView72.setText(finalsum[6]);

                AlertDialog.Builder builder6 = new AlertDialog.Builder(this);
                builder6.setView(dialogView6);
                builder6.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bu7.setText(finalsum[6]);
                        textsum.setText(String.valueOf(text));
                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text+=1;
                        shot[6] += 1;
                        shotstring[6] = String.valueOf(shot[6]);
                        textview70.setText(shotstring[6]);
                        number[6] = shot[6] + putt[6];
                        finalsum[6] = String.valueOf(number[6]);
                        textView72.setText(finalsum[6]);
                        shotput[6] = shot[6];
                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text-=1;
                        shot[6] -= 1;
                        shotstring[6] = String.valueOf(shot[6]);
                        textview70.setText(shotstring[6]);
                        number[6] = shot[6] + putt[6];
                        finalsum[6] = String.valueOf(number[6]);
                        textView72.setText(finalsum[6]);
                        shotput[6] = shot[6];
                    }
                });

                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text+=1;
                        putt[6] += 1;
                        puttstring[6] = String.valueOf(putt[6]);
                        textview71.setText(puttstring[6]);
                        number[6] = shot[6] + putt[6];
                        finalsum[6] = String.valueOf(number[6]);
                        textView72.setText(finalsum[6]);
                        putshot[6] = putt[6];
                    }
                });

                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text-=1;
                        putt[6] -= 1;
                        puttstring[6] = String.valueOf(putt[6]);
                        textview71.setText(puttstring[6]);
                        number[6] = shot[6] + putt[6];
                        finalsum[6] = String.valueOf(number[6]);
                        textView72.setText(finalsum[6]);
                        putshot[6] = putt[6];
                    }
                });

                builder6.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog6 = builder6.create();

                dialog6.setCanceledOnTouchOutside(true);

                dialog6.show();
                break;


            case R.id.bu8:
                LayoutInflater inflater7 = getLayoutInflater();
                final View dialogView7 = inflater7.inflate(R.layout.score_usermake, null);

                button = (Button) dialogView7.findViewById(R.id.plus1);
                button1 = (Button)dialogView7.findViewById(R.id.minus1);
                button2 = (Button)dialogView7.findViewById(R.id.plus2);
                button3 = (Button)dialogView7.findViewById(R.id.minus2);

                final TextView textview80 = (TextView)dialogView7.findViewById(R.id.shottext);
                final TextView textview81 = (TextView)dialogView7.findViewById(R.id.putttext);
                final TextView textView82 = (TextView)dialogView7.findViewById(R.id.sum);

                final Button bu8 = (Button)findViewById(R.id.bu8);
                textview80.setText(shotstring[7]);
                textview81.setText(puttstring[7]);
                textView82.setText(finalsum[7]);

                AlertDialog.Builder builder7 = new AlertDialog.Builder(this);
                builder7.setView(dialogView7);
                builder7.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bu8.setText(finalsum[7]);
                        textsum.setText(String.valueOf(text));
                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text+=1;
                        shot[7] += 1;
                        shotstring[7] = String.valueOf(shot[7]);
                        textview80.setText(shotstring[7]);
                        number[7] = shot[7] + putt[7];
                        finalsum[7] = String.valueOf(number[7]);
                        textView82.setText(finalsum[7]);
                        shotput[7] = shot[7];
                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text-=1;
                        shot[7] -= 1;
                        shotstring[7] = String.valueOf(shot[7]);
                        textview80.setText(shotstring[7]);
                        number[7] = shot[7] + putt[7];
                        finalsum[7] = String.valueOf(number[7]);
                        textView82.setText(finalsum[7]);
                        shotput[7] = shot[7];
                    }
                });

                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text+=1;
                        putt[7] += 1;
                        puttstring[7] = String.valueOf(putt[7]);
                        textview81.setText(puttstring[7]);
                        number[7] = shot[7] + putt[7];
                        finalsum[7] = String.valueOf(number[7]);
                        textView82.setText(finalsum[7]);
                        putshot[7] = putt[7];
                    }
                });

                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text-=1;
                        putt[7] -= 1;
                        puttstring[7] = String.valueOf(putt[7]);
                        textview81.setText(puttstring[7]);
                        number[7] = shot[7] + putt[7];
                        finalsum[7] = String.valueOf(number[7]);
                        textView82.setText(finalsum[7]);
                        putshot[7] = putt[7];
                    }
                });

                builder7.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog7 = builder7.create();

                dialog7.setCanceledOnTouchOutside(true);

                dialog7.show();
                break;


            case R.id.bu9:
                LayoutInflater inflater8 = getLayoutInflater();
                final View dialogView8 = inflater8.inflate(R.layout.score_usermake, null);

                button = (Button) dialogView8.findViewById(R.id.plus1);
                button1 = (Button)dialogView8.findViewById(R.id.minus1);
                button2 = (Button)dialogView8.findViewById(R.id.plus2);
                button3 = (Button)dialogView8.findViewById(R.id.minus2);

                final TextView textview90 = (TextView)dialogView8.findViewById(R.id.shottext);
                final TextView textview91 = (TextView)dialogView8.findViewById(R.id.putttext);
                final TextView textView92 = (TextView)dialogView8.findViewById(R.id.sum);

                final Button bu9 = (Button)findViewById(R.id.bu9);
                textview90.setText(shotstring[8]);
                textview91.setText(puttstring[8]);
                textView92.setText(finalsum[8]);

                AlertDialog.Builder builder8 = new AlertDialog.Builder(this);
                builder8.setView(dialogView8);
                builder8.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bu9.setText(finalsum[8]);
                        textsum.setText(String.valueOf(text));
                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text+=1;
                        shot[8] += 1;
                        shotstring[8] = String.valueOf(shot[8]);
                        textview90.setText(shotstring[8]);
                        number[8] = shot[8] + putt[8];
                        finalsum[8] = String.valueOf(number[8]);
                        textView92.setText(finalsum[8]);
                        shotput[8] = shot[8];
                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text-=1;
                        shot[8] -= 1;
                        shotstring[8] = String.valueOf(shot[8]);
                        textview90.setText(shotstring[8]);
                        number[8] = shot[8] + putt[8];
                        finalsum[8] = String.valueOf(number[8]);
                        textView92.setText(finalsum[8]);
                        shotput[8] = shot[8];
                    }
                });

                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text+=1;
                        text+=1;
                        putt[8] += 1;
                        puttstring[8] = String.valueOf(putt[8]);
                        textview91.setText(puttstring[8]);
                        number[8] = shot[8] + putt[8];
                        finalsum[8] = String.valueOf(number[8]);
                        textView92.setText(finalsum[8]);
                        putshot[8] = putt[8];
                    }
                });

                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text-=1;
                        putt[8] -= 1;
                        puttstring[8] = String.valueOf(putt[8]);
                        textview91.setText(puttstring[8]);
                        number[8] = shot[8] + putt[8];
                        finalsum[8] = String.valueOf(number[8]);
                        textView92.setText(finalsum[8]);
                        putshot[8] = putt[8];
                    }
                });

                builder8.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog8 = builder8.create();

                dialog8.setCanceledOnTouchOutside(true);

                dialog8.show();
                break;

        }

    }
    public void mOnClick2(View v)
    {
        final TextView textsum = (TextView)findViewById(R.id.textsum2);
        switch (v.getId())
        {
            case R.id.bu21:
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.score_usermake, null);
                Button button = (Button)dialogView.findViewById(R.id.plus1);
                Button button1 = (Button)dialogView.findViewById(R.id.minus1);
                Button button2 = (Button)dialogView.findViewById(R.id.plus2);
                Button button3 = (Button)dialogView.findViewById(R.id.minus2);

                final TextView textView = (TextView)dialogView.findViewById(R.id.shottext);
                final TextView textView1 = (TextView)dialogView.findViewById(R.id.putttext);
                final TextView textView2 = (TextView)dialogView.findViewById(R.id.sum);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(dialogView);

                textView.setText(shotstring2[0]);
                textView1.setText(puttstring2[0]);
                textView2.setText(finalsum2[0]);


                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bu1.setText(finalsum2[0]);
                        textsum.setText(String.valueOf(text2));
                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2+=1;
                        shot2[0] += 1;
                        shotstring2[0] = String.valueOf(shot2[0]);
                        textView.setText(shotstring2[0]);
                        number2[0] = shot2[0] + putt2[0];
                        finalsum2[0] = String.valueOf(number2[0]);
                        textView2.setText(finalsum2[0]);
                        shotput[9] = shot2[0];
                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2-=1;
                        shot2[0]-=1;
                        shotstring2[0] = String.valueOf(shot2[0]);
                        textView.setText(shotstring2[0]);
                        number2[0] = shot2[0]+putt2[0];
                        finalsum2[0] = String.valueOf(number2[0]);
                        textView2.setText(finalsum2[0]);
                        shotput[9] = shot2[0];
                    }
                });

                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2+=1;
                        putt2[0]+=1;
                        puttstring2[0] = String.valueOf(putt2[0]);
                        textView1.setText(puttstring2[0]);
                        number2[0] = shot2[0]+putt2[0];
                        finalsum2[0] = String.valueOf(number2[0]);
                        textView2.setText(finalsum2[0]);
                        putshot[9] = putt2[0];
                    }
                });

                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2-=1;
                        putt2[0]-=1;
                        puttstring2[0] = String.valueOf(putt2[0]);
                        textView1.setText(puttstring2[0]);
                        number2[0] = shot2[0]+putt2[0];
                        finalsum2[0] = String.valueOf(number2[0]);
                        textView2.setText(finalsum2[0]);
                        putshot[9] = putt2[0];
                    }
                });
                builder.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();

                dialog.setCanceledOnTouchOutside(true);

                dialog.show();
                break;

            //1번튼완료

            case R.id.bu22:
                LayoutInflater inflater1 = getLayoutInflater();
                final View dialogView1 = inflater1.inflate(R.layout.score_usermake, null);
                button = (Button) dialogView1.findViewById(R.id.plus1);
                button1 = (Button)dialogView1.findViewById(R.id.minus1);
                button2 = (Button)dialogView1.findViewById(R.id.plus2);
                button3 = (Button)dialogView1.findViewById(R.id.minus2);

                final TextView textview20 = (TextView)dialogView1.findViewById(R.id.shottext);
                final TextView textview21 = (TextView)dialogView1.findViewById(R.id.putttext);
                final TextView textView22 = (TextView)dialogView1.findViewById(R.id.sum);

                textview20.setText(shotstring2[1]);
                textview21.setText(puttstring2[1]);
                textView22.setText(finalsum2[1]);
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setView(dialogView1);
                builder1.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bu2.setText(finalsum2[1]);
                        textsum.setText(String.valueOf(text2));
                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2+=1;
                        shot2[1] += 1;
                        shotstring2[1] = String.valueOf(shot2[1]);
                        textview20.setText(shotstring2[1]);
                        number2[1] = shot2[1] + putt2[1];
                        finalsum2[1] = String.valueOf(number2[1]);
                        textView22.setText(finalsum2[1]);
                        shotput[10] = shot2[1];
                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2-=1;
                        shot2[1]-=1;
                        shotstring2[1] = String.valueOf(shot2[1]);
                        textview20.setText(shotstring2[1]);
                        number2[1] = shot2[1]+putt2[1];
                        finalsum2[1] = String.valueOf(number2[1]);
                        textView22.setText(finalsum2[1]);
                        shotput[10] = shot2[1];
                    }
                });

                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2+=1;
                        putt2[1] += 1;
                        puttstring2[1] = String.valueOf(putt2[1]);
                        textview21.setText(puttstring2[1]);
                        number2[1] = shot2[1] + putt2[1];
                        finalsum2[1] = String.valueOf(number2[1]);
                        textView22.setText(finalsum2[1]);
                        putshot[10] = putt2[1];
                    }
                });

                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2-=1;
                        putt2[1] -= 1;
                        puttstring2[1] = String.valueOf(putt2[1]);
                        textview21.setText(puttstring2[1]);
                        number2[1] = shot2[1] + putt2[1];
                        finalsum2[1] = String.valueOf(number2[1]);
                        textView22.setText(finalsum2[1]);
                        putshot[10] = putt2[1];
                    }
                });


                builder1.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog1 = builder1.create();

                dialog1.setCanceledOnTouchOutside(true);

                dialog1.show();
                break;

            //2버튼완료
            case R.id.bu23:
                LayoutInflater inflater2 = getLayoutInflater();
                final View dialogView2 = inflater2.inflate(R.layout.score_usermake, null);
                button = (Button) dialogView2.findViewById(R.id.plus1);
                button1 = (Button)dialogView2.findViewById(R.id.minus1);
                button2 = (Button)dialogView2.findViewById(R.id.plus2);
                button3 = (Button)dialogView2.findViewById(R.id.minus2);

                final TextView textview30 = (TextView)dialogView2.findViewById(R.id.shottext);
                final TextView textview31 = (TextView)dialogView2.findViewById(R.id.putttext);
                final TextView textView32 = (TextView)dialogView2.findViewById(R.id.sum);

                final Button bu3 = (Button)findViewById(R.id.bu23);
                textview30.setText(shotstring2[2]);
                textview31.setText(puttstring2[2]);
                textView32.setText(finalsum2[2]);

                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setView(dialogView2);
                builder2.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bu3.setText(finalsum2[2]);
                        textsum.setText(String.valueOf(text2));
                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2+=1;
                        shot2[2] += 1;
                        shotstring2[2] = String.valueOf(shot2[2]);
                        textview30.setText(shotstring2[2]);
                        number2[2] = shot2[2] + putt2[2];
                        finalsum2[2] = String.valueOf(number2[2]);
                        textView32.setText(finalsum2[2]);
                        shotput[11] = shot2[2];
                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2-=1;
                        shot2[2] -= 1;
                        shotstring2[2] = String.valueOf(shot2[2]);
                        textview30.setText(shotstring2[2]);
                        number2[2] = shot2[2] + putt2[2];
                        finalsum2[2] = String.valueOf(number2[2]);
                        textView32.setText(finalsum2[2]);
                        shotput[11] = shot2[2];
                    }
                });

                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2+=1;
                        putt2[2] += 1;
                        puttstring2[2] = String.valueOf(putt2[2]);
                        textview31.setText(puttstring2[2]);
                        number2[2] = shot2[2] + putt2[2];
                        finalsum2[2] = String.valueOf(number2[2]);
                        textView32.setText(finalsum2[2]);
                        putshot[11] = putt2[2];
                    }
                });

                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2-=1;
                        putt2[2] -= 1;
                        puttstring2[2] = String.valueOf(putt2[2]);
                        textview31.setText(puttstring2[2]);
                        number2[2] = shot2[2] + putt2[2];
                        finalsum2[2] = String.valueOf(number2[2]);
                        textView32.setText(finalsum2[2]);
                        putshot[11] = putt2[2];
                    }
                });

                builder2.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog2 = builder2.create();

                dialog2.setCanceledOnTouchOutside(true);

                dialog2.show();
                break;

            case R.id.bu24:
                LayoutInflater inflater3 = getLayoutInflater();
                final View dialogView3 = inflater3.inflate(R.layout.score_usermake, null);

                button = (Button) dialogView3.findViewById(R.id.plus1);
                button1 = (Button)dialogView3.findViewById(R.id.minus1);
                button2 = (Button)dialogView3.findViewById(R.id.plus2);
                button3 = (Button)dialogView3.findViewById(R.id.minus2);

                final TextView textview40 = (TextView)dialogView3.findViewById(R.id.shottext);
                final TextView textview41 = (TextView)dialogView3.findViewById(R.id.putttext);
                final TextView textView42 = (TextView)dialogView3.findViewById(R.id.sum);

                final Button bu4 = (Button)findViewById(R.id.bu24);
                textview40.setText(shotstring2[3]);
                textview41.setText(puttstring2[3]);
                textView42.setText(finalsum2[3]);

                AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
                builder3.setView(dialogView3);
                builder3.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bu4.setText(finalsum2[3]);
                        textsum.setText(String.valueOf(text2));

                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2+=1;
                        shot2[3] += 1;
                        shotstring2[3] = String.valueOf(shot2[3]);
                        textview40.setText(shotstring2[3]);
                        number2[3] = shot2[3] + putt2[3];
                        finalsum2[3] = String.valueOf(number2[3]);
                        textView42.setText(finalsum2[3]);
                        shotput[12] = shot2[3];
                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2-=1;
                        shot2[3] -= 1;
                        shotstring2[3] = String.valueOf(shot2[3]);
                        textview40.setText(shotstring2[3]);
                        number2[3] = shot2[3] + putt2[3];
                        finalsum2[3] = String.valueOf(number2[3]);
                        textView42.setText(finalsum2[3]);
                        shotput[12] = shot2[3];
                    }
                });

                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2+=1;
                        putt2[3] += 1;
                        puttstring2[3] = String.valueOf(putt2[3]);
                        textview41.setText(puttstring2[3]);
                        number2[3] = shot2[3] + putt2[3];
                        finalsum2[3] = String.valueOf(number2[3]);
                        textView42.setText(finalsum2[3]);
                        putshot[12] = putt2[3];
                    }
                });

                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2-=1;
                        putt2[3] -= 1;
                        puttstring2[3] = String.valueOf(putt2[3]);
                        textview41.setText(puttstring2[3]);
                        number2[3] = shot2[3] + putt2[3];
                        finalsum2[3] = String.valueOf(number2[3]);
                        textView42.setText(finalsum2[3]);
                        putshot[12] = putt2[3];
                    }
                });


                builder3.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog3 = builder3.create();

                dialog3.setCanceledOnTouchOutside(true);

                dialog3.show();
                break;

            case R.id.bu25:
                LayoutInflater inflater4 = getLayoutInflater();
                final View dialogView4 = inflater4.inflate(R.layout.score_usermake, null);

                button = (Button) dialogView4.findViewById(R.id.plus1);
                button1 = (Button)dialogView4.findViewById(R.id.minus1);
                button2 = (Button)dialogView4.findViewById(R.id.plus2);
                button3 = (Button)dialogView4.findViewById(R.id.minus2);

                final TextView textview50 = (TextView)dialogView4.findViewById(R.id.shottext);
                final TextView textview51 = (TextView)dialogView4.findViewById(R.id.putttext);
                final TextView textView52 = (TextView)dialogView4.findViewById(R.id.sum);

                final Button bu5 = (Button)findViewById(R.id.bu25);
                textview50.setText(shotstring2[4]);
                textview51.setText(puttstring2[4]);
                textView52.setText(finalsum2[4]);

                AlertDialog.Builder builder4 = new AlertDialog.Builder(this);
                builder4.setView(dialogView4);
                builder4.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bu5.setText(finalsum2[4]);
                        textsum.setText(String.valueOf(text2));
                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2+=1;
                        shot2[4] += 1;
                        shotstring2[4] = String.valueOf(shot2[4]);
                        textview50.setText(shotstring2[4]);
                        number2[4] = shot2[4] + putt2[4];
                        finalsum2[4] = String.valueOf(number2[4]);
                        textView52.setText(finalsum2[4]);
                        shotput[13] = shot2[4];
                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2-=1;
                        shot2[4] -= 1;
                        shotstring2[4] = String.valueOf(shot2[4]);
                        textview50.setText(shotstring2[4]);
                        number2[4] = shot2[4] + putt2[4];
                        finalsum2[4] = String.valueOf(number2[4]);
                        textView52.setText(finalsum2[4]);
                        shotput[13] = shot2[4];
                    }
                });

                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2+=1;
                        putt2[4] += 1;
                        puttstring2[4] = String.valueOf(putt2[4]);
                        textview51.setText(puttstring2[4]);
                        number2[4] = shot2[4] + putt2[4];
                        finalsum2[4] = String.valueOf(number2[4]);
                        textView52.setText(finalsum2[4]);
                        putshot[13] = putt2[4];
                    }
                });

                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2-=1;
                        putt2[4] -= 1;
                        puttstring2[4] = String.valueOf(putt2[4]);
                        textview51.setText(puttstring2[4]);
                        number2[4] = shot2[4] + putt2[4];
                        finalsum2[4] = String.valueOf(number2[4]);
                        textView52.setText(finalsum2[4]);
                        putshot[13] = putt2[4];
                    }
                });

                builder4.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog4 = builder4.create();

                dialog4.setCanceledOnTouchOutside(true);

                dialog4.show();
                break;

            case R.id.bu26:
                LayoutInflater inflater5 = getLayoutInflater();
                final View dialogView5 = inflater5.inflate(R.layout.score_usermake, null);

                button = (Button) dialogView5.findViewById(R.id.plus1);
                button1 = (Button)dialogView5.findViewById(R.id.minus1);
                button2 = (Button)dialogView5.findViewById(R.id.plus2);
                button3 = (Button)dialogView5.findViewById(R.id.minus2);

                final TextView textview60 = (TextView)dialogView5.findViewById(R.id.shottext);
                final TextView textview61 = (TextView)dialogView5.findViewById(R.id.putttext);
                final TextView textView62 = (TextView)dialogView5.findViewById(R.id.sum);

                final Button bu6 = (Button)findViewById(R.id.bu26);
                textview60.setText(shotstring2[5]);
                textview61.setText(puttstring2[5]);
                textView62.setText(finalsum2[5]);

                AlertDialog.Builder builder5 = new AlertDialog.Builder(this);
                builder5.setView(dialogView5);
                builder5.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bu6.setText(finalsum2[5]);
                        textsum.setText(String.valueOf(text2));
                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2+=1;
                        shot2[5] += 1;
                        shotstring2[5] = String.valueOf(shot2[5]);
                        textview60.setText(shotstring2[5]);
                        number2[5] = shot2[5] + putt2[5];
                        finalsum2[5] = String.valueOf(number2[5]);
                        textView62.setText(finalsum2[5]);
                        shotput[14] = shot2[5];
                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2-=1;
                        shot2[5] -= 1;
                        shotstring2[5] = String.valueOf(shot2[5]);
                        textview60.setText(shotstring2[5]);
                        number2[5] = shot2[5] + putt2[5];
                        finalsum[5] = String.valueOf(number2[5]);
                        textView62.setText(finalsum2[5]);
                        shotput[14] = shot2[5];
                    }
                });

                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2+=1;
                        putt2[5] += 1;
                        puttstring2[5] = String.valueOf(putt2[5]);
                        textview61.setText(puttstring2[5]);
                        number2[5] = shot2[5] + putt2[5];
                        finalsum2[5] = String.valueOf(number2[5]);
                        textView62.setText(finalsum2[5]);
                        putshot[14] = putt2[5];
                    }
                });

                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2-=1;
                        putt2[5] -= 1;
                        puttstring2[5] = String.valueOf(putt2[5]);
                        textview61.setText(puttstring2[5]);
                        number2[5] = shot2[5] + putt2[5];
                        finalsum2[5] = String.valueOf(number2[5]);
                        textView62.setText(finalsum2[5]);
                        putshot[14] = putt2[5];
                    }
                });

                builder5.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog5 = builder5.create();

                dialog5.setCanceledOnTouchOutside(true);

                dialog5.show();
                break;


            case R.id.bu27:
                LayoutInflater inflater6 = getLayoutInflater();
                final View dialogView6 = inflater6.inflate(R.layout.score_usermake, null);

                button = (Button) dialogView6.findViewById(R.id.plus1);
                button1 = (Button)dialogView6.findViewById(R.id.minus1);
                button2 = (Button)dialogView6.findViewById(R.id.plus2);
                button3 = (Button)dialogView6.findViewById(R.id.minus2);

                final TextView textview70 = (TextView)dialogView6.findViewById(R.id.shottext);
                final TextView textview71 = (TextView)dialogView6.findViewById(R.id.putttext);
                final TextView textView72 = (TextView)dialogView6.findViewById(R.id.sum);

                final Button bu7 = (Button)findViewById(R.id.bu27);
                textview70.setText(shotstring[6]);
                textview71.setText(puttstring[6]);
                textView72.setText(finalsum[6]);

                AlertDialog.Builder builder6 = new AlertDialog.Builder(this);
                builder6.setView(dialogView6);
                builder6.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bu7.setText(finalsum[6]);
                        textsum.setText(String.valueOf(text));
                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2+=1;
                        shot2[6] += 1;
                        shotstring2[6] = String.valueOf(shot2[6]);
                        textview70.setText(shotstring2[6]);
                        number2[6] = shot2[6] + putt2[6];
                        finalsum2[6] = String.valueOf(number2[6]);
                        textView72.setText(finalsum2[6]);
                        shotput[15] = shot2[6];
                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2-=1;
                        shot2[6] -= 1;
                        shotstring2[6] = String.valueOf(shot2[6]);
                        textview70.setText(shotstring2[6]);
                        number2[6] = shot2[6] + putt2[6];
                        finalsum2[6] = String.valueOf(number2[6]);
                        textView72.setText(finalsum2[6]);
                        shotput[15] = shot2[6];
                    }
                });

                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2+=1;
                        putt2[6] += 1;
                        puttstring2[6] = String.valueOf(putt2[6]);
                        textview71.setText(puttstring2[6]);
                        number2[6] = shot2[6] + putt2[6];
                        finalsum2[6] = String.valueOf(number2[6]);
                        textView72.setText(finalsum2[6]);
                        putshot[15] = putt2[6];
                    }
                });

                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2-=1;
                        putt2[6] -= 1;
                        puttstring2[6] = String.valueOf(putt2[6]);
                        textview71.setText(puttstring2[6]);
                        number2[6] = shot2[6] + putt2[6];
                        finalsum2[6] = String.valueOf(number2[6]);
                        textView72.setText(finalsum2[6]);
                        putshot[15] = putt2[6];
                    }
                });

                builder6.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog6 = builder6.create();

                dialog6.setCanceledOnTouchOutside(true);

                dialog6.show();
                break;


            case R.id.bu28:
                LayoutInflater inflater7 = getLayoutInflater();
                final View dialogView7 = inflater7.inflate(R.layout.score_usermake, null);

                button = (Button) dialogView7.findViewById(R.id.plus1);
                button1 = (Button)dialogView7.findViewById(R.id.minus1);
                button2 = (Button)dialogView7.findViewById(R.id.plus2);
                button3 = (Button)dialogView7.findViewById(R.id.minus2);

                final TextView textview80 = (TextView)dialogView7.findViewById(R.id.shottext);
                final TextView textview81 = (TextView)dialogView7.findViewById(R.id.putttext);
                final TextView textView82 = (TextView)dialogView7.findViewById(R.id.sum);

                final Button bu8 = (Button)findViewById(R.id.bu28);
                textview80.setText(shotstring2[7]);
                textview81.setText(puttstring2[7]);
                textView82.setText(finalsum2[7]);

                AlertDialog.Builder builder7 = new AlertDialog.Builder(this);
                builder7.setView(dialogView7);
                builder7.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bu8.setText(finalsum2[7]);
                        textsum.setText(String.valueOf(text2));
                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2+=1;
                        shot2[7] += 1;
                        shotstring2[7] = String.valueOf(shot2[7]);
                        textview80.setText(shotstring2[7]);
                        number2[7] = shot2[7] + putt2[7];
                        finalsum2[7] = String.valueOf(number2[7]);
                        textView82.setText(finalsum2[7]);
                        shotput[16] = shot2[7];
                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2-=1;
                        shot2[7] -= 1;
                        shotstring2[7] = String.valueOf(shot2[7]);
                        textview80.setText(shotstring2[7]);
                        number2[7] = shot2[7] + putt2[7];
                        finalsum2[7] = String.valueOf(number2[7]);
                        textView82.setText(finalsum2[7]);
                        shotput[16] = shot2[7];
                    }
                });

                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2+=1;
                        putt2[7] += 1;
                        puttstring2[7] = String.valueOf(putt2[7]);
                        textview81.setText(puttstring2[7]);
                        number2[7] = shot2[7] + putt2[7];
                        finalsum2[7] = String.valueOf(number2[7]);
                        textView82.setText(finalsum2[7]);
                        putshot[16] = putt2[7];
                    }
                });

                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2-=1;
                        putt2[7] -= 1;
                        puttstring2[7] = String.valueOf(putt2[7]);
                        textview81.setText(puttstring2[7]);
                        number2[7] = shot2[7] + putt2[7];
                        finalsum2[7] = String.valueOf(number2[7]);
                        textView82.setText(finalsum2[7]);
                        putshot[16] = putt2[7];
                    }
                });

                builder7.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog7 = builder7.create();

                dialog7.setCanceledOnTouchOutside(true);

                dialog7.show();
                break;


            case R.id.bu29:
                LayoutInflater inflater8 = getLayoutInflater();
                final View dialogView8 = inflater8.inflate(R.layout.score_usermake, null);

                button = (Button) dialogView8.findViewById(R.id.plus1);
                button1 = (Button)dialogView8.findViewById(R.id.minus1);
                button2 = (Button)dialogView8.findViewById(R.id.plus2);
                button3 = (Button)dialogView8.findViewById(R.id.minus2);

                final TextView textview90 = (TextView)dialogView8.findViewById(R.id.shottext);
                final TextView textview91 = (TextView)dialogView8.findViewById(R.id.putttext);
                final TextView textView92 = (TextView)dialogView8.findViewById(R.id.sum);

                final Button bu9 = (Button)findViewById(R.id.bu29);
                textview90.setText(shotstring2[8]);
                textview91.setText(puttstring2[8]);
                textView92.setText(finalsum2[8]);

                AlertDialog.Builder builder8 = new AlertDialog.Builder(this);
                builder8.setView(dialogView8);
                builder8.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bu9.setText(finalsum2[8]);
                        textsum.setText(String.valueOf(text2));
                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2+=1;
                        shot2[8] += 1;
                        shotstring2[8] = String.valueOf(shot2[8]);
                        textview90.setText(shotstring2[8]);
                        number2[8] = shot2[8] + putt2[8];
                        finalsum2[8] = String.valueOf(number2[8]);
                        textView92.setText(finalsum2[8]);
                        shotput[17] = shot2[8];
                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2-=1;
                        shot2[8] -= 1;
                        shotstring2[8] = String.valueOf(shot2[8]);
                        textview90.setText(shotstring2[8]);
                        number2[8] = shot2[8] + putt2[8];
                        finalsum2[8] = String.valueOf(number2[8]);
                        textView92.setText(finalsum2[8]);
                        shotput[17] = shot2[8];
                    }
                });

                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2+=1;
                        text2+=1;
                        putt2[8] += 1;
                        puttstring2[8] = String.valueOf(putt2[8]);
                        textview91.setText(puttstring2[8]);
                        number2[8] = shot2[8] + putt2[8];
                        finalsum2[8] = String.valueOf(number2[8]);
                        textView92.setText(finalsum2[8]);
                        putshot[17] = putt2[8];
                    }
                });

                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2-=1;
                        putt2[8] -= 1;
                        puttstring2[8] = String.valueOf(putt2[8]);
                        textview91.setText(puttstring2[8]);
                        number2[8] = shot2[8] + putt2[8];
                        finalsum2[8] = String.valueOf(number2[8]);
                        textView92.setText(finalsum2[8]);
                        putshot[17] = putt2[8];
                    }
                });

                builder8.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog8 = builder8.create();

                dialog8.setCanceledOnTouchOutside(true);

                dialog8.show();
                break;

        }
    }




}
