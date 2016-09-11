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

        //リンクの定義をする
//        HashMap<String, String> links = new HashMap<>();
//        links.put("neko", "nyanko");
//        links.put("neko", "hoge");
//        links.put("neko1", "neko2");
//        links.put("neko2", "neko3");
//        links.put("neko3", "neko1");

        List<String> links = Arrays.asList("neko-nyanko","neko-hoge","neko1-neko2","neko2-neko3","neko3-neko1");

        Forcelayout.with(this).nodesize(100).linkStrength(0.08).distance(250).nodes(nodes).links(links);


    }

}
