package com.swalloow.mydaummap;

import android.app.Activity;
import android.os.Bundle;

import com.perples.recosdk.RECOBeaconManager;
import com.perples.recosdk.RECOBeaconRegion;
import com.perples.recosdk.RECOServiceConnectListener;

import java.util.ArrayList;

/**
 * RECOActivity 클래스는 RECOMonitoringActivity와 RECORangingActivity를 위한 기본 클래스 입니다.
 * Monitoring 이나 ranging을 단일 클래스로 구성하고 싶으시다면, 이 클래스를 삭제하시고 필요한 메소드와 RECOServiceConnectListener를 해당 클래스에서 구현하시기 바랍니다.
 */
public abstract class beacon_reco extends Activity implements RECOServiceConnectListener { //서비스 연결을 위해 RECOServiceConnectListener 인터페이스가 필요
    protected RECOBeaconManager mRecoManager; //RECOBeaconManager 인스턴스를 통해 region monitoring, region ranging을 수행
    protected ArrayList<RECOBeaconRegion> mRegions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * RECOBeaconManager 인스턴스틀 생성합니다. (스캔 대상 및 백그라운드 ranging timeout 설정)
         * RECO만을 스캔하고, 백그라운드 ranging timeout을 설정하고 싶지 않으시다면, 다음과 같이 생성하시기 바랍니다.
         * 		mRecoManager = RECOBeaconManager.getInstance(getApplicationContext(), true, false);
         * 주의: enableRangingTimeout을 false로 설정 시, 배터리 소모량이 증가합니다.
         */
        //RECOBeaconManager.getInstance(Context, boolean, boolean)의 경우,
        //Context, RECO 비콘만을 대상으로 동작 여부를 설정하는 값, 그리고 백그라운드 monitoring 중 ranging 시 timeout을 설정하는 값을 매개변수로 받습니다.
        //RECOBeaconManager.getInstance(Context) 메소드로 RECOBeaconManager의 인스턴스를 생성하실 경우,
        //기본 값으로 RECO 비콘에 대해서만 동작하고, 백그라운드 monitoring 중 ranging 시 timeout이 설정됩니다.
        //또한, RECOBeaconManager.getInstance(Context, boolean) 메소드로 RECOBeaconManager의 인스턴스를 생성하실 경우, 기본 값으로 RECO 비콘에 대해서만 동작합니다.
        mRecoManager = RECOBeaconManager.getInstance(getApplicationContext(), com.swalloow.mydaummap.beacon_main.SCAN_RECO_ONLY, com.swalloow.mydaummap.beacon_main.ENABLE_BACKGROUND_RANGING_TIMEOUT);
        mRegions = this.generateBeaconRegion();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private ArrayList<RECOBeaconRegion> generateBeaconRegion() {
        ArrayList<RECOBeaconRegion> regions = new ArrayList<RECOBeaconRegion>();        //비콘은 여러개 들어올 수 있기에 ArrayList로 만들어줌

        RECOBeaconRegion recoRegion;
        recoRegion = new RECOBeaconRegion(com.swalloow.mydaummap.beacon_main.RECO_UUID, "RECO Sample Region");
        regions.add(recoRegion);        //ArrayList에 값 추가

        return regions;
    }

    protected abstract void start(ArrayList<RECOBeaconRegion> regions);
    protected abstract void stop(ArrayList<RECOBeaconRegion> regions);
}
