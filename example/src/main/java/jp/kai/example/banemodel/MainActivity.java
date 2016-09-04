package jp.kai.example.banemodel;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;

import jp.kai.example.banemodel.R;
import jp.kai.banemodel.Banemodel;
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
        final Banemodel _bane =  new Banemodel(getApplicationContext());
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
        Banemodel.Properties hoge = Banemodel.with(this);
        hoge.setnodes(nodes);
        hoge.setlinks(links);
        //String hodge = "";


    }

}
