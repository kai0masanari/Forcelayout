package jp.kai.banemodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.HashMap;


/**
 * Created by kai on 2016/09/03.
 */
public class Banemodel extends ViewGroup{
    private final static Properties properties = new Properties();
    private static Context mContext = null;
    private static ViewGroup mView = null;

    public Banemodel(Context context, ViewGroup vg) {
        super(context);
        mContext = context;
        mView = vg;
    }

    /*
    private Banemodel(){
        super();
    }*/

    public static Properties with(Context context){
        mContext = context;
        return properties.prepare(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }


    public static class Properties{
        public Properties(){}

        private Properties prepare(Context context){


            return this;
        }

        //ノードのセッター
        public static void setnodes(final HashMap<String, Integer> nodes){
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //view更新
                    //String = name Integer = Resourcefile
                    for (final String str : nodes.keySet()) {
                        Bitmap bitmap = null;
                        bitmap = BitmapFactory.decodeResource(mContext.getResources(), nodes.get(str));
                        ImageView image = new ImageView(mContext);
                        image.setImageBitmap(bitmap);
                        mView.addView(image, new ViewGroup.LayoutParams(10, 10));
                    }
                }
            });
        }

        //リンクのセッター
        public static void setlinks(){

        }

        //描画の開始
        public static void start(){

        }

    }

}
