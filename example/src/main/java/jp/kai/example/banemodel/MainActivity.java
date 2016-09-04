package jp.kai.example.banemodel;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
        nodes.put("neko",R.drawable.test2);
        nodes.put("nyanko",R.drawable.test2);
        nodes.put("hoge",R.drawable.test2);

        //リンクの定義をする
        HashMap<String, String> links = new HashMap<>();
        links.put("neko","nyanko");
        links.put("nyanko","hoge");

        //
        Banemodel.Properties hoge = Banemodel.with(this);
        hoge.setnodes(nodes);
        hoge.setlinks(links);
        //String hodge = "";


    }

}
