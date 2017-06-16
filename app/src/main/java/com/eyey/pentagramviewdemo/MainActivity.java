package com.eyey.pentagramviewdemo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.eyey.pentagramview.PentagramAdapter;
import com.eyey.pentagramview.PentagramView;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    PentagramView view;
    View button;
    PentagramAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = (PentagramView) findViewById(R.id.penta);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.notifyDataSetChange();
            }
        });
        adapter = new PentagramAdapter() {
            @Override
            public List<String> getVertexText() {
                return Arrays.asList("1", "2", "3", "4", "5");
            }

            @Override
            public List<Float> getVertexScores(int position) {
                float a = (float) Math.random();
                float b = (float) Math.random();
                float c = (float) Math.random();
                float d = (float) Math.random();
                float e = (float) Math.random();
                return Arrays.asList(a, b, c, d, e);
            }

            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public int getViewColor(int position) {
                return Color.BLUE;
            }
        };
        view.setPentagramAdapter(adapter);
    }
}
