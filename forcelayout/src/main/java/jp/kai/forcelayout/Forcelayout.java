package jp.kai.forcelayout;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.Display;
import android.view.WindowManager;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by kai on 2016/09/03.
 */
public class Forcelayout extends View{
    private static Properties properties = null;
//    private static Nodestyle nodestyle = null;
//    private static Nodestyle nodestyle = null;
    private static Context mContext = null;
    private static HashMap<String, Bitmap> nodeslist = new HashMap<>();
    public static Properties.Node[] nodes = new Properties.Node[200];
    public static Properties.Edge[] edges = new Properties.Edge[500];
    static ArrayList<String> nodename_array = new ArrayList<String>();
    static ArrayList<Bitmap> nodebitmap_array = new ArrayList<>();
    private static ArrayList<String> convertlist = new ArrayList<>();;
    private static int nedges = 0;
    private static float display_width;
    private static float display_height;
    private static int nodeindex = 0; //number of nodes

    private int targetnode = -1;

    private static int roundsize = 5;
    private static int fontsize = 30;
    private static boolean drawline = true;
    private static boolean drawlabel = true;

    public Forcelayout(Context context) {
        super(context);
        mContext = context;
        init();
    }

    private static void init(){
        Properties.Node[] nodes = new Properties.Node[200];
        Properties.Edge[] edges = new Properties.Edge[500];
        nodename_array.clear();
        nodebitmap_array.clear();
        convertlist.clear();
        nedges = 0;
        nodeindex = 0;
    }

    public static Properties with(Context context){
        mContext = context;
        properties = new Properties(context);

        return properties.prepare();
    }

//    public static Nodestyle node(Context context){
//        mContext = context;
//        nodestyle = new Nodestyle(context);
//        return nodestyle.prepare();
//    }
//
//    public static Linkstyle link(Context context){
//        mContext = context;
//        linkstyle = new Linkstyle(context);
//        return linkstyle.prepare();
//    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int touch_x = (int)event.getX();
        int touch_y = (int)event.getY();

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


    // draw function
    @Override
    protected void dispatchDraw(Canvas canvas) {
        Paint paint = new Paint();

        //draw link's line
        if(edges.length != 0){
            for (int i = 0 ; i < nedges-1 ; i++) {
                if (edges[i].group) {
                    Properties.Edge e = edges[i];
                    float x1 = (float) (nodes[e.from].x + nodes[e.from].width/2);
                    float y1 = (float) (nodes[e.from].y + nodes[e.from].height/2);
                    float x2 = (float) (nodes[e.to].x + nodes[e.to].width/2);
                    float y2 = (float) (nodes[e.to].y + nodes[e.to].height/2);

                    if(drawline) {
                        paint.setStrokeWidth(5);
                        float[] pts = {x1, y1, x2, y2};
                        canvas.drawLines(pts, paint);
                    }
                }
            }
        }

        //draw node's image
        for (final String str : nodeslist.keySet()) {
            if(convertlist.indexOf(str) == -1) {
                Bitmap _bitmap = nodeslist.get(str);
                nodebitmap_array.add(getCroppedBitmap(nodeslist.get(str), roundsize));
                convertlist.add(str);
            }
        }

        //draw label
        for (int i = 0; i < convertlist.size(); i++) {
            canvas.drawBitmap(nodebitmap_array.get(i), (int) nodes[i].x, (int) nodes[i].y, paint);

            if(drawlabel) {
                paint.setTextSize(30);
                canvas.drawText(nodes[i].nodename, (int) (nodes[i].x + nodes[i].width), (int) (nodes[i].y + nodes[i].height + 30), paint);
            }
        }

        if(nedges != 0) {

            properties.relax();
        }

        invalidate();
    }


//    public static class Test <L, I> {
//        private final L label;
//        private final I image;
//
//        private Test(L label, I image){
//            this.label = label;
//            this.image = image;
//
//        }
//
//        public static <L, I> Test<L, I> create(L label, I image) {
//            return new Test<>(label, image);
//        }
//    }


    //
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


//    public static class Nodestyle{
//        private Context mContext;
//
//        public Nodestyle(Context context){
//            mContext = context;
//        }
//
//        private Nodestyle prepare(){
//            return this;
//        }
//
//        //setter of node's size
//        public Nodestyle nodesize(int nodeswidth){
//            this.nodeswidth = nodeswidth;
//            return this;
//        }
//
//        private Nodestyle nodes(){
//            return this;
//        }
//
//
//    }



    public static class Properties{
        private static Context mContext;

        private static float nodearea_width; //draw area = screen size
        private static float nodearea_height;
        private static int reduction = 30;
        private static int nodeswidth = 150; //node's width
        private static int distance = 300; //distance between nodes


        private static double gravity = 0.04;
        private static double bounce = 0.08; //
        private static double attenuation = 0.7;//0.9; //
        private static double coulomb = 680; //


        public Properties(Context context){
            mContext = context;
        }

        private Properties prepare(){
            Display mDisplay = getDisplayMetrics(mContext);
            display_width = mDisplay.getWidth();
            display_height = mDisplay.getHeight();

            return this;
        }

        //setter of linkStrength
        public Properties linkStrength(double bounce){
            this.bounce = bounce;
            return this;
        }

        //setter of linkStrength
        public Properties distance(int distance){
            this.distance = distance;
            return this;
        }

        //setter of linkStrength
        public Properties gravity(double gravity){
            this.gravity = gravity;
            return this;
        }

        //setter of node's size
        public Properties nodesize(int nodeswidth){
            this.nodeswidth = nodeswidth;
            return this;
        }

