package com.swalloow.mydaummap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;


public class map extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    static final String API_KEY = "db3b73bbf771536aa9e317af7ec7770a";
    double latitude = 0.0, longitude; //내 위치
    //각 홀의 위치
    double []latitude_ap = {36.362316, 36.364751, 36.366282, 36.367843, 36.369148, 36.370555, 36.369148, 36.366199, 36.364360};
    double []longitude_ap = {127.297054, 127.299707, 127.297258, 127.298432, 127.299363, 127.296365, 127.295510, 127.293634, 127.296135};
    //홀 구분
    int choose = 10, padding = 100;
    MapView mapView;
    MapPOIItem marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        Spinner spinner = (Spinner)findViewById(R.id.spinner);

        //어댑터 생성
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.number, android.R.layout.simple_spinner_item);

        //스피너와 어댑터 연결
        spinner.setAdapter(adapter);

        //이벤트를 일으킨 위젯과 리스너와 연결
        spinner.setOnItemSelectedListener(this);

        // Using TedPermission library
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(map.this, "권한 허가", Toast.LENGTH_SHORT).show();

                // MapView 객체생성 및 API Key 설정
                mapView = new MapView(map.this);
                mapView.setDaumMapApiKey(API_KEY);

                RelativeLayout mapViewContainer = (RelativeLayout) findViewById(R.id.map_view);
                mapViewContainer.addView(mapView);

                // 마커 생성 및 설정
                marker = new MapPOIItem();
                marker.setItemName("Default Marker");
                marker.setTag(0);
