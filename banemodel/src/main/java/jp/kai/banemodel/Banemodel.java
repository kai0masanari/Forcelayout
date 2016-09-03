package jp.kai.banemodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.Image;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.Display;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Random;


/**
 * Created by kai on 2016/09/03.
 */
public class Banemodel extends ViewGroup{
    private final static Properties properties = new Properties();
    private static Context mContext = null;
    private static ViewGroup mView = null;
    private static HashMap<String, ImageView> nodeslist = new HashMap<>();
    public static Properties.Node[] nodes = new Properties.Node[200];
    int nedges = 0;
    public Properties.Edge links[] = new Properties.Edge[500];

    private static boolean isanimated = false;




    //private static HashMap<TextView, ImageView> nodeslist = new HashMap<>();


    private static NodeResize _resize = new NodeResize();

    public Banemodel(Context context, ViewGroup vg) {
        super(context);
        mContext = context;
        mView = vg;
    }

    public static Properties with(Context context){
        mContext = context;
        return properties.prepare(context);

    }

    public static void start(){

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event)  {
        // タッチされたらまずonInterceptTouchEventが呼ばれる
        // ここでtrueを返せば親ViewのonTouchEvent
        // ここでfalseを返せば子ViewのonClickやらonLongClickやら

        return false;
    }
    public boolean dispatchTouchEvent(MotionEvent event) {

        return false;
    }

    public static class Properties implements Animation.AnimationListener{
        //画面幅
        //TODO ゆくゆくはばねモデルの表示領域も指定できるようにし、それに対応させたい。
        private static float display_width;
        private static float display_height;
        private static float nodes_dis = 100; //default value
        private static int nodeindex = 0;
        private static int screenX = 0;
        private static int screenY = 0;
        private static int targetLocalX = 0;
        private static int targetLocalY = 0;


        public Properties(){}

        private Properties prepare(Context context){
            Display mDisplay = getDisplayMetrics(context);
            display_width = mDisplay.getWidth();
            display_height = mDisplay.getHeight();
            return this;
        }

