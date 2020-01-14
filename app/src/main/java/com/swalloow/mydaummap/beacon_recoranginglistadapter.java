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

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.perples.recosdk.RECOBeacon;

import java.util.ArrayList;
import java.util.Collection;

public class beacon_recoranginglistadapter extends BaseAdapter {
    private ArrayList<RECOBeacon> mRangedBeacons;
    private LayoutInflater mLayoutInflater;

    public beacon_recoranginglistadapter(Context context) {
        super();
        mRangedBeacons = new ArrayList<RECOBeacon>();
        mLayoutInflater = LayoutInflater.from(context);
    }



    public void updateBeacon(RECOBeacon beacon) {
        synchronized (mRangedBeacons) {
            if(mRangedBeacons.contains(beacon)) {
                mRangedBeacons.remove(beacon);
            }
            mRangedBeacons.add(beacon);
        }
    }

    public void updateAllBeacons(Collection<RECOBeacon> beacons) {
        synchronized (beacons) {
            mRangedBeacons = new ArrayList<RECOBeacon>(beacons);
        }
    }

    public void clear() {
        mRangedBeacons.clear();
    }

    @Override
    public int getCount() {
        return mRangedBeacons.size();
    }

    @Override
    public Object getItem(int position) {
        return mRangedBeacons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        RECOBeacon nearest_beacon = mRangedBeacons.get(0);
        for(int i= 1 ;i<mRangedBeacons.size();i++)
        {
            if(nearest_beacon.getAccuracy()>mRangedBeacons.get(i).getAccuracy())
            {
                nearest_beacon = mRangedBeacons.get(i);
            }
        }

        if(convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.beacon_item_ranging_beacon, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.recoProximityUuid = (TextView)convertView.findViewById(R.id.recoProximityUuid);
            viewHolder.recoMajor = (TextView)convertView.findViewById(R.id.recoMajor);
            viewHolder.recoMinor = (TextView)convertView.findViewById(R.id.recoMinor);
            viewHolder.recoProximity = (TextView)convertView.findViewById(R.id.recoProximity);
            viewHolder.recoAccuracy = (TextView)convertView.findViewById(R.id.recoAccuracy);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        RECOBeacon recoBeacon = mRangedBeacons.get(position);

        String proximityUuid = recoBeacon.getProximityUuid();

        viewHolder.recoProximityUuid.setText(String.format("%s-%s-%s-%s-%s", proximityUuid.substring(0, 8), proximityUuid.substring(8, 12), proximityUuid.substring(12, 16), proximityUuid.substring(16, 20), proximityUuid.substring(20) ));
        viewHolder.recoMajor.setText(nearest_beacon.getMajor() + "");
        viewHolder.recoMinor.setText(nearest_beacon.getMinor() + "");
        viewHolder.recoProximity.setText(nearest_beacon.getProximity() + "");
        viewHolder.recoAccuracy.setText(String.format("%.2f", nearest_beacon.getAccuracy()));

        advice.major = nearest_beacon.getMajor();
        advice.minor = nearest_beacon.getMinor();
        return convertView;
    }



    static class ViewHolder {
        TextView recoProximityUuid;
        TextView recoMajor;
        TextView recoMinor;
        TextView recoProximity;
        TextView recoAccuracy;
    }

}
