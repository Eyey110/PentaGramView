package com.eyey.pentagramviewdemo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.eyey.pentagramview.PentagramAdapter;
import com.eyey.pentagramview.PentagramView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    PentagramView view;
    View button;
    View button2;
    MyPentaAdapter adapter;
    int[] color = new int[]{Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = (PentagramView) findViewById(R.id.penta);
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float a = (float) Math.random();
                float b = (float) Math.random();
                float c = (float) Math.random();
                float d = (float) Math.random();
                float e = (float) Math.random();
                adapter.scores.add(Arrays.asList(a, b, c, d, e));
                adapter.notifyDataSetChange();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.scores.size() > 0) {
                    adapter.scores.remove(adapter.scores.size() - 1);
                    adapter.notifyDataSetChange();
                }
            }
        });
        adapter = new MyPentaAdapter();
        view.setPentagramAdapter(adapter);
    }

    class MyPentaAdapter extends PentagramAdapter {
        public List<List<Float>> scores;

        public MyPentaAdapter() {
            this.scores = new ArrayList<>();
        }

        @Override
        public List<String> getVertexText() {
            return Arrays.asList("1", "2", "3", "4", "5");
        }

        @Override
        public List<Float> getVertexScores(int position) {
            return scores.get(position);
        }

        @Override
        public int getCount() {
            return scores.size();
        }

        @Override
        public int getViewColor(int position) {
            return color[position % color.length];
        }
    }
}
