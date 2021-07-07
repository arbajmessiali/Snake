package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {
    public static ImageView img_swipe;
    public static Dialog dialogScore2;
    private easyMode easyMode;
    public static TextView txt_score2, txt_best_score2, txt_dialog_score2, txt_dialog_best_score2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_WIDTH = dm.widthPixels;
        Constants.SCREEN_HEIGHT = dm.heightPixels;
        setContentView(R.layout.easy_mode);
        img_swipe = findViewById(R.id.img_swipe2);
        easyMode = findViewById(R.id.easy_mode);
        txt_score2 = findViewById(R.id.txt_score2);
        txt_best_score2 = findViewById(R.id.txt_best_score2);
        dialogScore2();
    }

    private void dialogScore2() {
        int bestScore2 = 0;
        SharedPreferences sp = this.getSharedPreferences("gamesetting2", Context.MODE_PRIVATE);
        if(sp!=null){
            bestScore2 = sp.getInt("bestscore2",0);
        }
        MainActivity2.txt_best_score2.setText(bestScore2+"");
        dialogScore2 = new Dialog(this);
        dialogScore2.setContentView(R.layout.dialog_start2);
        txt_dialog_score2 = dialogScore2.findViewById(R.id.txt_dialog_score2);
        txt_dialog_best_score2 = dialogScore2.findViewById(R.id.txt_dialog_best_score2);
        txt_dialog_best_score2.setText(bestScore2 + "");
        dialogScore2.setCanceledOnTouchOutside(false);
        RelativeLayout rl_start = dialogScore2.findViewById(R.id.rl_start);
        rl_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_swipe.setVisibility(View.VISIBLE);
                easyMode.reset();
                dialogScore2.dismiss();
            }
        });

        RelativeLayout rl_exit = dialogScore2.findViewById(R.id.rl_exit);
        rl_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogScore2.dismiss();
                openStartActivity();
                MainActivity2.this.finish();
            }
        });
        dialogScore2.show();
    }

    public void openStartActivity(){
        Intent intent = new Intent(this, StartMenu.class);
        startActivity(intent);
    }
}