package jp.kai.example.forcelayout;

import android.app.Activity;
import android.os.Bundle;

import java.util.HashMap;

import jp.kai.forcelayout.Forcelayout;
/**
 * Created by kai on 2016/09/03.
 */
public class MainActivity extends Activity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        /*
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        setContentView(linearLayout);
        */
        //
        final Forcelayout _bane =  new Forcelayout(getApplicationContext());
        setContentView(_bane);

        //ノードの定義をする
        //TODO リソースファイルだけではなくインターネット上のリソースも対応できるようにしたい
        HashMap<String, Integer> nodes = new HashMap<>();
        String[] name = {"neko", "nyanko", "hoge", "neko1"};

        nodes.put("neko",R.drawable.a);
        nodes.put("nyanko",R.drawable.b);
        nodes.put("hoge",R.drawable.c);
        nodes.put("neko1",R.drawable.d);
        nodes.put("neko2",R.drawable.d);

        //リンクの定義をする
        HashMap<String, String> links = new HashMap<>();
//        links.put("neko", "nyanko");
        links.put("nyanko", "neko");

        Forcelayout.with(this).nodesize(300).linkStrength(0.08).distance(300).nodes(nodes).links(links);


    }

}
