package jp.kai.example.forcelayout;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

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
		
        final Forcelayout _bane =  new Forcelayout(getApplicationContext());
        setContentView(_bane);



        //ノードの定義をする
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
        nodes.put("neko7",R.drawable.d);
        nodes.put("neko8",R.drawable.d);
        nodes.put("neko9",R.drawable.d);
        nodes.put("neko10",R.drawable.d);
        nodes.put("neko11",R.drawable.d);
        nodes.put("neko12",R.drawable.d);

        //リンクの定義をする
        List<String> links = Arrays.asList("neko5-neko8","neko-neko4","neko1-neko2","neko2-neko3","neko3-neko1","neko5-neko10","neko3-neko11","neko3-neko12","neko3-nyanko","hoge-neko7");

        Forcelayout.with(this).nodesize(120).linkStrength(0.09).distance(200).nodes(nodes).links(links);


    }

}
