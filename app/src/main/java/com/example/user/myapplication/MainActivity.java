package com.example.user.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    MainActivity mainActivity = this;
    String LOG_TAG = "myLogs";
    HashMap<String, String> filename = new HashMap<>();
    ListView listView = null;
    EditText editText = null;
    InputMethodManager imm = null;
    ArrayList<HashMap<String, Object>> arrayList = null;
    ArrayList<HashMap<String, Object>> filterArrayList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().hide();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        listView = (ListView) findViewById(R.id.listView);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        editText = (EditText) findViewById(R.id.editText);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        arrayList = new ArrayList<>();

        editText.clearFocus();
        HashMap<String, Object> map;
        try {
            // открываем поток для чтения
            InputStream inputStream = getResources().openRawResource(R.raw.cheats);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "windows-1251"));
            String str = "", str2 = "";
            while ((str = br.readLine()) != null) {
                if ((str2 = br.readLine()) != null) {
                    map = new HashMap<>();
                    map.put("Image", getResources().getIdentifier(str2, "drawable", "com.example.user.myapplication"));
                    map.put("Text", str);
                    arrayList.add(map);
                    filename.put(str, str2);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 1; i < arrayList.size(); i++) {
            HashMap<String, Object> name = arrayList.get(i);
            int j;
            for (j = i - 1; j >= 0 && ((String) arrayList.get(j).get("Text")).compareTo((String) name.get("Text")) > 0; j--) {
                arrayList.set(j + 1, arrayList.get(j));
            }
            arrayList.set(j + 1, name);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, arrayList,
                R.layout.list_item,
                new String[]{"Image", "Text"},
                new int[]{R.id.icon, R.id.text});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.requestFocus();
                imm.toggleSoftInput(0, 0);
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filterArrayList = new ArrayList<>();
                for (int i = 0; i < arrayList.size(); i++) {
                    if (Pattern.compile(s.toString().toLowerCase()).matcher(((String) arrayList.get(i).get("Text")).toLowerCase()).find()) {
                        filterArrayList.add(arrayList.get(i));
                    }
                }

                SimpleAdapter adapter = new SimpleAdapter(mainActivity, filterArrayList,
                        R.layout.list_item,
                        new String[]{"Image", "Text"},
                        new int[]{R.id.icon, R.id.text});
                listView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String name = ((TextView) view.findViewById(R.id.text)).getText().toString();
        Log.d(LOG_TAG, name);
        Intent intent = new Intent(this, cheats.class);
        intent.putExtra("game", filename.get(name));
        startActivity(intent);
    }
}
