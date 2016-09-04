package jp.kai.banemodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.Display;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by kai on 2016/09/03.
 */
public class Banemodel extends ViewGroup{
    private final static Properties properties = new Properties();
    private static Context mContext = null;
    private static ViewGroup mView;
    private static HashMap<String, ImageView> nodeslist = new HashMap<>();
    public static Properties.Node[] nodes = new Properties.Node[200];
    public static Properties.Edge[] edges = new Properties.Edge[500];
    static ArrayList<String> nodename_array = new ArrayList<String>();
    static ArrayList<Bitmap> nodebitmap_array = new ArrayList<>();
    private static ArrayList<String> convertlist = new ArrayList<>();;
    private static int nedges = 0;

    private static boolean isanimated = false;




    //private static HashMap<TextView, ImageView> nodeslist = new HashMap<>();

    public Banemodel(Context context) {
        super(context);
        //setWillNotDraw(false);
        mContext = context;
        mView = new LinearLayout(context.getApplicationContext());
        nodename_array.clear();
        nodebitmap_array.clear();
        convertlist.clear();
        nedges = 0;

        //定期処理ハンドラの生成と実行


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


    // 描画処理を記述
    @Override
    protected void dispatchDraw(Canvas canvas) {
        Paint paint = new Paint();
        if(edges.length != 0){
            for (int i = 0 ; i < nedges-1 ; i++) {
                if (edges[i].group) {
                    Properties.Edge e = edges[i];
                    Log.i(e.from+"width",""+nodes[e.from].width/2 );
                    float x1 = (float) (nodes[e.from].x + nodes[e.from].width/2);
                    float y1 = (float) (nodes[e.from].y + nodes[e.from].height/2);
                    float x2 = (float) (nodes[e.to].x + nodes[e.to].width/2);
                    float y2 = (float) (nodes[e.to].y + nodes[e.to].height/2);
                    //int len = (int) Math.abs(Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)) - e.len);
                    paint.setStrokeWidth(5);
                    float[] pts = {x1, y1, x2, y2};
                    canvas.drawLines(pts, paint);


                    Log.i("dispatchDraw", "test");
                }
            }
        }

        for (final String str : nodeslist.keySet()) {
            if(convertlist.indexOf(str) == -1) {
                nodebitmap_array.add(((BitmapDrawable) nodeslist.get(str).getDrawable()).getBitmap());
                convertlist.add(str);
            }
        }
        for(int i=0; i<convertlist.size(); i++){
            Log.i("Image","index is "+i);
            Log.i("Image","converted num is "+convertlist.size());
            canvas.drawBitmap(nodebitmap_array.get(i), (int)nodes[i].x, (int)nodes[i].y, paint);

        }

        if(nedges != 0) {

            properties.relax();
        }

    }



    public static class Properties implements Animation.AnimationListener{
        //画面関連
        //TODO ゆくゆくはばねモデルの表示領域も指定できるようにし、それに対応させたい。
        private static float display_width;
        private static float display_height;
        private static float nodearea_width; //実際のノードの範囲
        private static float nodearea_height;
        private static int reduction = 8;

        //ノード関連
        private static int screenX = 0;
        private static int screenY = 0;
        private static int nodeindex = 0; //実際のノードの数

        private static int targetLocalX = 0; //ドラッグ時の座標保存
        private static int targetLocalY = 0;
        //TODO Nodeクラスにドラッグ判定も追加すること
        private static boolean dragging = false; //ドラッグ中かの判定

        //ばねモデルのパラメータ
        private static double bounce = 0.1; //ばね定数
        private static double attenuation = 0.8; //減衰定数
        private static double coulomb = 600; //クーロン


        public Properties(){}

        private Properties prepare(Context context){
            Display mDisplay = getDisplayMetrics(context);
            display_width = mDisplay.getWidth();
            display_height = mDisplay.getHeight();

            nodeindex = 0;
            return this;
        }

        //ノードのセッター
        public static void setnodes(final HashMap<String, Integer> nodemaps){
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (final String str : nodemaps.keySet()) {
                        // ビットマップ作成オブジェクトの設定
                        BitmapFactory.Options bmfOptions = new BitmapFactory.Options();



                        // 画像を1/？サイズに縮小する
                        bmfOptions.inSampleSize = reduction;
                        // メモリの解放
                        bmfOptions.inPurgeable = true;

                        final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
                        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), nodemaps.get(str), bmfOptions);
                        // 画像の元サイズ 取得
                        //TODO 元サイズに応じて縮小サイズを動的に変えること
                        final int imgheight = bmfOptions.outHeight;
                        final int imgwidth = bmfOptions.outWidth;


                        //ノード
                        ImageView nodeimage = new ImageView(mContext);
                        nodeimage.setImageBitmap(bitmap);

                        nodearea_width = display_width - (int)(imgwidth);
                        nodearea_height = display_height - (int)(imgheight);

                        /*
                        Log.i("nodearea_width",""+nodearea_width);
                        Log.i("nodearea_height",""+nodearea_height);
                        Log.i("size?w",""+display_width);
                        */
                        double imgwidth_d = (double)(imgwidth/reduction);

                        addNode(str, nodeindex, imgwidth, imgheight);

                        nodeimage.setTranslationX((float) nodes[nodeindex].x);
                        nodeimage.setTranslationY((float) nodes[nodeindex].y);
                        mView.addView(nodeimage, new ViewGroup.LayoutParams(WC, WC));

                        nodename_array.add(str);


                        //TODO TextViewとImageViewのコンテナを作って管理すること
                        /*
                        TextView nodename = new TextView(mContext);
                        nodename.setText(str);
                        nodename.setTranslationX(size[0]);
                        nodename.setTranslationY(size[1]+10);
                        mView.addView(nodename, new LinearLayou.LayoutParams(WC, WC));
                        */

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
        public static void setlinks(final HashMap<String, String> linkmaps){
            Handler handler = new Handler();
            handler.post(new Runnable() {

                @Override
                public void run() {
                    for(int i=0; i < nodename_array.size();i++){
                        for(int j=0; j<nodename_array.size(); j++){
                            if(i != j) {
                                addEdge(i, j);
                            }
                        }
                    }

                    for (final String str : linkmaps.keySet()) {
                        for(int i=0; i<nedges-1; i++){
                            if (edges[i].from == nodename_array.indexOf(str) && edges[i].to == nodename_array.indexOf(linkmaps.get(str))){
                                edges[i].group = true;
                            }
                                //addEdge(nodename_array.indexOf(str), nodename_array.indexOf(linkmaps.get(str)));
                        }
                    }
                }
            });
        }

        //ノードクラス
        public static class Node{
            String nodename;

            double x;
            double y;
            double width;
            double height;

            double dx;
            double dy;
        }

        //リンククラス
        public static class Edge {
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
        public static void addNode(String lbl, int index, int width, int height){
            //System.out.println(lbl);
            Node n = new Node();

            n.x = (nodearea_width)*Math.random();
            n.y = (nodearea_height-10)*(Math.random())+10;
            n.nodename = lbl;
            n.width = width;
            n.height = height;
            n.dx = 0;
            n.dy = 0;

            nodes[index] = n;

        }

        //リンクの追加
        public static void addEdge(int from, int to){
            Edge e = new Edge();
            e.from = from;
            e.to = to;
            e.len = 0;
            e.group = true;
            edges[nedges++] = e;

        }


        //ばねの動作
        public void relax(){
            if(nedges != 0){
                for(int i=0; i<nodeindex; i++){
                    double fx = 0,fy = 0;


                    for(int j=0; j<nodeindex; j++){

                        double distX = (int)((nodes[i].x + nodes[i].width/2) - (nodes[j].x + nodes[j].width/2));
                        double distY = (int)((nodes[i].y + nodes[i].height/2) - (nodes[j].y + nodes[j].height/2));
                        double rsq = distX * distX + distY *distY;
                        Log.v("distX",""+distX);
                        int rsq_round = (int)rsq*100;
                        rsq = rsq_round/100;
                        //Log.v("jtransforms2",""+rsq);
                        //Log.v("coulomb",""+coulomb * distX);

                        double coulombdist_x = coulomb * distX;
                        double coulombdist_y = coulomb * distY;
                        int coulombdist_round_x = (int)coulombdist_x*100;
                        int coulombdist_round_y = (int)coulombdist_y*100;
                        coulombdist_x = coulombdist_round_x/100;
                        coulombdist_y = coulombdist_round_y/100;

                        if(rsq != 0) {
                            //fx += (coulomb * distX) / rsq;
                            fx += coulombdist_x / rsq;
                            //fy += (coulomb * distY) / rsq;
                            fy += coulombdist_y / rsq;
                        }
                        Log.i("round_coulombdist_x",""+ coulombdist_x);
                        Log.i("round_rsq",""+ rsq);
                    }

                    //target node
                    for(int j=0; j<nedges-1; j++){
                        double distX=0,distY=0;
                        if(i == edges[j].from ){
                            distX = nodes[edges[j].to].x - nodes[i].x;
                            distY = nodes[edges[j].to].y - nodes[i].y;

                        } else if( i== edges[j].to){
                            distX = nodes[edges[j].from].x - nodes[i].x;
                            distY = nodes[edges[j].from].y - nodes[i].y;
                        }
                        fx += bounce *distX;
                        fy += bounce *distY;
                    }

                    //速度の算出
                    nodes[i].dx = (nodes[i].dx + fx) * attenuation;
                    nodes[i].dy = (nodes[i].dy + fy) * attenuation;

                    //速度をもとに収束させてく
                    nodes[i].x += nodes[i].dx;
                    nodes[i].y += nodes[i].dy;
                    Log.i("relax", ""+fx);

                }
            }
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

        private static float[] getSize(ImageView img) {
            Rect rect = img.getDrawable().getBounds();
            float scaleX = (float) img.getWidth() / (float) rect.width();
            float scaleY = (float) img.getHeight() / (float) rect.height();
            float scale = Math.min(scaleX, scaleY);
            float width = scale * rect.width();
            float height = scale * rect.height();
            return new float[] {width, height};
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
