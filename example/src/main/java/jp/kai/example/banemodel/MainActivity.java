package jp.kai.example.banemodel;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        setContentView(linearLayout);

        Banemodel _bane =  new Banemodel(getApplicationContext(), linearLayout);

        HashMap<String, Integer> nodes = new HashMap<>();
        nodes.put("",R.drawable.test);

        Banemodel.with(this).setnodes(nodes);
    }
}