        //ノードのセッター
        public static void setnodes(final HashMap<String, Integer> nodemaps){
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {

                    //view更新
                    //String = name Integer = Resourcefile
                    for (final String str : nodemaps.keySet()) {
                        // ビットマップ作成オブジェクトの設定
                        BitmapFactory.Options bmfOptions = new BitmapFactory.Options();

                        // 画像の元サイズ 取得
                        //TODO 元サイズに応じて縮小サイズを動的に変えること
                        final int height = bmfOptions.outHeight;
                        final int width = bmfOptions.outWidth;
                        // 画像を1/？サイズに縮小（メモリ対策）
                        bmfOptions.inSampleSize = 8;

                        // システムメモリ上に再利用性の無いオブジェクトがある場合に勝手に解放（メモリ対策）
                        bmfOptions.inPurgeable = true;

                        final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
                        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), nodemaps.get(str), bmfOptions);

                        addNode(str,nodeindex);

                        ImageView nodeimage = new ImageView(mContext);
                        nodeimage.setImageBitmap(bitmap);
                        nodeimage.setTranslationX((float) nodes[nodeindex].x);
                        nodeimage.setTranslationY((float) nodes[nodeindex].y);
                        mView.addView(nodeimage, new ViewGroup.LayoutParams(WC, WC));

                        //float[] size = getSize(nodeimage);

                        //TODO TextViewとImageViewのコンテナを作って管理すること
                        TextView nodename = new TextView(mContext);
                        nodename.setText(str);
                        //nodename.setTranslationX(size[0]);
                        //nodename.setTranslationY(size[1]+10);
                        //mView.addView(nodename, new LinearLayou.LayoutParams(WC, WC));

                        nodeslist.put(str,nodeimage);
                        nodeslist.get(str).setOnTouchListener(Touchlistener);

                        Log.i("hogehoge","indices "+nodeindex+ " nodename "+str);

                        Log.i("hogehoge","indices "+nodeindex+ " nodename "+nodes[nodeindex].nodename);
                        nodeindex++;
                    }
                }
            });
            start();
        }



        //リンクのセッター
        public static void setlinks(){

        }

        //ノードクラス
        public static class Node{
            String nodename;

            double x;
            double y;

            double dx;
            double dy;
        }

        //リンククラス
        public class Edge {
            int from;
            int to;
            double len;
            boolean group;
        }

        //描画の開始
        public static void start() {
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //isanimated = true;
                    //while(isanimated){
                    for (final String textView : nodeslist.keySet()) {

                        //ImageView nodeimage = new ImageView(mContext);
                        Log.i("akio", "guniko : "+textView);
                        /*
                        TranslateAnimation animation = new TranslateAnimation(0, 50, 0, 0);
                        animation.setDuration(5000);
                        animation.setFillAfter(true);
                        //animation.setAnimationListener();
                        nodeslist.get(textView).startAnimation(animation);
                        */
                    }
                    //}
                }
            });
        }

        //ノードの追加
        public static void addNode(String lbl, int index){
            //System.out.println(lbl);
            Node n = new Node();

            n.x = (display_width)*Math.random();
            n.y = (display_height)*Math.random();
            n.nodename = lbl;

            Log.i("akio", "guniko_x : "+n.x + " guniko_y : "+n.y);

            nodes[index] = n;

        }

        //ばねの動作
        private void relax(){
            /*
            if(links.length != 0){
                for(int i=0; i<links.length; i=i+1){
                    double x1,x2,y1,y2;
                    x1 = nodeObj[links[i].from].x + width/2;
                    y1 = nodeObj[links[i].from].y + height/2;
                    x2 = nodeObj[links[i].to].x + width/2;
                    y2 = nodeObj[links[i].to].y + height/2;

                    double vx = x1-x2;
                    double vy = y1-y2;

                    if(get_distance(x1,y1,x2,y2) < nodes_dis){
                        if(!nodeObj[links[i].to].unchor){
                            nodeObj[links[i].to].x += vx * ((get_distance(x1,y1,x2,y2)-nodes_dis))/10;
                            nodeObj[links[i].to].y += vy * ((get_distance(x1,y1,x2,y2)-nodes_dis))/10;
                        }
                        if(!nodeObj[links[i].from].unchor){
                            nodeObj[links[i].from].x -= vx * ((get_distance(x1,y1,x2,y2)-nodes_dis))/10;
                            nodeObj[links[i].from].y -= vy * ((get_distance(x1,y1,x2,y2)-nodes_dis))/10;
                        }
                    }else if(get_distance(x1,y1,x2,y2) > nodes_dis){
                        if(!nodeObj[links[i].to].unchor){
                            nodeObj[links[i].to].x += vx * ((get_distance(x1,y1,x2,y2)-nodes_dis))/10000;
                            nodeObj[links[i].to].y += vy * ((get_distance(x1,y1,x2,y2)-nodes_dis))/10000;
                        }
                        if(!nodeObj[links[i].from].unchor){
                            nodeObj[links[i].from].x -= vx * ((get_distance(x1,y1,x2,y2)-nodes_dis))/10000;
                            nodeObj[links[i].from].y -= vy * ((get_distance(x1,y1,x2,y2)-nodes_dis))/10000;
                        }
                    }
                }
            }
            */
        }

        //2点から距離を求める
        private double get_distance(double c_position_x, double c_position_y, double b_position_x, double b_position_y){
            double distance = Math.sqrt(Math.pow(c_position_x - b_position_x, 2)+Math.pow(c_position_y - b_position_y, 2));
            return distance;
        }

        //display size
        public static final Display getDisplayMetrics(Context context) {
            WindowManager winMan = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            Display disp = winMan.getDefaultDisplay();
            //DisplayMetrics dispMet = new DisplayMetrics();
            //disp.getMetrics(dispMet);
            Log.i("hamukatu...",""+disp.getWidth());
            Log.i("hamukatu...",""+disp.getHeight());
            return disp;
        }

        //listener
        private static OnTouchListener Touchlistener = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int)event.getRawX();
                int y = (int)event.getRawY();

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        targetLocalX = v.getLeft();
                        targetLocalY = v.getTop();

                        screenX = x;
                        screenY = y;

                        break;

                    case MotionEvent.ACTION_MOVE:

                        int diffX = screenX - x;
                        int diffY = screenY - y;

                        targetLocalX -= diffX;
                        targetLocalY -= diffY;

                        v.layout(targetLocalX,
                                targetLocalY,
                                targetLocalX + v.getWidth(),
                                targetLocalY + v.getHeight());

                        screenX = x;
                        screenY = y;

                        break;

                    case MotionEvent.ACTION_UP:
                        break;
                }
                return true;
            }
        };

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

        public static void stop(){
            isanimated = false;
        }
    }

}