//                marker.setMapPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude));
                marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
                marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
            }


            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(map.this, "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }

        };

        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("지도 서비스를 사용하기 위해서는 위치 접근 권한이 필요합니다.")
                .setDeniedMessage("\n[설정] > [권한] 에서 권한을 허용할 수 있어요.")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }

    @Override //상속받았을 때 메소드를 재정의하기 위해 사용함
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
        if(position != 0) {
            choose = position - 1;
            Toast.makeText(this, "홀" + position + "번이 선택되었습니다.", Toast.LENGTH_SHORT).show();
            getLocation();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent){

    }

    // 위치받아오기 함수
    public void getLocation() {
        // 위치정보를 관리 및 위치정보를 받아오는함수를 사용하기 위한 LocationManager 객체 생성
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // 위치정보를 받아오기 및 위치정보변경에 사용될 콜백메소드를 위한 리스너를 작성
        LocationListener locationListener = new LocationListener() {

            // 새로운 위치가 발견되면 호출된다.
            public void onLocationChanged(Location location) {

                double distance;
                TextView distance_result = (TextView)findViewById(R.id.distance_result);

                latitude = location.getLatitude();
                longitude = location.getLongitude();

                mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);
                marker.setMapPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude));

                // 경로 지정을 위한 준비
                MapPolyline polyline = new MapPolyline();
                polyline.setTag(1000);
                polyline.setLineColor(Color.argb(128, 255, 51, 0)); //Polyline 컬러 지정

                // Polyline 좌표 지정
                polyline.addPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude));

                if(choose == 0) {
                    mapView.removeAllPolylines();
                    polyline.addPoint(MapPoint.mapPointWithGeoCoord(latitude_ap[0], longitude_ap[0]));
                    //위도, 경도를 이용한 두 점 사이의 거리 구하는 공식
                    distance = Math.acos(Math.sin(Math.toRadians(latitude)) * Math.sin(Math.toRadians(latitude_ap[0])) + Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(latitude_ap[0])) * Math.cos(Math.abs(Math.toRadians(longitude) - Math.toRadians(longitude_ap[0])))) * 6371 * 1000;
                    distance_result.setText((int) distance + "meter");
                    mapView.addPOIItem(marker);
                    // Polyline 지도에 올리기
                    mapView.addPolyline(polyline);
                    // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정
                    MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
                    mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));
                    choose = 10;
                }
                else if(choose == 1) {
                    mapView.removeAllPolylines();
                    polyline.addPoint(MapPoint.mapPointWithGeoCoord(latitude_ap[1], longitude_ap[1]));
                    //위도, 경도를 이용한 두 점 사이의 거리 구하는 공식
                    distance = Math.acos(Math.sin(Math.toRadians(latitude)) * Math.sin(Math.toRadians(latitude_ap[1])) + Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(latitude_ap[1])) * Math.cos(Math.abs(Math.toRadians(longitude) - Math.toRadians(longitude_ap[1])))) * 6371 * 1000;
                    distance_result.setText((int) distance + "meter");
                    mapView.addPOIItem(marker);
                    // Polyline 지도에 올리기
                    mapView.addPolyline(polyline);
                    // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정
                    MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
                    mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));
                    choose = 10;
                }
                else if(choose == 2) {
                    mapView.removeAllPolylines();
                    polyline.addPoint(MapPoint.mapPointWithGeoCoord(latitude_ap[2], longitude_ap[2]));
                    //위도, 경도를 이용한 두 점 사이의 거리 구하는 공식
                    distance = Math.acos(Math.sin(Math.toRadians(latitude)) * Math.sin(Math.toRadians(latitude_ap[2])) + Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(latitude_ap[2])) * Math.cos(Math.abs(Math.toRadians(longitude) - Math.toRadians(longitude_ap[2])))) * 6371 * 1000;
                    distance_result.setText((int) distance + "meter");
                    mapView.addPOIItem(marker);
                    // Polyline 지도에 올리기
                    mapView.addPolyline(polyline);
                    // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정
                    MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
                    mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));
                    choose = 10;
                }
                else if(choose == 3) {
                    mapView.removeAllPolylines();
                    polyline.addPoint(MapPoint.mapPointWithGeoCoord(latitude_ap[3], longitude_ap[3]));
                    //위도, 경도를 이용한 두 점 사이의 거리 구하는 공식
                    distance = Math.acos(Math.sin(Math.toRadians(latitude)) * Math.sin(Math.toRadians(latitude_ap[3])) + Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(latitude_ap[3])) * Math.cos(Math.abs(Math.toRadians(longitude) - Math.toRadians(longitude_ap[3])))) * 6371 * 1000;
                    distance_result.setText((int) distance + "meter");
                    mapView.addPOIItem(marker);
                    // Polyline 지도에 올리기
                    mapView.addPolyline(polyline);
                    // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정
                    MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
                    mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));
                    choose = 10;
                }
                else if(choose == 4) {
                    mapView.removeAllPolylines();
                    polyline.addPoint(MapPoint.mapPointWithGeoCoord(latitude_ap[4], longitude_ap[4]));
                    //위도, 경도를 이용한 두 점 사이의 거리 구하는 공식
                    distance = Math.acos(Math.sin(Math.toRadians(latitude)) * Math.sin(Math.toRadians(latitude_ap[4])) + Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(latitude_ap[4])) * Math.cos(Math.abs(Math.toRadians(longitude) - Math.toRadians(longitude_ap[4])))) * 6371 * 1000;
                    distance_result.setText((int) distance + "meter");
                    mapView.addPOIItem(marker);
                    // Polyline 지도에 올리기
                    mapView.addPolyline(polyline);
                    // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정
                    MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
                    mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));
                    choose = 10;
                }
                else if(choose == 5) {
                    mapView.removeAllPolylines();
                    polyline.addPoint(MapPoint.mapPointWithGeoCoord(latitude_ap[5], longitude_ap[5]));
                    //위도, 경도를 이용한 두 점 사이의 거리 구하는 공식
                    distance = Math.acos(Math.sin(Math.toRadians(latitude)) * Math.sin(Math.toRadians(latitude_ap[5])) + Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(latitude_ap[5])) * Math.cos(Math.abs(Math.toRadians(longitude) - Math.toRadians(longitude_ap[5])))) * 6371 * 1000;
                    distance_result.setText((int) distance + "meter");
                    mapView.addPOIItem(marker);
                    // Polyline 지도에 올리기
                    mapView.addPolyline(polyline);
                    // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정
                    MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
                    mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));
                    choose = 10;
                }
                else if(choose == 6) {
                    mapView.removeAllPolylines();
                    polyline.addPoint(MapPoint.mapPointWithGeoCoord(latitude_ap[6], longitude_ap[6]));
                    //위도, 경도를 이용한 두 점 사이의 거리 구하는 공식
                    distance = Math.acos(Math.sin(Math.toRadians(latitude)) * Math.sin(Math.toRadians(latitude_ap[6])) + Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(latitude_ap[6])) * Math.cos(Math.abs(Math.toRadians(longitude) - Math.toRadians(longitude_ap[6])))) * 6371 * 1000;
                    distance_result.setText((int) distance + "meter");
                    mapView.addPOIItem(marker);
                    // Polyline 지도에 올리기
                    mapView.addPolyline(polyline);
                    // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정
                    MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
                    mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));
                    choose = 10;
                }
                else if(choose == 7) {
                    mapView.removeAllPolylines();
                    polyline.addPoint(MapPoint.mapPointWithGeoCoord(latitude_ap[7], longitude_ap[7]));
                    //위도, 경도를 이용한 두 점 사이의 거리 구하는 공식
                    distance = Math.acos(Math.sin(Math.toRadians(latitude)) * Math.sin(Math.toRadians(latitude_ap[7])) + Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(latitude_ap[7])) * Math.cos(Math.abs(Math.toRadians(longitude) - Math.toRadians(longitude_ap[7])))) * 6371 * 1000;
                    distance_result.setText((int) distance + "meter");
                    mapView.addPOIItem(marker);
                    // Polyline 지도에 올리기
                    mapView.addPolyline(polyline);
                    // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정
                    MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
                    mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));
                    choose = 10;
                }
                else if(choose == 8) {
                    mapView.removeAllPolylines();
                    polyline.addPoint(MapPoint.mapPointWithGeoCoord(latitude_ap[8], longitude_ap[8]));
                    //위도, 경도를 이용한 두 점 사이의 거리 구하는 공식
                    distance = Math.acos(Math.sin(Math.toRadians(latitude)) * Math.sin(Math.toRadians(latitude_ap[8])) + Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(latitude_ap[8])) * Math.cos(Math.abs(Math.toRadians(longitude) - Math.toRadians(longitude_ap[8])))) * 6371 * 1000;
                    distance_result.setText((int) distance + "meter");
                    mapView.addPOIItem(marker);
                    // Polyline 지도에 올리기
                    mapView.addPolyline(polyline);
                    // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정
                    MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
                    mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));
                    choose = 10;
                }
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