        //draw line
        public Properties drawLine(boolean drawline){
            Forcelayout.drawline = drawline;
            return this;
        }

        //draw label
        public Properties drawLable(boolean drawlabel){
            Forcelayout.drawlabel = drawlabel;
            return this;
        }

        //set fontSize
        public Properties setFontSize(int fontsize){
            Forcelayout.fontsize = fontsize;
            return this;
        }


        //setter of nodes : drawable
        public Properties nodes(final HashMap<String, Integer> nodemaps){
            init_nodes();

            Resources resource = mContext.getResources();
            for (final String str : nodemaps.keySet()) {
                BitmapFactory.Options imageOptions = new BitmapFactory.Options();
                imageOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
                imageOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeResource(resource, nodemaps.get(str), imageOptions);
                // get image width
                int bitmapwidth  = imageOptions.outWidth;

                BitmapFactory.Options bmfOptions = new BitmapFactory.Options();

                //resize
                reduction = bitmapwidth / nodeswidth;
                if(reduction != 0) {
                    bmfOptions.inSampleSize = reduction;
                }

                Bitmap bitmap = BitmapFactory.decodeResource(resource, nodemaps.get(str), bmfOptions);

                int imgheight = bmfOptions.outHeight;
                int imgwidth = bmfOptions.outWidth;


                if(imgwidth != nodeswidth){
                    bitmap = resizeBitmap(bitmap, nodeswidth);

                    imgheight = bitmap.getHeight();
                    imgwidth = bitmap.getWidth();
                }

                nodearea_width = display_width - (int) (imgwidth);
                nodearea_height = display_height - (int) (imgheight);

                addNode(str, nodeindex, imgwidth, imgheight);

                nodename_array.add(str);

                nodeslist.put(str, bitmap);
                nodeindex++;
            }

            return this;
        }


        //setter of links
        public Properties links(final List<String> linkmaps){
            init_links();

            for(int i=0; i < nodename_array.size();i++){
                for(int j=0; j < nodename_array.size(); j++){
                    if(i != j) {
                        addEdge(i, j);
                    }
                }
            }

            for (int k=0; k<linkmaps.size(); k++) {
                String[] pair = linkmaps.get(k).split("-");

                if(pair.length == 2) {
                    for (int i = 0; i < nedges; i++) {
                        if ((edges[i].from == nodename_array.indexOf(pair[0]) && edges[i].to == nodename_array.indexOf(pair[1]) ||
                                (edges[i].to == nodename_array.indexOf(pair[0]) && edges[i].from == nodename_array.indexOf(pair[1])))) {
                            edges[i].group = true;
                        }
                    }
                }
            }

            return  this;
        }

        //class of node
        public static class Node{
            String nodename;

            double x;
            double y;
            double width;
            double height;

            double dx;
            double dy;
        }

        //class of link
        public static class Edge {
            int from;
            int to;
            double len;
            boolean group;
        }


        public static void addNode(String lbl, int index, int width, int height){
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

        public static void addEdge(int from, int to){
            Edge e = new Edge();
            e.from = from;
            e.to = to;
            e.group = false;
            edges[nedges++] = e;
        }


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



                        if(rsq != 0 && Math.sqrt(rsq) < distance) {
                            fx += coulombdist_x / rsq ;
                            fy += coulombdist_y / rsq ;
                        }
                    }

                    //gravity : node - central
                    double distX_c=0,distY_c=0;
                    distX_c = display_width/2 - (nodes[i].x + nodes[i].width/2);
                    distY_c = display_height/2 - (nodes[i].y + nodes[i].height/2);

                    fx += gravity *distX_c;
                    fy += gravity *distY_c;


                    //node in group : from - to
                    for(int j=0; j<nedges-1; j++){
                        double distX=0,distY=0;
                        if(edges[j].group) {
                            if (i == edges[j].from) {
                                distX = nodes[edges[j].to].x - nodes[i].x;
                                distY = nodes[edges[j].to].y - nodes[i].y;

                            } else if (i == edges[j].to) {
                                distX = nodes[edges[j].from].x - nodes[i].x;
                                distY = nodes[edges[j].from].y - nodes[i].y;
                            }
                        }
                        fx += bounce *distX;
                        fy += bounce *distY;
                    }

                    nodes[i].dx = (nodes[i].dx + fx) * attenuation;
                    nodes[i].dy = (nodes[i].dy + fy) * attenuation;


                    nodes[i].x += nodes[i].dx;
                    nodes[i].y += nodes[i].dy;

                }
            }
        }

        private static void init_nodes(){
            Properties.Node[] nodes = new Properties.Node[200];
            nodename_array.clear();
            nodebitmap_array.clear();
            convertlist.clear();
            nodeindex = 0;
        }

        private static void init_links(){
            Properties.Edge[] edges = new Properties.Edge[500];
            nedges = 0;
        }


        //display size
        public static final Display getDisplayMetrics(Context context) {
            WindowManager winMan = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            Display disp = winMan.getDefaultDisplay();
            return disp;
        }

        public static Bitmap resizeBitmap(Bitmap src, int width){
            int srcWidth = src.getWidth();
            int srcHeight = src.getHeight();

            // get Screen size
            Matrix matrix = new Matrix();

            float widthScale = (float)width / (float)srcWidth;
            matrix.postScale(widthScale, widthScale);


            // resize
            Bitmap dst = Bitmap.createBitmap(src, 0, 0, srcWidth, srcHeight, matrix, true);
            src.recycle();
            src = null;

            return dst;
        }

    }
}
