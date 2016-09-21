package jp.kai.example.forcelayout;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import jp.kai.forcelayout.Forcelayout;
/**
 * Created by kai on 2016/09/03.
 */
public class MainActivity extends Activity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_main);
//        Forcelayout _bane = (Forcelayout) findViewById(R.id.sample_logview);

        final Forcelayout _bane =  new Forcelayout(getApplicationContext());

        Button button1 = new Button(this);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set links
                List<String> links = Arrays.asList("neko-nyanko","neko1-neko2","neko5-neko8","neko-neko4","neko2-neko5");

                _bane.with(getApplicationContext()).linkStrength(0.09).gravity(0.04).distance(200).links(links);
            }
        });
        button1.setText("change links");

        Button button2 = new Button(this);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set links
                List<String> links = Arrays.asList("neko-nyanko","neko1-neko2","neko5-neko8","neko-neko4","neko2-neko5");

                _bane.with(getApplicationContext()).linkStrength(0.1).gravity(0.3).distance(500);
            }
        });
        button2.setText("change linkStrength and gravity");


        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(button1);
        layout.addView(button2);
        layout.addView(_bane);
        setContentView(layout);



        //set nodes
        HashMap<String, Integer> nodes = new HashMap<>();

        nodes.put("neko",R.drawable.a);
        nodes.put("nyanko",R.drawable.b);
        nodes.put("hoge",R.drawable.c);
        nodes.put("neko1",R.drawable.d);
        nodes.put("neko2",R.drawable.d);
        nodes.put("neko3",R.drawable.d);
        nodes.put("neko4",R.drawable.d);
        nodes.put("neko5",R.drawable.d);
        nodes.put("neko6",R.drawable.d);
        nodes.put("neko7",R.drawable.e);
        nodes.put("neko8",R.drawable.e);
        nodes.put("neko9",R.drawable.f);
        nodes.put("neko10",R.drawable.f);
        nodes.put("neko11",R.drawable.g);
        nodes.put("neko12",R.drawable.g);

        //set links
        List<String> links = Arrays.asList("neko5-neko8","neko-neko4","neko1-neko2","neko2-neko3","neko3-neko1","neko5-neko10","neko3-neko11","neko3-neko12","neko3-nyanko","hoge-neko7");

        _bane.with(this)
                .nodesize(200)
                .linkStrength(0.09)
                .distance(200)
                .gravity(0.04)
                .nodes(nodes)
                .links(links);


    }


    @Override protected void onStart() {
        super.onStart();
    }

}
