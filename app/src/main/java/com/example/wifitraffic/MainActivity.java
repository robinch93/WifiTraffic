package com.example.wifitraffic;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public double greenLimit = 0;
    public double yellowLimit = 0;
    public double redLimit = 0;
    public int channelNo = 1;
    public int load = 0;
    public int max1 = 20;
    public int max2 = 25;
    Thread t1, t2;
    private Button red;
    private Button yellow;
    private Button green;
    private View view;
    private int currentBackgroundColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        red = findViewById(R.id.red_button);
        yellow = findViewById(R.id.yellow_button);
        green = findViewById(R.id.green_button);
        view = findViewById(R.id.activity);

        green.setOnClickListener(this);
        yellow.setOnClickListener(this);
        red.setOnClickListener(this);

        final TextView textView2 = (TextView) findViewById(R.id.textView2);
        final TextView textView4 = (TextView) findViewById(R.id.textView4);
        final TextView textView6 = (TextView) findViewById(R.id.textView6);

        // dividing the max capacity of channel in 2 : 1 : 1 ratio for green, yellow and red colors resp.

        t1 = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                load++;
                                textView4.setText(String.valueOf(max1));
                                textView6.setText(String.valueOf(load));
                                textView2.setText(String.valueOf(channelNo));
                                if (load >= max1) {
                                    channelNo++;
                                    load = 0;
                                } else if (load > 0 && load < greenLimit) {
                                    green.callOnClick();
                                } else if (load > greenLimit && load < yellowLimit) {
                                    yellow.callOnClick();
                                } else if (load > yellowLimit && load < redLimit) {
                                    red.callOnClick();
                                }

                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };


        t2 = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(3000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                load++;

                                textView4.setText(String.valueOf(max2));
                                textView6.setText(String.valueOf(load));
                                textView2.setText(String.valueOf(channelNo));
                                if (load == 4) {
                                    load = load + 6;
                                }
                                if (load >= max2) {
                                    channelNo++;
                                    load = 0;
                                } else if (load > 0 && load < greenLimit) {
                                    green.callOnClick();
                                } else if (load > greenLimit && load < yellowLimit) {
                                    yellow.callOnClick();
                                } else if (load > yellowLimit && load < redLimit) {
                                    red.callOnClick();
                                }

                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        // t.start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        currentBackgroundColor = Color.TRANSPARENT;
        Drawable background = view.getBackground();
        if (background instanceof ColorDrawable)
            currentBackgroundColor = ((ColorDrawable) background).getColor();

        outState.putInt("background_color", currentBackgroundColor);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey("background_color")) {
            view.setBackgroundColor(savedInstanceState.getInt("background_color"));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.red_button:
                view.setBackgroundColor(Color.RED);
                break;
            case R.id.yellow_button:
                view.setBackgroundColor(Color.YELLOW);
                break;
            case R.id.green_button:
                view.setBackgroundColor(Color.GREEN);
                break;
        }
    }

    public void calc1() {
        greenLimit = Math.ceil(max1 / 2);
        yellowLimit = redLimit = Math.ceil(max1 / 4);
        yellowLimit = greenLimit + yellowLimit;
        redLimit = greenLimit + yellowLimit + redLimit;
    }

    public void calc2() {
        greenLimit = Math.ceil(max2 / 2);
        yellowLimit = redLimit = Math.ceil(max2 / 4);
        yellowLimit = greenLimit + yellowLimit;
        redLimit = greenLimit + yellowLimit + redLimit;
    }

    public void StartBtnClick(View view) {
        t1.start();
        calc1();
    }

    public void StopBtnClick(View view) {
        t1.interrupt();
        t2.interrupt();
    }

    public void SwitchBtnClick(View view) {
        calc2();
        t1.interrupt();
        channelNo++;
        load = 0;
        t2.start();
    }
}
