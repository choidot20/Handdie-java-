package com.swalloow.mydaummap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class weather extends AppCompatActivity {
    String weather_name,
            weather_temp,
            weather_pressure,
            weather_speed,
            weather_deg,
            weather_humidity,
            weather3_deg, weather3_temp, weather3_name, weather3_speed,
            weather6_deg, weather6_temp, weather6_name, weather6_speed,
            weather9_deg, weather9_temp, weather9_name, weather9_speed;

    float scaleX = 10.0f, scaleY = 10.0f;
    float scaleXX = 8.5f, scaleYY = 8.5f;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wearther);

        // 안드로이드 2.3.3 버전부터 사용해야 할 쓰레드의 정책과 관련한 StrictMode를 지정한다.
        // 규약에는 디스크 쓰기/읽기 작업, 네트워크 사용과 같이 메인 쓰레드에서 언제 끝날지 모르는 작업을 대기한다는 것은 문제가있다.
        // 즉, 상황에 따라 언제 끝날지 모르는 작업 등을 위해 무한정 대기 상태에 놓이지 않게 쓰레드 정책을 설정하는 것
        //StrictMode의 모든 규약을 허용한 후 적용
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        getLocation();

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    // 아이콘 다운로드를 위한 함수
    private Bitmap downloadImage(String strUrl) throws IOException {
        Bitmap bitmap = null;
        InputStream iStream = null;
        try {
            URL url = new URL(strUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            bitmap = BitmapFactory.decodeStream(iStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            iStream.close();
        }
        return bitmap;
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

    // 날씨정보 얻어오는 작업을 위한 환경을 제공하는 쓰레드함수
    private class GetWeatherTask extends AsyncTask<Double, Integer, Bitmap[]> {
        //LinkedList result = new LinkedList();
        Bitmap[] bitmap = new Bitmap[4];
        JSONObject main, weather, wind, cloud;
        JSONObject weather3;
        JSONObject weather6;
        JSONObject weather9;

        protected Bitmap[] doInBackground(Double... url) {

            try {
                Log.v("latti", ",url[0]: " + url[0].toString());
                Log.v("logi", ",url[1]: " + url[1].toString());

                JSONObject currentjObject = new JSONObject(downloadInfo("http://api.openweathermap.org/data/2.5/weather?lat=" + url[0].toString() + "&lon=" + url[1].toString() + "&units=metric&APPID=e6d5fc2ac344eab3a2d413a721378faa"));
                JSONObject futurejObject = new JSONObject(downloadInfo("http://api.openweathermap.org/data/2.5/forecast?lat=" + url[0].toString() + "&lon=" + url[1].toString() + "&units=metric&APPID=e6d5fc2ac344eab3a2d413a721378faa"));

                // 3시간뒤 날씨 받아오기
                weather3 = new JSONObject(futurejObject.getJSONArray("list").getString(0));
                weather3_deg = new JSONObject(weather3.getString("wind")).getString("deg");
                weather3_temp = new JSONObject(weather3.getString("main")).getString("temp");
                weather3_speed = new JSONObject(weather3.getString("wind")).getString("speed");
                weather3 = new JSONObject(weather3.getJSONArray("weather").getString(0));
                weather3_name = weather3.getString("main") + "(" + weather3.getString("description") + ")";

                //6시간뒤 날씨 받아오기
                weather6 = new JSONObject(futurejObject.getJSONArray("list").getString(1));
                weather6_deg = new JSONObject(weather6.getString("wind")).getString("deg");
                weather6_temp = new JSONObject(weather6.getString("main")).getString("temp");
                weather6_speed = new JSONObject(weather6.getString("wind")).getString("speed");
                weather6 = new JSONObject(weather6.getJSONArray("weather").getString(0));
                weather6_name = weather6.getString("main") + "(" + weather6.getString("description") + ")";

                //9시간뒤 날씨 받아오기
                weather9 = new JSONObject(futurejObject.getJSONArray("list").getString(2));
                weather9_deg = new JSONObject(weather9.getString("wind")).getString("deg");
                weather9_temp = new JSONObject(weather9.getString("main")).getString("temp");
                weather9_speed = new JSONObject(weather9.getString("wind")).getString("speed");
                weather9 = new JSONObject(weather9.getJSONArray("weather").getString(0));
                weather9_name = weather9.getString("main") + "(" + weather9.getString("description") + ")";

                // 현재날씨받아오기
                main = new JSONObject(currentjObject.getJSONArray("weather").getString(0));
                weather = currentjObject.getJSONObject("main");
                wind = currentjObject.getJSONObject("wind");
                cloud = currentjObject.getJSONObject("clouds");

                Log.v("현재날씨", " : " + main);
                weather_speed = wind.getString("speed");
                weather_deg = wind.getString("deg");
                weather_humidity = weather.getString("humidity");
                weather_pressure = weather.getString("pressure");
                weather_temp = weather.getString("temp");
                weather_name = main.getString("main") + "(" + main.getString("description") + ")";

                String[] iconUrl = new String[4];
                iconUrl[0] = new JSONObject(currentjObject.getJSONArray("weather").getString(0)).getString("icon");
                iconUrl[1] = weather3.getString("icon");
                iconUrl[2] = weather6.getString("icon");
                iconUrl[3] = weather9.getString("icon");

                // icon받아오는 작업
                // bitmap[0]-현재, bitmap[1]-3시간뒤, bitmap[2]-6시간뒤, bitmap[3]-9시간뒤
                for (int i = 0; i < 4; i++) {
                    bitmap[i] = downloadImage("http://openweathermap.org/img/w/" + iconUrl[i] + ".png");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        protected void onPostExecute(Bitmap[] result) {

            float temp1, check;
            int temp2;

            Matrix matrix = new Matrix();
            Matrix matrix2 = new Matrix();
            Matrix matrix3 = new Matrix();
            Matrix matrix4 = new Matrix();
            Matrix matrix5 = new Matrix();
            Bitmap bitmap2, bitmap3, bitmap4, bitmap5, rotateBitmap;

            bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.arrow);
            bitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.arrow1);
            bitmap4 = BitmapFactory.decodeResource(getResources(), R.drawable.arrow1);
            bitmap5 = BitmapFactory.decodeResource(getResources(), R.drawable.arrow1);

            matrix.setScale(scaleX, scaleY);

            ImageView[] result_imageview = new ImageView[3];
            TextView[] future_windtext = new TextView[3];
            TextView[] future_temptext = new TextView[3];

            TextView w_speed = (TextView) findViewById(R.id.wind_result);
            TextView w_direction = (TextView) findViewById(R.id.wd_result);
            TextView w_temperature = (TextView) findViewById(R.id.tp_result);
            ImageView w_icon = (ImageView) findViewById(R.id.weather1);
            ImageView w_icon_arrow = (ImageView) findViewById(R.id.arrow);
            ImageView w_icon_arrow3 = (ImageView) findViewById(R.id.arrow1);
            ImageView w_icon_arrow6 = (ImageView) findViewById(R.id.arrow2);
            ImageView w_icon_arrow9 = (ImageView) findViewById(R.id.arrow3);

            w_icon.setScaleX(scaleX);
            w_icon.setScaleY(scaleY);

            temp1 = Float.parseFloat(weather_speed);
            temp2 = (int) temp1;
            weather_speed = Integer.toString(temp2);

            temp1 = Float.parseFloat(weather_temp);
            temp2 = (int) temp1;
            weather_temp = Integer.toString(temp2);

            w_icon.setImageBitmap(result[0]);
            w_speed.setText(weather_speed + "m/s");
            w_temperature.setText(weather_temp + "℃");
            w_direction.setText("서쪽");

            //첫 화살표 위치 조건
            check = Float.parseFloat(weather_deg);
            if ((270 <= check) && (check < 315)) {
                matrix2.postRotate(45);
                rotateBitmap = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix2, true);
                bitmap2 = rotateBitmap;
                w_icon_arrow.setImageBitmap(bitmap2);
                w_icon_arrow.setScaleType(ImageView.ScaleType.FIT_CENTER);
                w_direction.setText("남서쪽");
            } else if ((0 <= check) && (check < 45)) {
                matrix2.postRotate(90);
                rotateBitmap = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix2, true);
                bitmap2 = rotateBitmap;
                w_icon_arrow.setImageBitmap(bitmap2);
                w_icon_arrow.setScaleType(ImageView.ScaleType.FIT_CENTER);
                w_direction.setText("북쪽");
            } else if ((45 <= check) && (check < 90)) {
                matrix2.postRotate(135);
                rotateBitmap = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix2, true);
                bitmap2 = rotateBitmap;
                w_icon_arrow.setImageBitmap(bitmap2);
                w_icon_arrow.setScaleType(ImageView.ScaleType.FIT_CENTER);
                w_direction.setText("북동쪽");
            } else if ((90 <= check) && (check < 135)) {
                matrix2.postRotate(180);
                rotateBitmap = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix2, true);
                bitmap2 = rotateBitmap;
                w_icon_arrow.setImageBitmap(bitmap2);
                w_icon_arrow.setScaleType(ImageView.ScaleType.FIT_CENTER);
                w_direction.setText("동쪽");
            } else if ((135 <= check) && (check < 180)) {
                matrix2.postRotate(225);
                rotateBitmap = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix2, true);
                bitmap2 = rotateBitmap;
                w_icon_arrow.setImageBitmap(bitmap2);
                w_icon_arrow.setScaleType(ImageView.ScaleType.FIT_CENTER);
                w_direction.setText("남동쪽");
            } else if ((180 <= check) && (check < 225)) {
                matrix2.postRotate(270);
                rotateBitmap = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix2, true);
                bitmap2 = rotateBitmap;
                w_icon_arrow.setImageBitmap(bitmap2);
                w_icon_arrow.setScaleType(ImageView.ScaleType.FIT_CENTER);
                w_direction.setText("남쪽");
            } else if ((225 <= check) && (check < 270)) {
                matrix2.postRotate(315);
                rotateBitmap = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix2, true);
                bitmap2 = rotateBitmap;
                w_icon_arrow.setImageBitmap(bitmap2);
                w_icon_arrow.setScaleType(ImageView.ScaleType.FIT_CENTER);
                w_direction.setText("남서쪽");
            }

            //3시간 후 화살표 위치 조건
            check = Float.parseFloat(weather3_deg);
            if ((270 <= check) && (check < 315)) {
                matrix3.postRotate(45);
                rotateBitmap = Bitmap.createBitmap(bitmap3, 0, 0, bitmap3.getWidth(), bitmap3.getHeight(), matrix3, true);
                bitmap3 = rotateBitmap;
                w_icon_arrow3.setImageBitmap(bitmap3);
                w_icon_arrow3.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else if ((0 < check) && (check < 45)) {
                matrix3.postRotate(90);
                rotateBitmap = Bitmap.createBitmap(bitmap3, 0, 0, bitmap3.getWidth(), bitmap3.getHeight(), matrix3, true);
                bitmap3 = rotateBitmap;
                w_icon_arrow3.setImageBitmap(bitmap3);
                w_icon_arrow3.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else if ((45 <= check) && (check < 90)) {
                matrix3.postRotate(135);
                rotateBitmap = Bitmap.createBitmap(bitmap3, 0, 0, bitmap3.getWidth(), bitmap3.getHeight(), matrix3, true);
                bitmap3 = rotateBitmap;
                w_icon_arrow3.setImageBitmap(bitmap3);
                w_icon_arrow3.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else if ((90 <= check) && (check < 135)) {
                matrix3.postRotate(180);
                rotateBitmap = Bitmap.createBitmap(bitmap3, 0, 0, bitmap3.getWidth(), bitmap3.getHeight(), matrix3, true);
                bitmap3 = rotateBitmap;
                w_icon_arrow3.setImageBitmap(bitmap3);
                w_icon_arrow3.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else if ((135 <= check) && (check < 180)) {
                matrix3.postRotate(225);
                rotateBitmap = Bitmap.createBitmap(bitmap3, 0, 0, bitmap3.getWidth(), bitmap3.getHeight(), matrix3, true);
                bitmap3 = rotateBitmap;
                w_icon_arrow3.setImageBitmap(bitmap3);
                w_icon_arrow3.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else if ((180 <= check) && (check < 225)) {
                matrix3.postRotate(270);
                rotateBitmap = Bitmap.createBitmap(bitmap3, 0, 0, bitmap3.getWidth(), bitmap3.getHeight(), matrix3, true);
                bitmap3 = rotateBitmap;
                w_icon_arrow3.setImageBitmap(bitmap3);
                w_icon_arrow3.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else if ((225 <= check) && (check < 270)) {
                matrix3.postRotate(315);
                rotateBitmap = Bitmap.createBitmap(bitmap3, 0, 0, bitmap3.getWidth(), bitmap3.getHeight(), matrix3, true);
                bitmap3 = rotateBitmap;
                w_icon_arrow3.setImageBitmap(bitmap3);
                w_icon_arrow3.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }

            //6시간 후 화살표 위치 조건
            check = Float.parseFloat(weather6_deg);
            if ((270 <= check) && (check < 315)) {
                matrix4.postRotate(45);
                rotateBitmap = Bitmap.createBitmap(bitmap4, 0, 0, bitmap4.getWidth(), bitmap4.getHeight(), matrix4, true);
                bitmap4 = rotateBitmap;
                w_icon_arrow6.setImageBitmap(bitmap4);
                w_icon_arrow6.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else if ((0 < check) && (check < 45)) {
                matrix4.postRotate(90);
                rotateBitmap = Bitmap.createBitmap(bitmap4, 0, 0, bitmap4.getWidth(), bitmap4.getHeight(), matrix4, true);
                bitmap4 = rotateBitmap;
                w_icon_arrow6.setImageBitmap(bitmap4);
                w_icon_arrow6.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else if ((45 <= check) && (check < 90)) {
                matrix4.postRotate(135);
                rotateBitmap = Bitmap.createBitmap(bitmap4, 0, 0, bitmap4.getWidth(), bitmap4.getHeight(), matrix4, true);
                bitmap4 = rotateBitmap;
                w_icon_arrow6.setImageBitmap(bitmap4);
                w_icon_arrow6.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else if ((90 <= check) && (check < 135)) {
                matrix4.postRotate(180);
                rotateBitmap = Bitmap.createBitmap(bitmap4, 0, 0, bitmap4.getWidth(), bitmap4.getHeight(), matrix4, true);
                bitmap4 = rotateBitmap;
                w_icon_arrow6.setImageBitmap(bitmap4);
                w_icon_arrow6.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else if ((135 <= check) && (check < 180)) {
                matrix4.postRotate(225);
                rotateBitmap = Bitmap.createBitmap(bitmap4, 0, 0, bitmap4.getWidth(), bitmap4.getHeight(), matrix4, true);
                bitmap4 = rotateBitmap;
                w_icon_arrow6.setImageBitmap(bitmap4);
                w_icon_arrow6.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else if ((180 <= check) && (check < 225)) {
                matrix4.postRotate(270);
                rotateBitmap = Bitmap.createBitmap(bitmap4, 0, 0, bitmap4.getWidth(), bitmap4.getHeight(), matrix4, true);
                bitmap4 = rotateBitmap;
                w_icon_arrow6.setImageBitmap(bitmap4);
                w_icon_arrow6.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else if ((225 <= check) && (check < 270)) {
                matrix4.postRotate(315);
                rotateBitmap = Bitmap.createBitmap(bitmap4, 0, 0, bitmap4.getWidth(), bitmap4.getHeight(), matrix4, true);
                bitmap4 = rotateBitmap;
                w_icon_arrow6.setImageBitmap(bitmap4);
                w_icon_arrow6.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }

            //9시간 후 화살표 위치 조건
            check = Float.parseFloat(weather9_deg);
            if ((270 <= check) && (check < 315)) {
                matrix5.postRotate(45);
                rotateBitmap = Bitmap.createBitmap(bitmap5, 0, 0, bitmap5.getWidth(), bitmap5.getHeight(), matrix5, true);
                bitmap5 = rotateBitmap;
                w_icon_arrow9.setImageBitmap(bitmap5);
                w_icon_arrow9.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else if ((0 < check) && (check < 45)) {
                matrix5.postRotate(90);
                rotateBitmap = Bitmap.createBitmap(bitmap5, 0, 0, bitmap5.getWidth(), bitmap5.getHeight(), matrix5, true);
                bitmap5 = rotateBitmap;
                w_icon_arrow9.setImageBitmap(bitmap5);
                w_icon_arrow9.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else if ((45 <= check) && (check < 90)) {
                matrix5.postRotate(135);
                rotateBitmap = Bitmap.createBitmap(bitmap5, 0, 0, bitmap5.getWidth(), bitmap5.getHeight(), matrix5, true);
                bitmap5 = rotateBitmap;
                w_icon_arrow9.setImageBitmap(bitmap5);
                w_icon_arrow9.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else if ((90 <= check) && (check < 135)) {
                matrix5.postRotate(180);
                rotateBitmap = Bitmap.createBitmap(bitmap5, 0, 0, bitmap5.getWidth(), bitmap5.getHeight(), matrix5, true);
                bitmap5 = rotateBitmap;
                w_icon_arrow9.setImageBitmap(bitmap5);
                w_icon_arrow9.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else if ((135 <= check) && (check < 180)) {
                matrix5.postRotate(225);
                rotateBitmap = Bitmap.createBitmap(bitmap5, 0, 0, bitmap5.getWidth(), bitmap5.getHeight(), matrix5, true);
                bitmap5 = rotateBitmap;
                w_icon_arrow9.setImageBitmap(bitmap5);
                w_icon_arrow9.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else if ((180 <= check) && (check < 225)) {
                matrix5.postRotate(270);
                rotateBitmap = Bitmap.createBitmap(bitmap5, 0, 0, bitmap5.getWidth(), bitmap5.getHeight(), matrix5, true);
                bitmap5 = rotateBitmap;
                w_icon_arrow9.setImageBitmap(bitmap5);
                w_icon_arrow9.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else if ((225 <= check) && (check < 270)) {
                matrix5.postRotate(315);
                rotateBitmap = Bitmap.createBitmap(bitmap5, 0, 0, bitmap5.getWidth(), bitmap5.getHeight(), matrix5, true);
                bitmap5 = rotateBitmap;
                w_icon_arrow9.setImageBitmap(bitmap5);
                w_icon_arrow9.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }

            result_imageview[0] = (ImageView) findViewById(R.id.wt1);
            result_imageview[1] = (ImageView) findViewById(R.id.wt2);
            result_imageview[2] = (ImageView) findViewById(R.id.wt3);

            future_windtext[0] = (TextView) findViewById(R.id.word1_result);
            future_windtext[1] = (TextView) findViewById(R.id.word2_result);
            future_windtext[2] = (TextView) findViewById(R.id.word3_result);

            future_temptext[0] = (TextView) findViewById(R.id.tp1_result);
            future_temptext[1] = (TextView) findViewById(R.id.tp2_result);
            future_temptext[2] = (TextView) findViewById(R.id.tp3_result);

            for (int i = 0; i < 3; i++) {
                result_imageview[i].setScaleX(scaleXX);
                result_imageview[i].setScaleY(scaleYY);
                result_imageview[i].setImageBitmap(result[i + 1]);
            }

            temp1 = Float.parseFloat(weather3_temp);
            temp2 = (int) temp1;
            weather3_temp = Integer.toString(temp2);

            temp1 = Float.parseFloat(weather6_temp);
            temp2 = (int) temp1;
            weather6_temp = Integer.toString(temp2);

            temp1 = Float.parseFloat(weather9_temp);
            temp2 = (int) temp1;
            weather9_temp = Integer.toString(temp2);

            future_temptext[0].setText(weather3_temp + "℃");
            future_temptext[1].setText(weather6_temp + "℃");
            future_temptext[2].setText(weather9_temp + "℃");

            temp1 = Float.parseFloat(weather3_speed);
            temp2 = (int) temp1;
            weather3_speed = Integer.toString(temp2);

            temp1 = Float.parseFloat(weather6_speed);
            temp2 = (int) temp1;
            weather6_speed = Integer.toString(temp2);

            temp1 = Float.parseFloat(weather9_speed);
            temp2 = (int) temp1;
            weather9_speed = Integer.toString(temp2);

            future_windtext[0].setText(weather3_speed + "m/s");
            future_windtext[1].setText(weather6_speed + "m/s");
            future_windtext[2].setText(weather9_speed + "m/s");
        }
    }



    // 위치받아오기 함수
    public void getLocation() {
        // 위치정보를 관리 및 위치정보를 받아오는함수를 사용하기 위한 LocationManager 객체 생성
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // 위치정보를 받아오기 및 위치정보변경에 사용될 콜백메소드를 위한 리스너를 작성
        LocationListener locationListener = new LocationListener() {

            // 새로운 위치가 발견되면 호출된다.
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                GetWeatherTask getWeatherTask = new GetWeatherTask();
                getWeatherTask.execute((Double) latitude, (Double) longitude);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            public void onProviderEnabled(String provider) {

            }

            public void onProviderDisabled(String provider) {

            }
        };

        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 10, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}