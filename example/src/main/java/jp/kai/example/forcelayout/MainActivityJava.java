package jp.kai.example.forcelayout;

import android.app.Activity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.kai.forcelayout.Forcelayout;

/**
 * Created by kai on 2017/05/05.
 * Usage for Java
 */

public class MainActivityJava extends Activity{


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        final Forcelayout bane = new Forcelayout(getApplication());

        /** set nodes */
        final ArrayList<Pair<String, Integer>> nodes = new ArrayList<>();

        nodes.add(new Pair<>("neko", R.drawable.a));
        nodes.add(new Pair<>("nyanko", R.drawable.b));
        nodes.add(new Pair<>("hoge", R.drawable.c));
        nodes.add(new Pair<>("neko1", R.drawable.d));
        nodes.add(new Pair<>("neko2", R.drawable.d));
        nodes.add(new Pair<>("neko3", R.drawable.d));
        nodes.add(new Pair<>("neko4", R.drawable.d));
        nodes.add(new Pair<>("neko5", R.drawable.d));
        nodes.add(new Pair<>("neko6", R.drawable.d));
        nodes.add(new Pair<>("neko7", R.drawable.e));
        nodes.add(new Pair<>("neko8", R.drawable.e));
        nodes.add(new Pair<>("neko9", R.drawable.f));
        nodes.add(new Pair<>("neko10", R.drawable.f));
        nodes.add(new Pair<>("neko11", R.drawable.g));
        nodes.add(new Pair<>("neko12", R.drawable.g));

        //set links
        List<String> links = Arrays.asList("neko5-neko8","neko-neko4","neko1-neko2","neko2-neko3","neko3-neko1","neko5-neko10","neko3-neko11","neko3-neko12","neko3-nyanko","hoge-neko7");

        bane.with()
                .friction(0.09)
                .distance(200)
                .gravity(0.04)
                .nodes(nodes)
                .links(links);

        Button button1 = new Button(this);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set links
                List<String> links = Arrays.asList("neko-nyanko","neko1-neko2","neko5-neko8","neko-neko4","neko2-neko5");

                bane.with().friction(0.09).gravity(0.04).distance(200).links(links);
            }
        });
        button1.setText("change links");



        SeekBar seek1 = new SeekBar(this);
        seek1.setMax(100);
        seek1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bane.with().gravity((float)progress/1000);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(button1);
        layout.addView(seek1);
        layout.addView(bane);
        setContentView(layout);
    }
}
