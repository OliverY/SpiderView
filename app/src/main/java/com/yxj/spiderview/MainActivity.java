package com.yxj.spiderview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;

import com.yxj.yspiderview.SpiderView;

public class MainActivity extends AppCompatActivity {

    private SpiderView mSpiderView;
    private SeekBar mAttr1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSpiderView = (SpiderView) findViewById(R.id.spider_view);
        mAttr1 = (SeekBar) findViewById(R.id.attr1);

        mAttr1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSpiderView.setSingleData(new SpiderView.Data("敏捷",(progress*mSpiderView.maxDegree)/100f));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
