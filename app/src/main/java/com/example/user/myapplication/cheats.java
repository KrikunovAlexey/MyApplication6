package com.example.user.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by User on 10.10.2017.
 */

public class cheats extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cheats);
        String game = getIntent().getStringExtra("game");
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.setBackgroundResource(getResources().getIdentifier(game + "_back", "drawable", "com.example.user.myapplication"));
        TextView textView = (TextView) findViewById(R.id.textView);
        try {
            // открываем поток для чтения
            InputStream inputStream = getResources().openRawResource(getResources().getIdentifier(game, "raw", "com.example.user.myapplication"));
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "windows-1251"));
            String s = "";
            String str = "";
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                s += str + "\n";
            }
            textView.setText(s);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
