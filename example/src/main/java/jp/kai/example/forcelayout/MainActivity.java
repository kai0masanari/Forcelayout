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

        //リンクの定義をする
        HashMap<String, String> links = new HashMap<>();


        int index = (int)(Math.random()*name.length);
        int index2 = (int)(Math.random()*name.length);


        if(index != index2) {
            links.put(name[index], name[index2]);
        }else if(index >0){
            links.put(name[index-1], name[index2]);
        }
        //links.put("nyanko","hoge");

        //
        Forcelayout.Properties hoge = Forcelayout.with(this).setnodes(nodes).setlinks(links);
        //String hodge = "";


    }

}
