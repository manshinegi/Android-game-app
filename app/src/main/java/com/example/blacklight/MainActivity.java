package com.example.blacklight;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    LinearLayout topleft, topRight, bottomLeft, bottomRight;
    TextView scoreText;
    Handler mHandler;
    int score = 0;
    boolean mIsRunning;
    int previousBox = 0;
    int temp;
    AlertDialog.Builder builder;
    AlertDialog alert;
    DialogInterface dialogInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        topleft = (LinearLayout) findViewById(R.id.topLeft);
        topRight = (LinearLayout) findViewById(R.id.topRight);
        bottomLeft = (LinearLayout) findViewById(R.id.bottomLeft);
        bottomRight = (LinearLayout) findViewById(R.id.bottomRight);
        scoreText = (TextView) findViewById(R.id.scroreText);
        mIsRunning = true;
        score = 0;
        previousBox = 0;
        temp = 0;
        mHandler = new Handler();

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dialogInterface != null) {
            dialogInterface.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialogInterface != null) {
            dialogInterface.dismiss();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopRepeatingTask();
        if (dialogInterface != null) {
            dialogInterface.dismiss();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("boxno", "resumed called");
        startRepeatingTask();
    }


    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            temp++;
            try {
//                if ( isFirstTask== false) {
                if (temp > 1 && temp - score == 2) {

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Log.d("boxno", Thread.currentThread().getName() + "1111111");
                            showDialog();

                        }
                    });
                    return;
                    //  }
                }
                //  isFirstTask = false;
                selectRandomBox();
            } finally {
                if(mIsRunning)
                mHandler.postDelayed(mStatusChecker, 2000);
            }
        }
    };
    void startRepeatingTask() {
        mStatusChecker.run();
        mIsRunning = true;
        Log.d("boxno", "started");
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
        mIsRunning = false;
        Log.d("boxno","stopped");
    }


    private void showDialog() {
        stopRepeatingTask();
        builder = new AlertDialog.Builder(this);
        builder.setMessage("GAME OVER : Your score is " + score);

        builder.setPositiveButton("Resart", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialogInterface = dialog;
                if (dialog != null) {
                    dialog.dismiss();
                }
                finish();
                startActivity(getIntent());

            }
        });
        alert = builder.create();

        if (!((Activity) this).isFinishing()) {
            alert.show();
        }
    }

    private void selectRandomBox() {
        Random rand = new Random(); //instance of random class
        int min = 1;
        int max = 4;
        //generate random values from 0-24
        int boxNo = rand.nextInt(max - min + 1) + min;
        while (boxNo == previousBox) {
             boxNo = rand.nextInt(max - min + 1) + min;
        }
        if (previousBox != 0)
            undoGrey(previousBox);
        previousBox = boxNo;
        turnBoxGrey(boxNo);

    }

    @SuppressLint("ResourceAsColor")
    private void turnBoxGrey(final int boxNo) {
        Log.d("boxno", String.valueOf(boxNo));

        switch (boxNo) {

            case 1:
                topleft.setBackgroundResource(R.color.grey);
                break;
            case 2:
                topRight.setBackgroundResource(R.color.grey);
                break;
            case 3:
                bottomLeft.setBackgroundResource(R.color.grey);
                break;
            case 4:
                bottomRight.setBackgroundResource(R.color.grey);
                break;
        }
    }

    @SuppressLint("ResourceAsColor")
    private void undoGrey(int previousBox) {
        switch (previousBox) {
            case 1:
                topleft.setBackgroundResource(R.color.red);
                break;
            case 2:
                topRight.setBackgroundResource(R.color.blue);
                break;
            case 3:
                bottomLeft.setBackgroundResource(R.color.yellow);
                break;
            case 4:
                bottomRight.setBackgroundResource(R.color.green);
                break;
        }
    }



    @SuppressLint("ResourceAsColor")
    public void onClick(View view) {
        if (((ColorDrawable) view.getBackground()).getColor() == -7566460) {
            Log.d("boxno", Thread.currentThread().getName() + "222222");
            scoreText.setText("Score : " + (++score));
        }else{
            showDialog();
        }
    }


}