package jp.kai.forcelayout;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
public class Forcelayout extends View{
    private static Properties properties = null;
    private static Context mContext = null;
    private static ViewGroup mView;
    private static HashMap<String, Bitmap> nodeslist = new HashMap<>();
    public static Properties.Node[] nodes = new Properties.Node[200];
    public static Properties.Edge[] edges = new Properties.Edge[500];
    static ArrayList<String> nodename_array = new ArrayList<String>();
    static ArrayList<Bitmap> nodebitmap_array = new ArrayList<>();
    private static ArrayList<String> convertlist = new ArrayList<>();;
    private static int nedges = 0;
    private static float display_width;
    private static float display_height;
    private static int nodeindex = 0; //実際のノードの数

    private int targetnode = -1;

    public Forcelayout(Context context) {
        super(context);
        mContext = context;
        mView = new LinearLayout(context.getApplicationContext());
        nodename_array.clear();
        nodebitmap_array.clear();
        convertlist.clear();
        nedges = 0;
    }

    public static Properties with(Context context){
        mContext = context;
        properties = new Properties(context);
        return properties.prepare();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int touch_x = (int)event.getX();
        int touch_y = (int)event.getY();

        Log.d("TouchEvent","X:"+touch_x+" Y:"+touch_y);

        switch ( event.getAction() ) {

            case MotionEvent.ACTION_DOWN:
                for(int i=0; i< nodeindex; i++){
                    if((nodes[i].x + nodes[i].width >= touch_x && nodes[i].x <= touch_x)
                            && (nodes[i].y + nodes[i].height >= touch_y && nodes[i].y <= touch_y)){
                        targetnode = i;
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if(targetnode != -1){
                    nodes[targetnode].x = touch_x-nodes[targetnode].width/2;
                    nodes[targetnode].y = touch_y-nodes[targetnode].height/2;
                }
                break;

            case MotionEvent.ACTION_UP:
                targetnode = -1;
                break;

            case MotionEvent.ACTION_CANCEL:
                targetnode = -1;
                break;

        }

        return true;
    }


    // 描画処理を記述
    @Override
    protected void dispatchDraw(Canvas canvas) {
        Paint paint = new Paint();
        if(edges.length != 0){
            for (int i = 0 ; i < nedges-1 ; i++) {
                if (edges[i].group) {
                    Properties.Edge e = edges[i];
                    float x1 = (float) (nodes[e.from].x + nodes[e.from].width/2);
                    float y1 = (float) (nodes[e.from].y + nodes[e.from].height/2);
                    float x2 = (float) (nodes[e.to].x + nodes[e.to].width/2);
                    float y2 = (float) (nodes[e.to].y + nodes[e.to].height/2);
                    //int len = (int) Math.abs(Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)) - e.len);
                    paint.setStrokeWidth(5);
                    float[] pts = {x1, y1, x2, y2};
                    canvas.drawLines(pts, paint);
                }
            }
        }

        for (final String str : nodeslist.keySet()) {
            if(convertlist.indexOf(str) == -1) {
                //Bitmap _bitmap = ((BitmapDrawable) nodeslist.get(str).getDrawable()).getBitmap();
                Bitmap _bitmap = nodeslist.get(str);

//                nodebitmap_array.add(getCroppedBitmap(_bitmap, 5));
                nodebitmap_array.add(getCroppedBitmap(nodeslist.get(str), 5));
                convertlist.add(str);
            }
        }
        for(int i=0; i<convertlist.size(); i++){
            canvas.drawBitmap(nodebitmap_array.get(i), (int)nodes[i].x, (int)nodes[i].y, paint);
            paint.setTextSize (30);
            canvas.drawText(nodes[i].nodename, (int)(nodes[i].x+nodes[i].width), (int)(nodes[i].y+nodes[i].height+30), paint);

        }

        if(nedges != 0) {

            properties.relax();
        }
        invalidate();
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap, int round) {

        int width  = bitmap.getWidth();
        int height = bitmap.getHeight();

        final Rect rect   = new Rect(0, 0, width, height);
        final RectF rectf = new RectF(0, 0, width, height);

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        paint.setAntiAlias(true);

        canvas.drawRoundRect(rectf, width / round, height / round, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;

    }


    public static class Properties{
        private static Context mContext;

        //画面関連
        //TODO Forcelayoutの表示領域、ノードのサイズ、ばね定数や減衰定数などのパラメータを指定できりゅおうにすること
        private static float nodearea_width; //ノードを描画する範囲　現状は画面いっぱいとる
        private static float nodearea_height;
        private static int reduction = 30;
        private static int nodeswidth = 150; //描画するノードの幅 ユーザに指定できるようにしたい

        //ばねモデルのパラメータ ユーザに指定できるようにする
        private static double bounce = 0.008; //ばね定数
        private static double attenuation = 0.8;//0.9; //減衰定数
        private static double coulomb = 680; //クーロン


        public Properties(Context context){
            mContext = context;
        }

        private Properties prepare(){
            Display mDisplay = getDisplayMetrics(mContext);
            display_width = mDisplay.getWidth();
            display_height = mDisplay.getHeight();
            return this;
        }

        //ノードのセッター
        public Properties setnodes(final HashMap<String, Integer> nodemaps){
            Resources resource = mContext.getResources();
            for (final String str : nodemaps.keySet()) {


                Bitmap bitmap = BitmapFactory.decodeResource(resource, nodemaps.get(str));
                // 画像サイズ取得
                int bitmapwidth  = bitmap.getWidth();
                int bitmapheight = bitmap.getHeight();

                bitmap.recycle();
                bitmap = null;

                // ビットマップ作成オブジェクトの設定
                BitmapFactory.Options bmfOptions = new BitmapFactory.Options();

                // 画像を1/？サイズに縮小する
                reduction = bitmapwidth / nodeswidth;
                bmfOptions.inSampleSize = reduction;
                // メモリの解放
                bmfOptions.inPurgeable = true;
                bitmap = BitmapFactory.decodeResource(resource, nodemaps.get(str), bmfOptions);

                final int imgheight = bmfOptions.outHeight;
                final int imgwidth = bmfOptions.outWidth;

                nodearea_width = display_width - (int) (imgwidth);
                nodearea_height = display_height - (int) (imgheight);
                double imgwidth_d = (double) (imgwidth / reduction);

                addNode(str, nodeindex, imgwidth, imgheight);

                nodename_array.add(str);

                //TODO TextViewとImageViewのコンテナを作って管理すること

                nodeslist.put(str, bitmap);
                //bitmap.recycle();
                //bitmap = null;

                nodeindex++;
            }

            return this;
        }

        //リンクのセッター
        public Properties setlinks(final HashMap<String, String> linkmaps){
            Handler handler = new Handler();
            handler.post(new Runnable() {

                @Override
                public void run() {
                    for(int i=0; i < nodename_array.size()-1;i++){
                        for(int j=i+1; j<nodename_array.size(); j++){
                            if(i != j) {
                                addEdge(i, j);
                            }
                        }
                    }

                    for (final String str : linkmaps.keySet()) {
                        for(int i=0; i<nedges-1; i++){
                            if ((edges[i].from == nodename_array.indexOf(str) && edges[i].to == nodename_array.indexOf(linkmaps.get(str)))  ||
                                            (edges[i].to == nodename_array.indexOf(str) && edges[i].from == nodename_array.indexOf(linkmaps.get(str)))){
                                edges[i].group = true;
                                Log.d("setLinks","from:"+str+" to:"+linkmaps.get(str));
                            }
                        }
                    }
                }
            });

            return this;
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
            e.group = false;
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
                        int rsq_round = (int)rsq*100;
                        rsq = rsq_round/100;

                        double coulombdist_x = coulomb * distX;
                        double coulombdist_y = coulomb * distY;
                        int coulombdist_round_x = (int)coulombdist_x*100;
                        int coulombdist_round_y = (int)coulombdist_y*100;
                        coulombdist_x = coulombdist_round_x/100;
                        coulombdist_y = coulombdist_round_y/100;


                        boolean isgroup = false;
                        for(int k=0; k<nedges;k++){
                            if(edges[k].to == nodename_array.indexOf(nodes[i])){
                                isgroup = edges[k].group;
                                break;
                            }

                        }


                        if(rsq != 0 && !isgroup) {
                            fx += (coulombdist_x / rsq) ;
                            fy += coulombdist_y / rsq ;
                        }
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

                        fx += bounce *distX*1.1;
                        fy += bounce *distY*1.1;

                    }
                    //速度の算出
                    nodes[i].dx = (nodes[i].dx + fx) * attenuation;
                    nodes[i].dy = (nodes[i].dy + fy) * attenuation;


                    //速度をもとに収束させてく
                    if(nodes[i].x < display_width - nodes[i].width && nodes[i].x > 0) {
                        nodes[i].x += nodes[i].dx;
                    }

                    //速度をもとに収束させてく
                    if(nodes[i].y < display_height - nodes[i].height && nodes[i].y > 0) {
                        nodes[i].y += nodes[i].dy;
                    }

                }
            }
        }

        //display size
        public static final Display getDisplayMetrics(Context context) {
            WindowManager winMan = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            Display disp = winMan.getDefaultDisplay();
            //DisplayMetrics dispMet = new DisplayMetrics();
            //disp.getMetrics(dispMet);
            return disp;
        }

        /*
        private static float[] getSize(ImageView img) {
            Rect rect = img.getDrawable().getBounds();
            float scaleX = (float) img.getWidth() / (float) rect.width();
            float scaleY = (float) img.getHeight() / (float) rect.height();
            float scale = Math.min(scaleX, scaleY);
            float width = scale * rect.width();
            float height = scale * rect.height();
            return new float[] {width, height};
        }
        */
    }
}
