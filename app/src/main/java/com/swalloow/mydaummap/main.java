package com.swalloow.mydaummap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class main extends AppCompatActivity {
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void Onclickreco(View v)
    {
        intent = new Intent(main.this,beacon_recoranging.class);
        startActivity(intent);
    }

    public void Onclickweather(View v)
    {
        intent = new Intent(main.this,weather.class);
        startActivity(intent);
    }

    public void Onclickmap(View v)
    {
        intent = new Intent(main.this,map.class);
        startActivity(intent);
    }

    public void Onclickscore(View v)
    {
        intent = new Intent(main.this,score_main.class);
        startActivity(intent);
    }
}
