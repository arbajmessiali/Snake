package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class StartMenu extends AppCompatActivity {
    private Button button, button1, exit_button, button_left, button_right;
    private TextView text1, text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_menu);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        button = (Button) findViewById(R.id.startbtn);
        button1 = (Button) findViewById(R.id.startbtn2);
        button_left = (Button) findViewById(R.id.left_button);
        button_right = (Button) findViewById(R.id.right_button);
        exit_button = (Button) findViewById(R.id.exitbtn);
        text1 = (TextView) findViewById(R.id.textView3);
        text2 = (TextView) findViewById(R.id.textView4);

        button1.setVisibility(View.INVISIBLE);
        button_left.setVisibility(View.INVISIBLE);
        text1.setVisibility(View.INVISIBLE);
        text2.setVisibility(View.INVISIBLE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
                StartMenu.this.finish();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEasyMode();
                StartMenu.this.finish();
            }
        });

        button_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setVisibility(View.INVISIBLE);
                button1.setVisibility(View.VISIBLE);
                button_left.setVisibility(View.VISIBLE);
                button_right.setVisibility(View.INVISIBLE);
                text1.setVisibility(View.VISIBLE);
                text2.setVisibility(View.VISIBLE);
            }
        });

        button_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button1.setVisibility(View.INVISIBLE);
                button.setVisibility(View.VISIBLE);
                button_right.setVisibility(View.VISIBLE);
                button_left.setVisibility(View.INVISIBLE);
            }
        });

        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openEasyMode(){
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do You Want To Exit ? ")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StartMenu.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}