/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 Perples, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.swalloow.mydaummap;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.perples.recosdk.RECOBeacon;
import com.perples.recosdk.RECOBeaconRegion;
import com.perples.recosdk.RECOErrorCode;
import com.perples.recosdk.RECORangingListener;

import java.util.ArrayList;
import java.util.Collection;

/**
 * RECORangingActivity 클래스는 foreground 상태에서 ranging을 수행합니다.
 */
public class beacon_recoranging extends beacon_reco implements RECORangingListener {

    private com.swalloow.mydaummap.beacon_recoranginglistadapter mRangingListAdapter;
    private ListView mRegionListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beacon_reco_ranging);

        //mRecoManager 인스턴스는 여기서 생성됩니다. RECOActivity.onCreate() 메소드를 참고하세요.

        //RECORangingListener 를 설정합니다. (필수)
        mRecoManager.setRangingListener(this);

        /**
         * RECOServiceConnectListener와 함께 RECOBeaconManager를 bind 합니다. RECOServiceConnectListener는 RECOActivity에 구현되어 있습니다.
         * monitoring 및 ranging 기능을 사용하기 위해서는, 이 메소드가 "반드시" 호출되어야 합니다.
         * bind후에, onServiceConnect() 콜백 메소드가 호출됩니다. 콜백 메소드 호출 이후 monitoring / ranging 작업을 수행하시기 바랍니다.
         */
        mRecoManager.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mRangingListAdapter = new beacon_recoranginglistadapter(this);
        mRegionListView = (ListView)findViewById(R.id.list_ranging);
        mRegionListView.setAdapter(mRangingListAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.stop(mRegions);
        this.unbind();
    }

    private void unbind() {
        try {
            mRecoManager.unbind();
        } catch (RemoteException e) {
            Log.i("RECORangingActivity", "Remote Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceConnect() {
        Log.i("RECORangingActivity", "onServiceConnect()");
        mRecoManager.setDiscontinuousScan(com.swalloow.mydaummap.beacon_main.DISCONTINUOUS_SCAN);
        this.start(mRegions);
        //Write the code when RECOBeaconManager is bound to RECOBeaconService
        //RECOBeaconManager가 RECOBeaconService에 속해있을 때의 코드 작성
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<RECOBeacon> recoBeacons, RECOBeaconRegion recoRegion) {
        Log.i("RECORangingActivity", "didRangeBeaconsInRegion() region: " + recoRegion.getUniqueIdentifier() + ", number of beacons ranged: " + recoBeacons.size());
        mRangingListAdapter.updateAllBeacons(recoBeacons);
        mRangingListAdapter.notifyDataSetChanged();
        //Write the code when the beacons in the region is received
        //영역 내부의 비콘들이 수신될 때 코드 작성
    }

    @Override
    //RECOServiceConnectListener의 onServiceConnect() callback 메소드 내, 혹은 호출된 이후에 이 부분을 작성하셔야 합니다.
    //RemoteException, NullPointerException 발생 시, rangingBeaconsDidFailForRegion(), onServiceFail() callback 메소드를 통해 ErrorCode를 전달받습니다.
    protected void start(ArrayList<RECOBeaconRegion> regions) {

        /**
         * 일부 안드로이드 기기에서 BLE 장치들을 스캔할 때, 한 번만 스캔 후 스캔하지 않는 버그(참고: http://code.google.com/p/android/issues/detail?id=65863)가 있습니다.
         * 해당 버그를 SDK에서 해결하기 위해, RECOBeaconManager에 setDiscontinuousScan() 메소드를 이용할 수 있습니다.
         * 해당 메소드는 기기에서 BLE 장치들을 스캔할 때(즉, ranging 시에), 연속적으로 계속 스캔할 것인지, 불연속적으로 스캔할 것인지 설정하는 것입니다.
         * 기본 값은 FALSE로 설정되어 있으며, 특정 장치에 대해 TRUE로 설정하시길 권장합니다.
         *
         * mRecoManager.setDiscontinuousScan(true);
         */

        for(RECOBeaconRegion region : regions) {
            try {
                mRecoManager.startRangingBeaconsInRegion(region);
            } catch (RemoteException e) {
                Log.i("RECORangingActivity", "Remote Exception");
                e.printStackTrace();
            } catch (NullPointerException e) {
                Log.i("RECORangingActivity", "Null Pointer Exception");
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void stop(ArrayList<RECOBeaconRegion> regions) {
        for(RECOBeaconRegion region : regions) {
            try {
                mRecoManager.stopRangingBeaconsInRegion(region);
            } catch (RemoteException e) {
                Log.i("RECORangingActivity", "Remote Exception");
                e.printStackTrace();
            } catch (NullPointerException e) {
                Log.i("RECORangingActivity", "Null Pointer Exception");
                e.printStackTrace();
            }
        }
    }
    public void Onclick(View v)
    {
        Intent intent = new Intent(this,advice.class);
        startActivity(intent);
    }

    @Override
    public void onServiceFail(RECOErrorCode errorCode) {
        //Write the code when the RECOBeaconService is failed.
        //See the RECOErrorCode in the documents.
        //RECOBeaconService가 실패했을 때 코드 작성
        //문서 내의 RECOErrorCode를 봐라
        return;
    }

    @Override
    public void rangingBeaconsDidFailForRegion(RECOBeaconRegion region, RECOErrorCode errorCode) {
        //Write the code when the RECOBeaconService is failed to range beacons in the region.
        //See the RECOErrorCode in the documents.
        //RECOBeaconService가 지역 내에서 비콘들을 탐지하는 것이 실패했을 때 코드 작성
        //문서 내의 RECOErrorCode를 봐라
        return;
    }
}
